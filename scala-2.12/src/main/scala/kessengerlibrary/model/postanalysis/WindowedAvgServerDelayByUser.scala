package io.github.malyszaryczlowiek
package kessengerlibrary.model.postanalysis

import io.circe.Decoder.Result
import io.circe.parser.decode
import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder, Error, HCursor, Json}
import kessengerlibrary.domain.Domain.UserID

import java.sql.Timestamp
import java.time.Instant
import java.util.UUID


case class WindowedAvgServerDelayByUser(windowStart: Timestamp, windowEnd: Timestamp, userId: UserID, delayMS: Long)

object WindowedAvgServerDelayByUser {

  implicit object encoder extends Encoder[WindowedAvgServerDelayByUser] {
    override def apply(a: WindowedAvgServerDelayByUser): Json = {
      Json.obj(
        ("windowStart", a.windowStart.toString.asJson),
        ("windowEnd",   a.windowEnd.toString.asJson),
        ("userId",      a.userId.toString.asJson),
        ("delayMS",     a.delayMS.asJson)
      )
    }
  }


  implicit object decoder extends Decoder[WindowedAvgServerDelayByUser] {
    override def apply(c: HCursor): Result[WindowedAvgServerDelayByUser] = {
      for {
        windowStart <- c.downField("windowStart").as[String]
        windowEnd   <- c.downField("windowEnd").as[String]
        userId      <- c.downField("userId").as[String]
        delayMS     <- c.downField("delayMS").as[Long]
      } yield {
        WindowedAvgServerDelayByUser(
          Timestamp.from(Instant.parse( windowStart )),
          Timestamp.from(Instant.parse( windowEnd )),
          UUID.fromString( userId ),
          delayMS
        )
      }
    }
  }


  def parseWindowedAvgServerDelayByUser(json: String): Either[Error, WindowedAvgServerDelayByUser] = decode[WindowedAvgServerDelayByUser](json)

  def toJSON(c: WindowedAvgServerDelayByUser): String = c.asJson.noSpaces

  def nullMessage: WindowedAvgServerDelayByUser =
    WindowedAvgServerDelayByUser(null, null, null, 0L)

}
