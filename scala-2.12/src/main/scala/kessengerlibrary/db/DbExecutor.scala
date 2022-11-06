package io.github.malyszaryczlowiek
package kessengerlibrary.db


import kessengerlibrary.db.queries._
import kessengerlibrary.db.queries.ERROR
import kessengerlibrary.domain.Domain.{ChatId, ChatName, DbResponse, Login, Offset, Partition, Password, UserID}
import kessengerlibrary.domain.{Chat, Domain, SessionInfo, Settings, User}
import kessengerlibrary.kafka.configurators.KafkaConfigurator
import kessengerlibrary.kafka.configurators.KafkaConfigurator.configurator

import java.sql.{Connection, PreparedStatement, ResultSet, Savepoint}
import java.time.ZoneId
import java.util.UUID

import scala.collection.mutable.ListBuffer
import scala.concurrent.duration.{Duration, SECONDS}
import scala.concurrent.{Await, ExecutionContext, Future, duration}
import scala.util.{Failure, Success, Try, Using}



class DbExecutor(val kafkaConfigurator: KafkaConfigurator) {


  def createUser(user: User, pass: Password, settings: Settings)(implicit connection: Connection): DbResponse[Int] = {
    val sql = "INSERT INTO users (user_id, login, pass) VALUES (?, ?, ?)"
    connection.setAutoCommit(false)
    val beforeAnyInsertions: Savepoint = connection.setSavepoint()
    Using(connection.prepareStatement(sql)) {
      (statement: PreparedStatement) =>
        statement.setObject(1, user.userId)
        statement.setString(2, user.login)
        statement.setString(3, pass)
        statement.executeUpdate()
    } match {
      case Failure(ex) =>
        connection.rollback(beforeAnyInsertions)
        connection.setAutoCommit(true)
        if (ex.getMessage.contains("duplicate key value violates unique constraint")) {
          Left(QueryError(ERROR, LoginTaken))
        }
        else handleExceptionMessage(ex)
      case Success(a) =>
        if (a == 1) {
          val sql2 = "INSERT INTO settings (user_id, zone_id) VALUES (?, ?)"
          Using(connection.prepareStatement( sql2 )) {
            (statement: PreparedStatement) =>
              statement.setObject(1, user.userId)
              statement.setString(2, settings.zoneId.getId)
              statement.executeUpdate()
          } match {
            case Failure(ex) =>
              connection.rollback(beforeAnyInsertions)
              connection.setAutoCommit(true)
              if (ex.getMessage.contains("duplicate key value violates unique constraint")) {
                Left(QueryError(ERROR, DataProcessingError))
              }
              handleExceptionMessage(ex)
            case Success(value) =>
              if (value == 1) {
                connection.commit()
                connection.setAutoCommit(true)
                Right(value)
              } else {
                connection.rollback(beforeAnyInsertions)
                connection.setAutoCommit(true)
                Left(QueryError(ERROR, DataProcessingError))
              }
          }
        } else {
          connection.rollback(beforeAnyInsertions)
          connection.setAutoCommit(true)
          Left(QueryError(ERROR, DataProcessingError))
        }
    }
  }





  def updateMyLogin(userId: UUID, newLogin: Login)(implicit connection: Connection): DbResponse[Int] = {
    Using(connection.prepareStatement("UPDATE users SET login = ? WHERE user_id = ? ")) { //    AND login = ?
      (statement: PreparedStatement) =>
        statement.setString(1, newLogin)
        statement.setObject(2, userId)
        statement.executeUpdate()
    } match {
      case Failure(ex) =>
        if (ex.getMessage.contains("duplicate key value violates unique constraint")) {
          Left(QueryError(ERROR, LoginTaken))
        }
        handleExceptionMessage(ex)
      case Success(v) => Right(v)
    }
  }






  def updateUsersPassword(user: User, oldPass: Password, newPass: Password)(implicit connection: Connection): DbResponse[Int] = {
    val sql = "UPDATE users SET pass = ? WHERE user_id = ? AND login = ? AND pass = ?"
    Using(connection.prepareStatement(sql)) {
      (statement: PreparedStatement) =>
        statement.setString(1, newPass)
        statement.setObject(2, user.userId)
        statement.setString(3, user.login)
        statement.setString(4, oldPass)
        statement.executeUpdate()
    } match {
      case Failure(ex) => handleExceptionMessage(ex)
      case Success(v) => Right(v)
    }
  }





  /**
   * szukanie człowieka przy logowaniu
   * przy logowaniu
   *
   * @param uuid
   */
  def findUser(login: Login, pass: Password)(implicit connection: Connection): DbResponse[(User, Settings)] = {
    val sql =
      "SELECT users.user_id, users.login, settings.joining_offset, settings.zone_id " +
        "FROM users " +
        "INNER JOIN settings " +
        "ON settings.user_id = users.user_id " +
        "WHERE users.login = ? AND users.pass = ? "
    Using(connection.prepareStatement(sql)) {
      (statement: PreparedStatement) =>
        statement.setString(1, login)
        statement.setString(2, pass)
        Using(statement.executeQuery()) {
          (resultSet: ResultSet) =>
            if (resultSet.next()) {
              val userId: UserID = resultSet.getObject[UUID]("user_id", classOf[UUID])
              val login: Login = resultSet.getString("login")
              val offset: Long = resultSet.getLong("joining_offset")
              val zoneId: String = resultSet.getString("zone_id")
              Right( (User(userId, login), Settings(joiningOffset = offset, zoneId = ZoneId.of(zoneId))) )
            }
            else
              Left(QueryError(ERROR, IncorrectLoginOrPassword))
        } match {
          case Failure(ex) => throw ex
          case Success(either) => either
        }
    } match {
      case Failure(ex) => handleExceptionMessage(ex)
      case Success(either) => either
    }
  }






  def createSession(sessionId: UUID, userId: UUID, validityTime: Long)(implicit connection: Connection): DbResponse[Int] = {
    val sql = "INSERT INTO sessions (session_id, user_id, validity_time)  VALUES (?, ?, ?)"
    Using(connection.prepareStatement(sql)) {
      (statement: PreparedStatement) =>
        statement.setObject(1, sessionId)
        statement.setObject(2, userId)
        statement.setLong(3, validityTime)
        val affectedRows: Int = statement.executeUpdate()
        Right(affectedRows)
    } match {
      case Failure(ex) => handleExceptionMessage(ex)
      case Success(e) => e
    }
  }





  /**
   *
   * @param userUUID
   * @return
   */
  def findUsersSession(userUUID: UUID)(implicit connection: Connection): DbResponse[List[SessionInfo]] = {
    val sql = "SELECT session_id, validity_time FROM sessions WHERE user_id = ? "
    Using(connection.prepareStatement(sql)) {
      (statement: PreparedStatement) =>
        statement.setObject(1, userUUID)
        Using(statement.executeQuery()) {
          (resultSet: ResultSet) =>
            val buffer: ListBuffer[SessionInfo] = ListBuffer.empty
            while (resultSet.next()) {
              val sessionId: UUID = resultSet.getObject[UUID]("session_id", classOf[UUID])
              val timeValidity: Long = resultSet.getLong("validity_time")
              buffer.append(SessionInfo(sessionId, userUUID, timeValidity))
            }
            Right(buffer.toList)
        } match {
          case Failure(ex) => throw ex
          case Success(either) => either
        }
    } match {
      case Failure(ex) => handleExceptionMessage[List[SessionInfo]](ex)
      case Success(either) => either
    }
  }



  def checkUsersSession(sessionId: UUID, userId: UUID, validityTime: Long)(implicit connection: Connection): DbResponse[Int] = {
    val sql = "SELECT validity_time FROM sessions WHERE session_id = ? AND user_id = ? AND validity_time = ? "
    Using(connection.prepareStatement(sql)) {
      (statement: PreparedStatement) =>
        statement.setObject(1, sessionId)
        statement.setObject(2, userId)
        statement.setLong(3, validityTime)
        Using(statement.executeQuery()) {
          (resultSet: ResultSet) =>
            var n = 0
            while (resultSet.next()) n += 1
            Right(n)
        } match {
          case Failure(ex) => throw ex
          case Success(either) => either
        }
    } match {
      case Failure(ex) => handleExceptionMessage[Int](ex)
      case Success(either) => either
    }
  }




  /**
   *
   *
   */
  def updateSession(sessionId: UUID, userId: UUID, newValidityTime: Long)(implicit connection: Connection): DbResponse[Int] = {
    val sql = "UPDATE sessions SET validity_time = ? WHERE session_id = ? AND user_id = ? "
    Using(connection.prepareStatement(sql)) {
      (statement: PreparedStatement) =>
        statement.setLong(1, newValidityTime)
        statement.setObject(2, sessionId)
        statement.setObject(3, userId)
        statement.executeUpdate()
    } match {
      case Failure(ex) => handleExceptionMessage(ex)
      case Success(v) => Right(v)
    }
  }





  /**
   * @param sessionId
   * @param userId
   * @return
   */
  def removeSession(sessionId: UUID, userId: UUID, validityTime: Long)(implicit connection: Connection): DbResponse[Int] = {
    Using(connection.prepareStatement("DELETE FROM sessions WHERE session_id = ? AND user_id = ? AND validity_time = ? ")) {
      (statement: PreparedStatement) =>
        statement.setObject(1, sessionId)
        statement.setObject(2, userId)
        statement.setLong(3, validityTime)
        statement.executeUpdate()
    } match {
      case Failure(ex) => handleExceptionMessage(ex)
      case Success(v) => Right(v)
    }
  }





  def removeAllExpiredUserSessions(userId: UUID)(implicit connection: Connection): DbResponse[Int] = {
    Using(connection.prepareStatement("DELETE FROM sessions WHERE user_id = ? AND validity_time <= ? ")) {
      (statement: PreparedStatement) =>
        statement.setObject(1, userId)
        statement.setLong(2, System.currentTimeMillis() / 1000L)
        statement.executeUpdate()
    } match {
      case Failure(ex) => handleExceptionMessage(ex)
      case Success(v) => Right(v)
    }
  }





  /**
   * For small (<10) list of users
   * @param logins
   * @param connection
   * @return
   */
  def findUsers(logins: List[Login])(implicit connection: Connection): DbResponse[List[User]] = {
    val prefix = "SELECT user_id, login FROM users WHERE login IN ( "
    val list = logins.foldLeft("")((folded, user) => s"$folded, '$user'").substring(2)
    val postfix = " ) "
    val sql = s"$prefix$list$postfix"
    Using(connection.prepareStatement(sql)) { statement =>
      Using(statement.executeQuery()) {
        (resultSet: ResultSet) =>
          val buffer = ListBuffer.empty[User]
          while (resultSet.next()) {
            val userId: UserID = resultSet.getObject[UUID]("user_id", classOf[UUID])
            val login: Login = resultSet.getString("login")
            buffer.append(User(userId, login))
          }
          Right(buffer.toList)
      } match {
        case Failure(ex) => throw ex
        case Success(either) => either
      }
    } match {
      case Failure(ex) => handleExceptionMessage(ex)
      case Success(either) => either
    }
  }



  def findMyChats(userUUID: UUID)(implicit connection: Connection): DbResponse[Map[Chat, Map[Partition, Offset]]] = {
    val numOfPartitions = configurator.CHAT_TOPIC_PARTITIONS_NUMBER
    val range = 0 until numOfPartitions
    val offsetColumn = "users_chats.users_offset_"
    val prefix = "SELECT chats.chat_id, users_chats.chat_name, chats.group_chat, users_chats.message_time, users_chats.silent" +
      "users.user_id, users.login, "
    val fold = range.foldLeft("")((folded, partition) => s"$folded$offsetColumn$partition, ").stripTrailing()
    val offsets = fold.substring(0, fold.length - 1) // we remove last coma ,
    val postfix = " FROM users_chats " +
      "INNER JOIN chats " +
      "ON users_chats.chat_id = chats.chat_id " +
      "INNER JOIN users_chats AS other_chats " +
      "ON chats.chat_id = other_chats.chat_id " +
      "INNER JOIN users " +
      "ON other_chats.user_id = users.user_id " +
      "WHERE users_chats.user_id = ?"
    val sql = s"$prefix$offsets$postfix"
    Using(connection.prepareStatement(sql)) {
      (statement: PreparedStatement) =>
        statement.setObject(1, userUUID)
        val buffer: ListBuffer[(Chat, Map[Partition, Offset])] = ListBuffer()
        Using(statement.executeQuery()) {
          (resultSet: ResultSet) =>
            while (resultSet.next()) {
              val chatId: ChatId = resultSet.getString("chat_id")
              val chatName: ChatName = resultSet.getString("chat_name")
              val groupChat: Boolean = resultSet.getBoolean("group_chat")
              val time: Long = resultSet.getLong("message_time")
              val silent: Boolean = resultSet.getBoolean("silent")
              val userId: UUID = resultSet.getObject[UUID]("user_id", classOf[UUID])
              val login: Login = resultSet.getString("login")
              val partitionOffsets: Map[Partition, Offset] =
                range.map(i => (i, resultSet.getLong(s"users_offset_$i"))).toMap
              val chat: Chat = Chat(chatId, chatName, groupChat, time, silent)
              val u: User = User(userId, login)
              // we add only when user_id is our id
              if (userUUID == u.userId)
                buffer += ((chat, partitionOffsets))
              // val grouped = buffer.toList.groupMap[Chat, User]((chat, user) => chat)((chat, user) => user)
            }
            val grouped = buffer.toList.toMap
            Right(grouped)
        } match {
          case Failure(ex) => throw ex
          case Success(either) => either
        }
    } match {
      case Failure(ex) => handleExceptionMessage[Map[Chat, Map[Int, Long]]](ex)
      case Success(either) => either
    }
  }





  def findChatUsers(chatId: ChatId)(implicit connection: Connection): DbResponse[List[User]] = {
    // val sql = "SELECT users.user_id, users.login FROM users INNER JOIN users_chats ON users.user_id = users_chats.user_id WHERE users_chats.chat_id = ? "
    val sql = "SELECT users.user_id, users.login FROM users_chats " + // users_chats.users_offset,
      "INNER JOIN users " +
      "ON users_chats.user_id = users.user_id " +
      "WHERE users_chats.chat_id = ?"
    Using(connection.prepareStatement(sql)) {
      (statement: PreparedStatement) =>
        statement.setString(1, chatId)
        Using(statement.executeQuery()) {
          (resultSet: ResultSet) => {
            val buffer = ListBuffer.empty[User]
            while (resultSet.next()) {
              val userId: UserID = resultSet.getObject[UUID]("user_id", classOf[UUID])
              val login: Login = resultSet.getString("login")
              buffer.append(User(userId, login))
            }
            Right(buffer.toList)
          }
        } match {
          case Failure(ex) => throw ex
          case Success(either) => either
        }
    } match {
      case Failure(ex) => handleExceptionMessage(ex)
      case Success(either) => either
    }
  }





  /*
  Chat Creation
   */



  private def checkDuplicatedChat(myID: UUID, otherId: UUID)(implicit connection: Connection): DbResponse[Int] = {
    val chatId1 = Domain.generateChatId(myID, otherId)
    val chatId2 = Domain.generateChatId(otherId, myID)
    val prefix = "SELECT silent FROM users_chats WHERE chat_id IN ( "
    val chatIds = s"'$chatId1', '$chatId2'"
    val postfix = " ) "
    val sql = s"$prefix$chatIds$postfix"
    Using(connection.prepareStatement(sql)) { preparedStatement =>
      Using(preparedStatement.executeQuery()) {
        (resultSet: ResultSet) =>
          var n = 0
          while (resultSet.next()) n += 1
          n
      } match {
        case Failure(ex) => throw ex
        case Success(v)  => v
      }
    } match {
      case Failure(ex) => handleExceptionMessage(ex)
      case Success(v)  => Right(v)
    }
  }





  def createSingleChat(me: User, otherId: UUID, chatId: ChatId, chatName: ChatName )(implicit connection: Connection, ec: ExecutionContext): DbResponse[Int] = {
    checkDuplicatedChat(me.userId, otherId) match {
      case Left(error) => Left(error)
      case Right(value) =>
        if ( value > 0 ) Left(QueryError(ERROR, UnsupportedOperation))
        else {
          connection.setAutoCommit(false)
          val beforeAnyInsertions: Savepoint = connection.setSavepoint()

          val f1 = Future {
            Using(connection.prepareStatement("INSERT INTO chats(chat_id, group_chat) VALUES (?,?)")) {
              (statement: PreparedStatement) =>
                statement.setString(1, chatId)
                statement.setBoolean(2, false)
                statement.executeUpdate()
            } match {
              case Failure(ex) => throw ex
              case Success(v) => v
            }
          }(ec)

          val ct = System.currentTimeMillis()

          val f2 = Future {
            Using(connection.prepareStatement("INSERT INTO users_chats (chat_id, chat_name, user_id, message_time) VALUES (?, ?, ?, ?)")) {
              (statement: PreparedStatement) =>
                statement.setString(1, chatId)
                statement.setString(2, chatName)
                statement.setObject(3, me.userId)
                statement.setLong(4, ct)
                statement.executeUpdate()
            } match {
              case Failure(exception) => throw exception
              case Success(value) => value
            }
          }(ec)

          val f3 = Future {
            Using(connection.prepareStatement("INSERT INTO users_chats (chat_id, chat_name, user_id, message_time) VALUES (?, ?, ?, ?)")) {
              (statement: PreparedStatement) =>
                statement.setString(1, chatId)
                statement.setString(2, s"${me.login}")
                statement.setObject(3, otherId)
                statement.setLong(4, ct)
                statement.executeUpdate()
            } match {
              case Failure(exception) => throw exception
              case Success(value) => value
            }
          }(ec)

          val zipped = List(f1, f2, f3).reduceLeft((l, r) => l.zipWith(r)(_ + _))
          val a = Await.result(zipped, Duration.create(5L, SECONDS))
          if (a == 3) {
            connection.commit()
            connection.setAutoCommit(true)
            Right(3)
          } else {
            connection.rollback( beforeAnyInsertions )
            connection.setAutoCommit(true)
            Left(QueryError(ERROR, DataProcessingError))
          }
        }
    }
  }





  def createGroupChat(users: List[User], chatName: ChatName, chatId: ChatId)(implicit connection: Connection, ex: ExecutionContext): DbResponse[Chat] = {
    val listSize = users.length
    if (listSize < 2)
      Left(QueryError(ERROR, AtLeastTwoUsers))
    else {
      connection.setAutoCommit(false)
      val beforeAnyInsertions: Savepoint = connection.setSavepoint()
      val time = System.currentTimeMillis()
      val chat = Chat(chatId, chatName, groupChat = true, time, silent = false)

      Using(connection.prepareStatement("INSERT INTO chats(chat_id, group_chat) VALUES (?,?)")) {
        (statement: PreparedStatement) =>
          statement.setString(1, chat.chatId)
          statement.setBoolean(2, chat.groupChat)
          statement.executeUpdate()
      } match {
        case Failure(ex) =>
          connection.rollback(beforeAnyInsertions)
          connection.setAutoCommit(true)
          handleExceptionMessage(ex)
        case Success(v) =>
          if (v == 1) {
            val affectionList: List[Future[Int]] = users.map(
              user =>
                Future {
                  Using(connection.prepareStatement("INSERT INTO users_chats (chat_id, user_id, chat_name, message_time) VALUES (?, ?, ?, ?)")) {
                    (statement: PreparedStatement) =>
                      statement.setString(1, chat.chatId)
                      statement.setObject(2, user.userId)
                      statement.setString(3, chatName)
                      statement.setLong(  4, chat.lastMessageTime)
                      statement.executeUpdate()
                  } match {
                    case Failure(exception) => throw exception
                    case Success(value) => value
                  }
                }(ex)
            )
            val zippedFuture = affectionList.reduceLeft((f1, f2) => f1.zipWith(f2)(_ + _)) // we zip all futures when they end and add affected rows.
            val totalAffectedRows: Int = Await.result(zippedFuture, Duration.create(5L, duration.SECONDS))
            if (totalAffectedRows == users.length) {
              connection.commit()
              connection.setAutoCommit(true)
              Right(chat)
            }
            else {
              connection.rollback(beforeAnyInsertions)
              connection.setAutoCommit(true)
              Left(QueryError(ERROR, DataProcessingError))
            }
          } else {
            connection.rollback(beforeAnyInsertions)
            connection.setAutoCommit(true)
            Left(QueryError(ERROR, DataProcessingError))
          }
      }
    }
  }





  def addNewUsersToChat(users: List[UUID], chatId: String, chatName: ChatName)(implicit connection: Connection, ec: ExecutionContext): DbResponse[Int] = {
    if (users.isEmpty) Left(QueryError(ERROR, NoUserSelected))
    else {
      var stateBeforeInsertion: Savepoint = null
      Try {
        connection.setAutoCommit(false)
        stateBeforeInsertion = connection.setSavepoint()
        val futureList = users.map(
          userId =>
            Future { // each insertion executed in separate thread
              Using(connection.prepareStatement("INSERT INTO users_chats (chat_id, user_id, chat_name) VALUES (?, ?, ?)")) {
                (statement: PreparedStatement) =>
                  statement.setString(1, chatId)
                  statement.setObject(2, userId)
                  statement.setString(3, chatName)
                  statement.executeUpdate()
              } match {
                case Failure(_)     => 0
                case Success(value) => value
              }
            }(ec)
        )
        val zippedFuture = futureList.reduceLeft((f1, f2) => f1.zipWith(f2)(_ + _))
        val affected = Await.result(zippedFuture, Duration.create(5L, duration.SECONDS))
        if (affected == users.length) {
          connection.commit()
          connection.setAutoCommit(true)
          Right(affected)
        } else {
          connection.rollback(stateBeforeInsertion)
          connection.setAutoCommit(true)
          Left(QueryError(ERROR, DataProcessingError))
        }
      } match {
        case Failure(ex) =>
          connection.rollback(stateBeforeInsertion) // This connection has been closed
          connection.setAutoCommit(true)
          handleExceptionMessage[Int](ex) // returns DataProcessing Error
        case Success(either) => either
      }
    }
  }





  private def numOfChatUsers(chatId: ChatId)(implicit connection: Connection): DbResponse[Int] = {
    Using(connection.prepareStatement("SELECT silent FROM users_chats WHERE chat_id = ? ")) {
      (statement: PreparedStatement) =>
        statement.setString(1, chatId)
        Using(statement.executeQuery()) {
          (resultSet: ResultSet) =>
            var n = 0
            while (resultSet.next()) n += 1
            n
        } match {
          case Failure(ex) => throw ex
          case Success(value) => Right(value)
        }
    } match {
      case Failure(ex) => handleExceptionMessage[Int](ex)
      case Success(either) => either
    }
  }





  /**
   * Method return number of other chat users
   */
  def leaveTheChat(userId: UUID, chatId: ChatId, groupChat: Boolean)(implicit connection: Connection): DbResponse[Int] = {
    if (groupChat) {
      numOfChatUsers(chatId) match {
        case Left(queryError: QueryError) =>
          Left(queryError)
        case Right(chatUsers) =>
          if (chatUsers < 0)
            Left(QueryError(ERROR, DataProcessingError))
          else if (chatUsers == 0)
            Right(chatUsers)
          else { // we process group chat
            val sql2 = "DELETE FROM users_chats INNER JOIN chats " +
              "ON users_chats.chat_id = chats.chat_id " +
              "WHERE  chats.group_chat = ? AND users_chats.chat_id = ? AND users_chats.user_id = ?  "

            val sql = "DELETE FROM users_chats WHERE chat_id = ? AND user_id = ? "
            Using(connection.prepareStatement( sql )) {
              (statement: PreparedStatement) =>
                statement.setString(1, chatId)
                statement.setObject(2, userId)
                statement.executeUpdate()
            } match {
              case Failure(ex) => handleExceptionMessage(ex)
              case Success(value) =>
                if (value == 1) Right(chatUsers - value)
                else Left(QueryError(ERROR, DataProcessingError))
            }
          }
      }
    } else Left(QueryError(ERROR, UnsupportedOperation))
  }






  /*
  technical chat editions
   */



  def updateJoiningOffset(user: User, offset: Offset)(implicit connection: Connection): DbResponse[Int] = {
    val sql = "UPDATE settings SET joining_offset = ? WHERE user_id = ? "
    Using(connection.prepareStatement(sql)) {
      (statement: PreparedStatement) =>
        statement.setLong(1, offset)
        statement.setObject(2, user.userId)
        statement.executeUpdate()
    } match {
      case Failure(ex) => handleExceptionMessage(ex)
      case Success(value) => Right(value)
    }
  }



  def updateChatOffsetAndMessageTime(userId: UUID, chat: Chat, sortedOffsets: => Seq[(Int, Long)])(implicit connection: Connection): DbResponse[Int] = {
    val prefix = "UPDATE users_chats SET "
    val offsets = sortedOffsets.foldLeft[String]("")(
      (folded: String, partitionAndOffset: (Int, Long)) =>
        s"${folded}users_offset_${partitionAndOffset._1} = ?, "
    ).stripTrailing()
    val postfix = " message_time = ? WHERE chat_id = ? AND user_id = ? "

    val sql = s"$prefix$offsets$postfix"
    Using(connection.prepareStatement(sql)) {
      (statement: PreparedStatement) =>
        val numOfPartitions = sortedOffsets.size
        sortedOffsets.foreach(
          (partitionAndOffset: (Int, Long)) => {
            val (partitionNum, offset): (Int, Long) = partitionAndOffset
            statement.setLong(partitionNum + 1, offset)
          }
        )
        statement.setLong(  numOfPartitions + 1, chat.lastMessageTime)
        statement.setString(numOfPartitions + 2, chat.chatId)
        statement.setObject(numOfPartitions + 3, userId)
        statement.executeUpdate()
    } match {
      case Failure(ex) => handleExceptionMessage[Int](ex) // returns DataProcessing Error
      case Success(value) => Right(value)
    }

  }



  def updateChatName(myUUID: UUID, chatId: String, newName: ChatName)(implicit connection: Connection): DbResponse[Int] = {
    val sql = "UPDATE users_chats SET chat_name = ? WHERE chat_id = ? AND user_id = ? "
    Using(connection.prepareStatement(sql)) {
      (statement: PreparedStatement) =>
        statement.setString(1, newName)
        statement.setString(2, chatId)
        statement.setObject(3, myUUID)
        statement.executeUpdate()
    } match {
      case Failure(ex) => handleExceptionMessage(ex)
      case Success(value) => Right(value)
    }
  }



  def updateChatSilence(chatId: ChatId, userId: UUID, silent: Boolean)(implicit connection: Connection) :DbResponse[Int] = {
    val sql = "UPDATE users_chats SET silent = ? WHERE chat_id = ? AND user_id = ? "
    Using(connection.prepareStatement(sql)) {
      (statement: PreparedStatement) =>
        statement.setBoolean(1, silent)
        statement.setString( 2, chatId)
        statement.setObject( 3, userId)
        statement.executeUpdate()
    } match {
      case Failure(ex) => handleExceptionMessage(ex)
      case Success(value) => Right(value)
    }
  }














  private def handleExceptionMessage[A](ex: Throwable): DbResponse[A] = {
    if (ex.getMessage == "FATAL: terminating connection due to administrator command"
      || ex.getMessage == "This connection has been closed."
      || ex.getMessage == "An I/O error occurred while sending to the backend.") {
      Left(QueryError(ERROR, NoDbConnection))
    }
    else if (ex.getMessage.toLowerCase.contains("timeout")) {
      Left(QueryError(ERROR, TimeOutDBError))
    }
    else if (ex.getMessage == "Incorrect login or password") {
      Left(QueryError(ERROR, IncorrectLoginOrPassword))
    }
    else if (ex.getMessage.contains("duplicate key value violates unique constraint")
      || ex.getMessage == "Data processing error."
      || ex.getMessage.contains("violates foreign key constraint")) {
      Left(QueryError(ERROR, DataProcessingError))
    }
    else
      Left(QueryError(ERROR, UndefinedError(ex.getMessage)))
  }

}


object DbExecutor {

  def apply(configurator: KafkaConfigurator): DbExecutor = new DbExecutor(configurator)

}

