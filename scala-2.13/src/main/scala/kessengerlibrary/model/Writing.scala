package io.github.malyszaryczlowiek
package kessengerlibrary.model

import kessengerlibrary.domain.Domain.{ChatId, Login, UserID}

import io.circe.Decoder.Result
import io.circe.parser.decode
import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder, Error, HCursor, Json}

import java.util.UUID



case class Writing(chatId: ChatId, writerLogin: Login, writerId: UserID)

object Writing {

  implicit object kafkaEncoder extends Encoder[Writing] {
    override def apply(a: Writing): Json =
      Json.obj(
        ("chatId",      Json.fromString(a.chatId)),
        ("writerLogin", Json.fromString(a.writerLogin)),
        ("writerId",    Json.fromString(a.writerId.toString))
      )
  }

  private implicit object websocketMessageEncoder extends Encoder[Writing] {
    override def apply(a: Writing): Json =
      Json.obj(
        ("wrt", a.asJson(kafkaEncoder))
      )
  }


  implicit object decoder extends Decoder[Writing] {
    override def apply(c: HCursor): Result[Writing] = {
      for {
        chatId      <- c.downField("chatId").as[String]
        writerLogin <- c.downField("writerLogin").as[String]
        writerId    <- c.downField("writerId").as[String]
      } yield {
        Writing(
          chatId,
          writerLogin,
          UUID.fromString( writerId )
        )
      }
    }
  }

  def toWebsocketJSON(m: Writing): String = m.asJson(websocketMessageEncoder).noSpaces

  def toKafkaJSON(m: Writing): String = m.asJson(kafkaEncoder).noSpaces

  def parseWriting(json: String): Either[Error, Writing] = decode[Writing](json)


}