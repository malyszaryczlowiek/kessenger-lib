package io.github.malyszaryczlowiek
package kessengerlibrary.domain

import kessengerlibrary.domain.Domain.{Login, UserID}

import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, HCursor, Json, Error}
import io.circe.parser.decode
import io.circe.syntax._

import java.util.UUID

case class User(userId: UserID, login: Login)

object User {

  implicit object encoder extends Encoder[User] {
    override def apply(a: User): Json = {
      Json.obj(
        ("user_id", Json.fromString(a.userId.toString)),
        ("login",   Json.fromString(a.login))
      )
    }
  }


  implicit object decoder extends Decoder[User] {
    override def apply(c: HCursor): Result[User] = {
      for {
        userId <- c.downField("user_id").as[String]
        login  <- c.downField("login").as[String]
      } yield {
        User(UUID.fromString(userId), login)
      }
    }
  }


  def parseJSONtoUser(json: String): Either[Error, User] = decode[User](json)

  def parseUserToJSON(user: User): String = user.asJson.noSpaces

  def parseJSONtoListOfUsers(json: String): Either[Error, List[User]] = decode[List[User]](json)

  def parseListOfUsersToJSON(users: List[User]): String = users.asJson.noSpaces


  /**
   * nullUser has no login and is used to signify
   * deserialization error, such user must be ignored
   * in any processing
   *
   * @return
   */
  def nullUser: User = User(UUID.fromString("a092dbb2-2a69-4876-bbe4-8453aa5b6979"), "")

}
