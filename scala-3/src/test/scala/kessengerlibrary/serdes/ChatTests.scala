package io.github.malyszaryczlowiek
package kessengerlibrary.serdes

import io.github.malyszaryczlowiek.kessengerlibrary.model.Chat.reverseChatOrdering
import io.github.malyszaryczlowiek.kessengerlibrary.model.Chat

import java.time.LocalDateTime
import java.time.temporal.{ChronoUnit, TemporalUnit}
import java.util.UUID

class ChatTests extends  munit.FunSuite {

  test("Testing chat ordering") {

    val chatId = "Id"
    val chatName = "name"
    val groupChat = false
    val earlierTime = System.currentTimeMillis()
    val laterTime =  System.currentTimeMillis() + 2L// earlierTime.plus(2L, ChronoUnit.MINUTES)


    val earlier = Chat(chatId, chatName, groupChat, earlierTime, false)
    val later   = Chat(chatId, chatName, groupChat, laterTime, false)

    val list = List(earlier, later)
    val sorted = list.sorted

    assert(sorted == list.reverse, s"is not reversed")


  }

}
