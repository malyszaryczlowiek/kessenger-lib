package io.github.malyszaryczlowiek
package kessengerlibrary.model

import io.circe.Decoder.Result
import io.circe.parser.decode
import io.circe.syntax._
import io.circe.{Decoder, Encoder, Error, HCursor, Json}

import java.time.ZoneId



case class Settings(joiningOffset: Long = 0L, sessionDuration: Long = 900000L, zoneId: ZoneId = ZoneId.of("UTC"))

object Settings {

  implicit object encoder extends Encoder[Settings] {
    override def apply(a: Settings): Json = {
      Json.obj(
        ("joiningOffset",   Json.fromLong(a.joiningOffset)),
        ("sessionDuration", Json.fromLong(a.sessionDuration)),
        ("zoneId",          Json.fromString(a.zoneId.getId))
      )
    }
  }


  implicit object decoder extends Decoder[Settings] {
    override def apply(c: HCursor): Result[Settings] = {
      for {
        joiningOffset   <- c.downField("joiningOffset").as[Long]
        sessionDuration <- c.downField("sessionDuration").as[Long]
        zoneId          <- c.downField("zoneId").as[String]
      } yield {
        Settings(joiningOffset, sessionDuration, ZoneId.of(zoneId))
      }
    }
  }


  def parseJSONtoSettings(json: String): Either[Error, Settings] = decode[Settings](json)

  def toJSON(settings: Settings): String = settings.asJson.noSpaces


  /**
   * nullUser has no login and is used to signify
   * deserialization error, such user must be ignored
   * in any processing
   *
   * @return
   */
  def nullSettings: Settings = Settings()


}
