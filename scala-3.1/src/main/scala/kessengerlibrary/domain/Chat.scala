package io.github.malyszaryczlowiek
package kessengerlibrary.domain

import kessengerlibrary.domain.Domain.{ChatId, ChatName}

import java.time.LocalDateTime


/**
 *
 */
case class Chat(chatId: ChatId, chatName: ChatName, groupChat: Boolean, timeOfLastMessage: LocalDateTime)

object Chat:

  given reverseChatOrdering: Ordering[Chat] with
    override def compare(x: Chat, y: Chat): Int =
      if x.timeOfLastMessage.isBefore(y.timeOfLastMessage) then 1
      else if x.timeOfLastMessage.isAfter(y.timeOfLastMessage) then -1
      else 0

  given orderChatWithDate: Ordering[Chat] with
    override def compare(x: Chat, y: Chat): Int =
      val timeX = x.timeOfLastMessage
      val timeY = y.timeOfLastMessage
      if timeX.isBefore(timeY) then -1
      else if timeX.isAfter(timeY) then 1
      else 0    