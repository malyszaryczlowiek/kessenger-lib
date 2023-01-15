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




