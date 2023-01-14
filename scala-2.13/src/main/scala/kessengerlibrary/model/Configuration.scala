package io.github.malyszaryczlowiek
package kessengerlibrary.model

import kessengerlibrary.domain.Domain.ChatId
import kessengerlibrary.model.Configuration.ChatPartitionsOffsets

import io.circe.Decoder.Result
import io.circe.parser.decode
import io.circe.{Decoder, Error, HCursor}





// case class Configuration(me: User, joiningOffset: Long, chats: Map[ChatId, Map[Int, Long]])
case class Configuration(me: User, joiningOffset: Long, chats: List[ChatPartitionsOffsets])

object Configuration {

  case class PartitionOffset(partition: Int, offset: Long)

  object PartitionOffset {
    implicit object decoder extends Decoder[PartitionOffset] {
      override def apply(c: HCursor): Result[PartitionOffset] = {
        for {
          partition <- c.downField("partition").as[Int]
          offset    <- c.downField("offset").as[Long]
        } yield {
          PartitionOffset(partition, offset)
        }
      }
    }
  }

  case class ChatPartitionsOffsets(chatId: ChatId, partitionOffset: List[PartitionOffset])

  object  ChatPartitionsOffsets {
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
  }


  implicit object decoder extends Decoder[Configuration] {
    override def apply(c: HCursor): Result[Configuration] = {
      for {
        me            <- c.downField("me").as[User]
        joiningOffset <- c.downField("joiningOffset").as[Long]
        chats         <- c.downField("chats").as[List[ChatPartitionsOffsets]]
      } yield {
        Configuration(me, joiningOffset, chats)
      }
    }
  }


  def parseConfiguration(json: String): Either[Error, Configuration] = decode[Configuration](json)

}




// we do not send our configuration anywhere.
// def toJSON(c: Configuration): String = c.asJson.noSpaces

// encoder is not necessery
//  implicit object encoder extends Encoder[Configuration] {
//    override def apply(a: Configuration): Json = {
//      Json.obj(
//        ("me", a.me.asJson),
//        ("joiningOffset", Json.fromLong(a.joiningOffset)),
//        ("chats", a.chats.map( keyValue => {
//          Json.obj(
//            ("chatId", keyValue._1.asJson),
//            ("partitionOffset", keyValue._2.map(tup => {
//              Json.obj(
//                ("partition", Json.fromInt(tup._1)),
//                ("offset", Json.fromLong(tup._2))
//              )
//            }).asJson)
//          )
//        }).asJson)
//      )
//    }
//  }


