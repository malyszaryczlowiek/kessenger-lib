package io.github.malyszaryczlowiek
package kessengerlibrary.model.postanalysis

import io.circe.Decoder.Result
import io.circe.parser.decode
import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder, Error, HCursor, Json}

import java.sql.Timestamp
import java.time.Instant



case class WindowedAvgServerDelay(windowStart: Timestamp, windowEnd: Timestamp, delayMS: Long)

object WindowedAvgServerDelay {

  implicit object encoder extends Encoder[WindowedAvgServerDelay] {
    override def apply(a: WindowedAvgServerDelay): Json = {
      Json.obj(
        ("windowStart", a.windowStart.toString.asJson),
        ("windowEnd",   a.windowEnd.toString.asJson),
        ("delayMS",     a.delayMS.asJson)
      )
    }
  }


  implicit object decoder extends Decoder[WindowedAvgServerDelay] {
    override def apply(c: HCursor): Result[WindowedAvgServerDelay] = {
      for {
        windowStart <- c.downField("windowStart").as[String]
        windowEnd   <- c.downField("windowEnd").as[String]
        delayMS     <- c.downField("delayMS").as[Long]
      } yield {
        WindowedAvgServerDelay(
          Timestamp.from(Instant.parse( windowStart )),
          Timestamp.from(Instant.parse( windowEnd )),
          delayMS
        )
      }
    }
  }


  def parseWindowedAvgServerDelay(json: String): Either[Error, WindowedAvgServerDelay] = decode[WindowedAvgServerDelay](json)

  def toJSON(c: WindowedAvgServerDelay): String = c.asJson.noSpaces

  def nullMessage: WindowedAvgServerDelay =
    WindowedAvgServerDelay(null, null, 0L)

}
