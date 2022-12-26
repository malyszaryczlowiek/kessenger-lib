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
                   zoneId: ZoneId, chatId: ChatId, chatName: ChatName, groupChat: Boolean)

object Message {

  given encoder: Encoder[Message] = (a: Message) => Json.obj(
    ( "content",      Json.fromString ( a.content           )),
    ( "authorId",     Json.fromString ( a.authorId.toString )),
    ( "authorLogin",  Json.fromString ( a.authorLogin       )),
    ( "sendingTime",  Json.fromLong   ( a.sendingTime       )),
    ( "serverTime",   Json.fromLong   ( a.serverTime        )),
    ( "zoneId",       Json.fromString ( a.zoneId.toString   )),
    ( "chatId",       Json.fromString ( a.chatId            )),
    ( "chatName",     Json.fromString ( a.chatName          )),
    ( "groupChat",    Json.fromBoolean( a.groupChat         ))
  )

  given websocketMessageEncoder: Encoder[Message] = (a: Message) =>
    Json.obj( ("msg", a.asJson(encoder)) )

  given websocketMessageWithOffsetEncoder: Encoder[(Message, Int, Long)] = (a: (Message, Int, Long)) =>
    Json.obj(
      ("msg", a._1.asJson(encoder)),
      ("p",   Json.fromInt(a._2) ),
      ("o",   Json.fromLong(a._3) )
    )

  given decoder: Decoder[Message] = (c: HCursor) =>
    for {
      content      <- c.downField("content")    .as[String]
      authorId     <- c.downField("authorId")   .as[String]
      authorLogin  <- c.downField("authorLogin").as[String]
      sendingTime  <- c.downField("sendingTime").as[Long]
      serverTime   <- c.downField("serverTime") .as[Long]
      zoneId       <- c.downField("zoneId")     .as[String]
      chatId       <- c.downField("chatId")     .as[String]
      chatName     <- c.downField("chatName")   .as[String]
      groupChat    <- c.downField("groupChat")  .as[Boolean]
    } yield {
      Message(
        content,
        UUID.fromString( authorId ),
        authorLogin,
        sendingTime,
        serverTime,
        ZoneId.of( zoneId ),
        chatId,
        chatName,
        groupChat
      )
    }

  def toWebsocketJSON(m: Message): String = m.asJson(websocketMessageEncoder).noSpaces
  def toWebsocketJSON(m: (Message, Int, Long)): String = m.asJson.noSpaces
  def toKafkaJSON(m: Message): String = m.asJson(encoder).noSpaces
  def parseMessage(json: String): Either[Error, Message] = decode[Message](json)

  def nullMessage: Message =
    Message("", UUID.fromString("a092dbb2-2a69-4876-bbe4-8453aa5b6979"),"NULL_LOGIN", 0L,0L, ZoneOffset.UTC, "", "", false)

}

