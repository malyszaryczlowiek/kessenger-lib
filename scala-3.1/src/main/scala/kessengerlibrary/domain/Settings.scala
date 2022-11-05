package io.github.malyszaryczlowiek
package kessengerlibrary.domain

import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, Error, HCursor, Json}
import io.circe.parser.decode
import io.circe.syntax.*

import java.time.ZoneId



case class Settings(joiningOffset: Long = 0L, sessionDuration: Long = 900L, zoneId: ZoneId = ZoneId.of("UTC"))

object Settings:

  given encoder: Encoder[Settings] = (a: Settings) =>
    Json.obj(
      ("joining_offset",   Json.fromLong(a.joiningOffset)  ),
      ("session_duration", Json.fromLong(a.sessionDuration)),
      ("zone_id",          Json.fromString(a.zoneId.getId))
    )




  given decoder: Decoder[Settings] = (c: HCursor) =>
    for {
      joiningOffset   <- c.downField("joining_offset").as[Long]
      sessionDuration <- c.downField("session_duration").as[Long]
      zoneId          <- c.downField("zone_id").as[String]
    } yield {
      Settings(joiningOffset, sessionDuration, ZoneId.of(zoneId))
    }


  def parseJSONtoSettings(json: String): Either[Error, Settings] = decode[Settings](json)

  def parseSettingsToJSON(settings: Settings): String = settings.asJson.noSpaces


  /**
  * nullUser has no login and is used to signify
  * deserialization error, such user must be ignored
  * in any processing
  *
  * @return
  */
  def nullSettings: Settings = Settings()




