package io.github.malyszaryczlowiek
package kessengerlibrary.model

import kessengerlibrary.domain.Domain.ChatId

import io.circe.Decoder.Result
import io.circe.parser.decode
import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder, Error, HCursor, Json}

case class FetchMessagesFrom(chatId: ChatId)

object FetchMessagesFrom {

  given encoder: Encoder[FetchMessagesFrom] = (a: FetchMessagesFrom) =>
    Json.obj( ("chatId", a.chatId.asJson) )



  given decoder: Decoder[FetchMessagesFrom] = (c: HCursor) =>
    for {
      chatId <- c.downField("chatId").as[String]
    } yield {
      FetchMessagesFrom(chatId)
    }



  def parseFetchingOlderMessagesRequest(json: String): Either[Error, FetchMessagesFrom] = decode[FetchMessagesFrom](json)

  def toJSON(c: FetchMessagesFrom): String = c.asJson.noSpaces


}
