package io.github.malyszaryczlowiek
package kessengerlibrary.model

import kessengerlibrary.domain.Domain.{ServerDateTime, ZoneId}

import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, HCursor, Json}


case class MessagesPerZone(fromTime: ServerDateTime, toTime: ServerDateTime, zoneId: ZoneId, number: Long)

object MessagesPerZone {

  implicit object encoder extends Encoder[MessagesPerZone] {
    override def apply(a: MessagesPerZone): Json =
      Json.obj(
        ( "fromTime", Json.fromString( a.fromTime )),
        ( "toTime",   Json.fromString( a.toTime   )),
        ( "zoneId",   Json.fromString( a.zoneId   )),
        ( "number",   Json.fromLong  ( a.number   ))
      )
  }


  implicit object decoder extends  Decoder[MessagesPerZone]  {
    override def apply(c: HCursor): Result[MessagesPerZone] = {
      for {
        fromTime <- c.downField("fromTime").as[String]
        toTime   <- c.downField("toTime")  .as[String]
        zoneId   <- c.downField("zoneId")  .as[String]
        number   <- c.downField("number")  .as[Long]
      } yield {
        MessagesPerZone(
          fromTime,
          toTime,
          zoneId,
          number
        )
      }
    }

  }


//  def nullMessage: MessagesPerZone =
//    MessagesPerZone("", UUID.fromString("a092dbb2-2a69-4876-bbe4-8453aa5b6979"),"NULL_LOGIN", 0L, ZoneOffset.UTC, "", "", groupChat = false)


}
