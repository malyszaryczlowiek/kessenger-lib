package io.github.malyszaryczlowiek
package kessengerlibrary.db.queries

import kessengerlibrary.domain.Domain.{ChatName, Login}


sealed class QueryErrorMessage(mes: String) {
  override def toString: ChatName = mes
}
case class  UserNotFound(login: Login)         extends QueryErrorMessage(s"$login Not Found.")
case object NoDbConnection                     extends QueryErrorMessage("Connection to DB lost. Try again later.")
case object DataProcessingError                extends QueryErrorMessage("Data Processing Error.")
case class  UndefinedError(e: String = "")     extends QueryErrorMessage(s"Undefined Error. $e")
case object LoginTaken                         extends QueryErrorMessage("Sorry Login is taken, try with another one.")
case object AtLeastTwoUsers                    extends QueryErrorMessage("To create new chat, you have to select two users at least.")
case object NoUserSelected                     extends QueryErrorMessage("No User selected.")
case object TimeOutDBError                     extends QueryErrorMessage(s"Timeout Error.")
case object UserHasNoChats                     extends QueryErrorMessage("User has no chats.")
case object UnsupportedOperation               extends QueryErrorMessage("Unsupported Operation.")
case object IncorrectLoginOrPassword           extends QueryErrorMessage("Incorrect Login or Password.")
case object IncorrectPassword                  extends QueryErrorMessage("Incorrect Password.")
case class  ChatDoesNotExist(name: ChatName)   extends QueryErrorMessage(s"Chat $name does not exist.")



//case SomeUsersNotAddedToChat            extends QueryErrorMessage("Some users not added to chat.")
//case CannotAddUserToChat(login: Login)  extends QueryErrorMessage(s"Cannot add user $login to chat.")
//case TryingToAddNonExistingUser         extends QueryErrorMessage(s"You trying to add non existing user.")
// case UserIsAMemberOfChat(login: Login)  extends QueryErrorMessage(s"User $login is a member of chat currently.")
// case NotAllUsersRemovedFromChat         extends QueryErrorMessage("Not all selected Users removed from chat.")