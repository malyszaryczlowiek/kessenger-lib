package io.github.malyszaryczlowiek
package kessengerlibrary.model

import kessengerlibrary.domain.Domain.{ChatId, ChatName, Login, UserID}


import io.circe.Decoder.Result
import io.circe.parser.decode
import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder, Error, HCursor, Json}

import java.time.{ZoneId, ZoneOffset, ZonedDateTime}
import java.util.UUID


case class Message(content: String, authorId: UserID, authorLogin: Login, sendingTime: Long, serverTime: Long,
                   zoneId: ZoneId, chatId: ChatId, chatName: ChatName, groupChat: Boolean,
                   partOff: Option[PartitionOffset])

object Message {

  given kafkaEncoder: Encoder[Message] = (a: Message) =>
    a.partOff match {
      case Some(po) =>
        Json.obj(
          ("content", Json.fromString(a.content)),
          ("authorId", Json.fromString(a.authorId.toString)),
          ("authorLogin", Json.fromString(a.authorLogin)),
          ("sendingTime", Json.fromLong(a.sendingTime)),
          ("serverTime", Json.fromLong(a.serverTime)),
          ("zoneId", Json.fromString(a.zoneId.toString)),
          ("chatId", Json.fromString(a.chatId)),
          ("chatName", Json.fromString(a.chatName)),
          ("groupChat", Json.fromBoolean(a.groupChat)),
          ("partOff", po.asJson)
        )
      case None =>
        Json.obj(
          ("content", Json.fromString(a.content)),
          ("authorId", Json.fromString(a.authorId.toString)),
          ("authorLogin", Json.fromString(a.authorLogin)),
          ("sendingTime", Json.fromLong(a.sendingTime)),
          ("serverTime", Json.fromLong(a.serverTime)),
          ("zoneId", Json.fromString(a.zoneId.toString)),
          ("chatId", Json.fromString(a.chatId)),
          ("chatName", Json.fromString(a.chatName)),
          ("groupChat", Json.fromBoolean(a.groupChat)),
          ("partOff", PartitionOffset(-1, -1L).asJson)
        )
  }

  given websocketNewMessageListEncoder : Encoder[List[Message]] =
    (a: List[Message]) => Json.obj( ("newMsgList", a.map(_.asJson(kafkaEncoder)).asJson))


  given websocketOldFetchedMessageListEncoder: Encoder[List[Message]] =
    (a: List[Message]) => Json.obj(("oldMsgList", a.map(_.asJson(kafkaEncoder)).asJson))


  given decoder: Decoder[Message] = (c: HCursor) =>
    for {
      content <- c.downField("content").as[String]
      authorId <- c.downField("authorId").as[String]
      authorLogin <- c.downField("authorLogin").as[String]
      sendingTime <- c.downField("sendingTime").as[Long]
      serverTime <- c.downField("serverTime").as[Long]
      zoneId <- c.downField("zoneId").as[String]
      chatId <- c.downField("chatId").as[String]
      chatName <- c.downField("chatName").as[String]
      groupChat <- c.downField("groupChat").as[Boolean]
      partOff <- c.downField("partOff").as[PartitionOffset]
    } yield {
      if (partOff.partition < 0)
        Message(
          content,
          UUID.fromString(authorId),
          authorLogin,
          sendingTime,
          serverTime,
          ZoneId.of(zoneId),
          chatId,
          chatName,
          groupChat,
          None
        )
      else
        Message(
          content,
          UUID.fromString(authorId),
          authorLogin,
          sendingTime,
          serverTime,
          ZoneId.of(zoneId),
          chatId,
          chatName,
          groupChat,
          Some(partOff)
        )
    }

  def toNewMessagesWebsocketJSON(m: List[Message]): String = m.asJson(websocketNewMessageListEncoder).noSpaces

  def toOldMessagesWebsocketJSON(m: List[Message]): String = m.asJson(websocketOldFetchedMessageListEncoder).noSpaces

  // def toWebsocketJSON(m: (Message, Int, Long)): String = m.asJson.noSpaces

  def toKafkaJSON(m: Message): String = m.asJson(kafkaEncoder).noSpaces

  def parseMessage(json: String): Either[Error, Message] = decode[Message](json)
  def nullMessage: Message =
    Message("", UUID.fromString("a092dbb2-2a69-4876-bbe4-8453aa5b6979"),"NULL_LOGIN", 0L,0L,
      ZoneOffset.UTC, "", "", false, None)

}

