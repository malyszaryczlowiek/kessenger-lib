package io.github.malyszaryczlowiek
package kessengerlibrary.domain

import kessengerlibrary.domain.Domain.{ChatId, ChatName}

import java.time.LocalDateTime


/**
 *
 */
case class Chat(chatId: ChatId, chatName: ChatName, groupChat: Boolean, timeOfLastMessage: LocalDateTime)

object Chat {

  implicit object ReverseChatOrdering extends Ordering[Chat]  {
    override def compare(x: Chat, y: Chat): Int =
      if ( x.timeOfLastMessage.isBefore(y.timeOfLastMessage) ) 1
      else if ( x.timeOfLastMessage.isAfter(y.timeOfLastMessage) ) -1
      else 0
  }


}

