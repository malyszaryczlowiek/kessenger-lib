package io.github.malyszaryczlowiek
package kessengerlibrary.model

import kessengerlibrary.domain.Domain.ChatId
import kessengerlibrary.model

import io.circe.Decoder.Result
import io.circe.parser.decode
import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder, Error, HCursor, Json}


case class Configuration(me: User, joiningOffset: Long, chats: Map[ChatId, Map[Int, Long]])

object Configuration {

  implicit object encoder extends Encoder[Configuration] {
    override def apply(a: Configuration): Json = {
      Json.obj(
        ("me", a.me.asJson ),
        ("joiningOffset", Json.fromLong(a.joiningOffset)),
        ("chats", a.chats.map( keyValue => {
          Json.obj(
            ("chat", keyValue._1.asJson),
            ("partitionOffsets", keyValue._2.map(tup => {
              Json.obj(
                ("partition", Json.fromInt(tup._1)),
                ("offset", Json.fromLong(tup._2))
              )
            }).asJson)
          )
        }).asJson)
      )
    }
  }


  implicit object decoder extends Decoder[Configuration] {
    override def apply(c: HCursor): Result[Configuration] = {
      for {
        me            <- c.downField("me").as[User]
        joiningOffset <- c.downField("joiningOffset").as[Long]
        chats         <- c.downField("chats").as[Map[ChatId, Map[Int,Long]]]
      } yield {
        model.Configuration(me, joiningOffset, chats)
      }
    }
  }


  def parseConfiguration(json: String): Either[Error, Configuration] = decode[Configuration](json)

  def toJSON(c: Configuration): String = c.asJson.noSpaces

}
