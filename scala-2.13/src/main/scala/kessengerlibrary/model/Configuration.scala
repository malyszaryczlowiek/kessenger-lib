package io.github.malyszaryczlowiek
package kessengerlibrary.model

import io.circe.Decoder.Result
import io.circe.parser.decode
import io.circe.{Decoder, Error, HCursor}


case class Configuration(me: User, joiningOffset: Long, chats: List[ChatPartitionsOffsets])

object Configuration {


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


