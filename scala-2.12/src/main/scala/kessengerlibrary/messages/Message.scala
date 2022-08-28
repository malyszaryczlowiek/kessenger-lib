package io.github.malyszaryczlowiek
package kessengerlibrary.messages

import kessengerlibrary.domain.Domain.{ChatId, ChatName, Login, UserID}

import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, HCursor, Json}

import java.time.{ZoneId, ZoneOffset}
import java.util.UUID


case class Message(content: String, authorId: UserID, authorLogin: Login, utcTime: Long, zoneId: ZoneId, chatId: ChatId, chatName: ChatName, groupChat: Boolean)

object Message {

  implicit object encoder extends Encoder[Message] {
    override def apply(a: Message): Json =
      Json.obj(
        ( "content",      Json.fromString ( a.content           )),
        ( "authorId",     Json.fromString ( a.authorId.toString )),
        ( "authorLogin",  Json.fromString ( a.authorLogin       )),
        ( "utcTime",      Json.fromLong   ( a.utcTime           )),
        ( "zoneId",       Json.fromString ( a.zoneId.toString   )),
        ( "chatId",       Json.fromString ( a.chatId            )),
        ( "chatName",     Json.fromString ( a.chatName          )),
        ( "groupChat",    Json.fromBoolean( a.groupChat         ))
      )
  }


  implicit object decoder extends  Decoder[Message]  {
    override def apply(c: HCursor): Result[Message] = {
      for {
        content      <- c.downField("content")    .as[String]
        authorId     <- c.downField("authorId")   .as[String]
        authorLogin  <- c.downField("authorLogin").as[String]
        utcTime      <- c.downField("utcTime")    .as[Long]
        zoneId       <- c.downField("zoneId")     .as[String]
        chatId       <- c.downField("chatId")     .as[String]
        chatName     <- c.downField("chatName")   .as[String]
        groupChat    <- c.downField("groupChat")  .as[Boolean]
      } yield {
        Message(
          content,
          UUID.fromString( authorId ),
          authorLogin,
          utcTime,
          ZoneId.of( zoneId ),
          chatId,
          chatName,
          groupChat
        )
      }
    }

  }


  def nullMessage: Message =
    Message("", UUID.fromString("a092dbb2-2a69-4876-bbe4-8453aa5b6979"),"NULL_LOGIN", 0L, ZoneOffset.UTC, "", "", groupChat = false)

}

