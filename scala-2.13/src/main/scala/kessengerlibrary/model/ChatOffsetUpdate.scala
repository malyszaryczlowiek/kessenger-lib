package io.github.malyszaryczlowiek
package kessengerlibrary.model

import kessengerlibrary.domain.Domain.{ChatId, UserID}

import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, Error, HCursor, Json}
import io.circe.parser.decode
import io.circe.syntax.EncoderOps

import java.util.UUID

case class ChatOffsetUpdate(userId: UserID, chatId: ChatId, joiningOffset: Long, partitionOffsets: Map[Int, Long])

object ChatOffsetUpdate {


  implicit object encoder extends Encoder[ChatOffsetUpdate] {
    override def apply(a: ChatOffsetUpdate): Json = {
      Json.obj(
        ("userId", a.userId.toString.asJson),
        ("chatId", a.chatId.asJson),
        ("partitionOffsets", a.partitionOffsets.toList.map(tup => {
          Json.obj(
            ("partition", Json.fromInt(tup._1)),
            ("offset", Json.fromLong(tup._2))
          )
        }).asJson)
      )
    }
  }


  implicit object decoder extends Decoder[ChatOffsetUpdate] {
    override def apply(c: HCursor): Result[ChatOffsetUpdate] = {
      for {
        userId           <- c.downField("userId").as[String]
        chatId           <- c.downField("chatId").as[String]
        joiningOffset    <- c.downField("joiningOffset").as[Long]
        partitionOffsets <- c.downField("partitionOffsets").as[Map[Int, Long]]
      } yield {
        ChatOffsetUpdate(UUID.fromString(userId), chatId, joiningOffset, partitionOffsets)
      }
    }
  }


  def parseChatOffsetUpdate(json: String): Either[Error, ChatOffsetUpdate] = decode[ChatOffsetUpdate](json)

  def toJSON(c: ChatOffsetUpdate): String = c.asJson.noSpaces


}
