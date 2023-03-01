package io.github.malyszaryczlowiek
package kessengerlibrary.model


import io.circe.Decoder.Result
import io.circe.parser.decode
import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder, Error, HCursor, Json}

import java.util.UUID

case class SessionInfo(sessionId: UUID, userId: UUID, validityTime: Long)



object SessionInfo {

  given encoder: Encoder[SessionInfo] = (a: SessionInfo) => Json.obj(
    ("sessionId",    Json.fromString( a.sessionId.toString )),
    ("userId",       Json.fromString( a.userId.toString    )),
    ("validityTime", Json.fromLong(   a.validityTime       ))
  )



  given decoder: Decoder[SessionInfo] = (c: HCursor) =>
    for {
      sessionId    <- c.downField("sessionId").as[String]
      userId       <- c.downField("userId").as[String]
      validityTime <- c.downField("validityTime").as[Long]
    } yield {
      SessionInfo(UUID.fromString(sessionId), UUID.fromString(userId), validityTime)
    }



  def toJSON(m: SessionInfo): String = m.asJson.noSpaces

  def parseSessionInfo(json: String): Either[Error, SessionInfo] = decode[SessionInfo](json)

}
