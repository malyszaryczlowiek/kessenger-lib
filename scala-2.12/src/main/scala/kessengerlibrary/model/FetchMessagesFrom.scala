package io.github.malyszaryczlowiek
package kessengerlibrary.model

import kessengerlibrary.domain.Domain.ChatId

import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, Error, HCursor, Json}
import io.circe.parser.decode
import io.circe.syntax.EncoderOps

case class FetchMessagesFrom(chatId: ChatId)

object  FetchMessagesFrom {

  implicit object encoder extends Encoder[FetchMessagesFrom] {
    override def apply(a: FetchMessagesFrom): Json = {
      Json.obj( ("chatId", a.chatId.asJson) )
    }
  }


  implicit object decoder extends Decoder[FetchMessagesFrom] {
    override def apply(c: HCursor): Result[FetchMessagesFrom] = {
      for {
        chatId <- c.downField("chatId").as[String]
      } yield {
        FetchMessagesFrom(chatId)
      }
    }
  }


  def parseFetchingOlderMessagesRequest(json: String): Either[Error, FetchMessagesFrom] = decode[FetchMessagesFrom](json)

  def toJSON(c: FetchMessagesFrom): String = c.asJson.noSpaces


}
