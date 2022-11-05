package io.github.malyszaryczlowiek
package kessengerlibrary.db.queries

import kessengerlibrary.domain.Domain.{ChatName, Login}
import kessengerlibrary.domain.ErrorCode


sealed class QueryErrorMessage(mes: String) {
  override def toString: ChatName = mes
}
case class UserNotFound(codeNum: ErrorCode, login: Login)       extends QueryErrorMessage(s"Error ${codeNum.num}. '$login' Not Found.")
case class NoDbConnection(codeNum: ErrorCode)                   extends QueryErrorMessage(s"Error ${codeNum.num}. Connection to DB lost. Try again later.")
case class DataProcessingError(codeNum: ErrorCode)              extends QueryErrorMessage(s"Error ${codeNum.num}. Data Processing Error.")
case class UndefinedError(codeNum: ErrorCode, e: String = "")   extends QueryErrorMessage(s"Error ${codeNum.num}. Undefined Error. $e")
case class LoginTaken(codeNum: ErrorCode)                       extends QueryErrorMessage(s"Error ${codeNum.num}. Sorry Login is taken, try with another one.")
case class AtLeastTwoUsers(codeNum: ErrorCode)                  extends QueryErrorMessage(s"Error ${codeNum.num}. To create new chat, you have to select two users at least.")
case class NoUserSelected(codeNum: ErrorCode)                   extends QueryErrorMessage(s"Error ${codeNum.num}. No User selected.")
case class TimeOutDBError(codeNum: ErrorCode)                   extends QueryErrorMessage(s"Error ${codeNum.num}. Timeout Error.")
case class UserHasNoChats(codeNum: ErrorCode)                   extends QueryErrorMessage(s"Error ${codeNum.num}. User has no chats.")
case class UnsupportedOperation(codeNum: ErrorCode)             extends QueryErrorMessage(s"Error ${codeNum.num}. Unsupported Operation.")
case class IncorrectLoginOrPassword(codeNum: ErrorCode)         extends QueryErrorMessage(s"Error ${codeNum.num}. Incorrect Login or Password.")
case class IncorrectPassword(codeNum: ErrorCode)                extends QueryErrorMessage(s"Error ${codeNum.num}. Incorrect Password.")
case class ChatDoesNotExist(codeNum: ErrorCode, name: ChatName) extends QueryErrorMessage(s"Error ${codeNum.num}. Chat '$name' does not exist.")



//case SomeUsersNotAddedToChat            extends QueryErrorMessage("Some users not added to chat.")
//case CannotAddUserToChat(login: Login)  extends QueryErrorMessage(s"Cannot add user $login to chat.")
//case TryingToAddNonExistingUser         extends QueryErrorMessage(s"You trying to add non existing user.")
// case UserIsAMemberOfChat(login: Login)  extends QueryErrorMessage(s"User $login is a member of chat currently.")
// case NotAllUsersRemovedFromChat         extends QueryErrorMessage("Not all selected Users removed from chat.")