package io.github.malyszaryczlowiek
package kessengerlibrary.db.queries

import kessengerlibrary.domain.User
import kessengerlibrary.domain.Domain.{ChatName, Login}

enum QueryErrorMessage(message: String):
  override def toString: String = message

  case  UserNotFound(login: Login)       extends QueryErrorMessage(s"'$login' Not Found.")
  case  NoDbConnection                   extends QueryErrorMessage(s"Connection to DB lost. Try again later.")
  case  DataProcessingError              extends QueryErrorMessage(s"Data Processing Error.")
  case  UndefinedError(e: String = "")   extends QueryErrorMessage(s"Undefined Error. $e")
  case  LoginTaken                       extends QueryErrorMessage(s"Sorry Login is taken, try with another one.")
  case  AtLeastTwoUsers                  extends QueryErrorMessage(s"To create new chat, you have to select two users at least.")
  case  NoUserSelected                   extends QueryErrorMessage(s"No User selected.")
  case  TimeOutDBError                   extends QueryErrorMessage(s"Timeout Error.")
  case  UserHasNoChats                   extends QueryErrorMessage(s"User has no chats.")
  case  UnsupportedOperation             extends QueryErrorMessage(s"Unsupported Operation.")
  case  IncorrectLoginOrPassword         extends QueryErrorMessage(s"Incorrect Login or Password.")
  case  IncorrectPassword                extends QueryErrorMessage(s"Incorrect Password.")
  case  ChatDoesNotExist(name: ChatName) extends QueryErrorMessage(s"Chat '$name' does not exist.")

//case SomeUsersNotAddedToChat            extends QueryErrorMessage("Some users not added to chat.")
//case CannotAddUserToChat(login: Login)  extends QueryErrorMessage(s"Cannot add user $login to chat.")
//case TryingToAddNonExistingUser         extends QueryErrorMessage(s"You trying to add non existing user.")
// case UserIsAMemberOfChat(login: Login)  extends QueryErrorMessage(s"User $login is a member of chat currently.")
// case NotAllUsersRemovedFromChat         extends QueryErrorMessage("Not all selected Users removed from chat.")