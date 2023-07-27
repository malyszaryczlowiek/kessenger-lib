package io.github.malyszaryczlowiek
package kessengerlibrary.model.postanalysis

import io.circe.Decoder.Result
import io.circe.parser.decode
import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder, Error, HCursor, Json}

import java.sql.Timestamp
import java.time.{Instant, ZoneId}


case class WindowedAvgServerDelayByZone(windowStart: Timestamp, windowEnd: Timestamp, zoneId: ZoneId, delayMS: Long)

object WindowedAvgServerDelayByZone {

  implicit object encoder extends Encoder[WindowedAvgServerDelayByZone] {
    override def apply(a: WindowedAvgServerDelayByZone): Json = {
      Json.obj(
        ("windowStart", a.windowStart.toString.asJson),
        ("windowEnd",   a.windowEnd.toString.asJson),
        ("zoneId",      a.zoneId.toString.asJson),
        ("delayMS",     a.delayMS.asJson)
      )
    }
  }


  implicit object decoder extends Decoder[WindowedAvgServerDelayByZone] {
    override def apply(c: HCursor): Result[WindowedAvgServerDelayByZone] = {
      for {
        windowStart <- c.downField("windowStart").as[String]
        windowEnd   <- c.downField("windowEnd").as[String]
        zoneId      <- c.downField("zoneId").as[String]
        delayMS     <- c.downField("delayMS").as[Long]
      } yield {
        WindowedAvgServerDelayByZone(
          Timestamp.from(Instant.parse( windowStart )),
          Timestamp.from(Instant.parse( windowEnd )),
          ZoneId.of( zoneId ),
          delayMS
        )
      }
    }
  }


  def parseWindowedAvgServerDelayByZone(json: String): Either[Error, WindowedAvgServerDelayByZone] = decode[WindowedAvgServerDelayByZone](json)

  def toJSON(c: WindowedAvgServerDelayByZone): String = c.asJson.noSpaces

  def nullMessage: WindowedAvgServerDelayByZone =
    WindowedAvgServerDelayByZone(null, null, null, 0L)

}
