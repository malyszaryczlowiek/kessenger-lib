package io.github.malyszaryczlowiek
package kessengerlibrary.serdes

import kessengerlibrary.domain.Chat
import kessengerlibrary.domain.Chat.reverseChatOrdering

import java.time.LocalDateTime
import java.time.temporal.{ChronoUnit, TemporalUnit}
import java.util.UUID

class ChatTests extends  munit.FunSuite {

  test("Testing chat ordering") {

    val chatId = "Id"
    val chatName = "name"
    val groupChat = false
    val earlierTime = LocalDateTime.now()
    val laterTime = earlierTime.plus(2L, ChronoUnit.MINUTES)


    val earlier = Chat(chatId, chatName, groupChat, earlierTime)
    val later   = Chat(chatId, chatName, groupChat, laterTime)

    val list = List(earlier, later)
    val sorted = list.sorted

    assert(sorted == list.reverse, s"is not reversed")


  }

}
