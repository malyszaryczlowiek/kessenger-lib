package io.github.malyszaryczlowiek
package kessengerlibrary.model

import kessengerlibrary.domain.Domain.ChatId

import io.circe.Decoder.Result
import io.circe.parser.decode
import io.circe.{Decoder, Error, HCursor}

case class ChatPartitionsOffsets(chatId: ChatId, partitionOffset: List[PartitionOffset])

object ChatPartitionsOffsets {
  implicit object decoder extends Decoder[ChatPartitionsOffsets] {
    override def apply(c: HCursor): Result[ChatPartitionsOffsets] = {
      for {
        chatId          <- c.downField("chatId").as[String]
        partitionOffset <- c.downField("partitionOffset").as[List[PartitionOffset]]
      } yield {
        ChatPartitionsOffsets(chatId, partitionOffset)
      }
    }
  }

  def parseChatPartitionOffsets(json: String): Either[Error, ChatPartitionsOffsets] = decode[ChatPartitionsOffsets](json)
}