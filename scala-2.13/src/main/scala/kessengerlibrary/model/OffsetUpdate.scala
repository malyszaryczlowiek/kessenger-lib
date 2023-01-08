package io.github.malyszaryczlowiek
package kessengerlibrary.model

import kessengerlibrary.domain.Domain.{ChatId, UserID}

import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, Error, HCursor, Json}
import io.circe.parser.decode
import io.circe.syntax.EncoderOps

import java.util.UUID

case class OffsetUpdate(userId: UserID, chatId: ChatId, partitionOffsets: Map[Int, Long])

object OffsetUpdate {


  implicit object encoder extends Encoder[OffsetUpdate] {
    override def apply(a: OffsetUpdate): Json = {
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


  implicit object decoder extends Decoder[OffsetUpdate] {
    override def apply(c: HCursor): Result[OffsetUpdate] = {
      for {
        userId           <- c.downField("userId").as[String]
        chatId           <- c.downField("chatId").as[String]
        partitionOffsets <- c.downField("partitionOffsets").as[Map[Int, Long]]
      } yield {
        OffsetUpdate(UUID.fromString(userId), chatId, partitionOffsets)
      }
    }
  }


  def parseConfiguration(json: String): Either[Error, OffsetUpdate] = decode[OffsetUpdate](json)

  def toJSON(c: OffsetUpdate): String = c.asJson.noSpaces


}
