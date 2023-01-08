package io.github.malyszaryczlowiek
package kessengerlibrary.model


import kessengerlibrary.domain.Domain.UserID

import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, Error, HCursor, Json}
import io.circe.parser.decode
import io.circe.syntax.EncoderOps

import java.util.UUID

case class UserOffsetUpdate(userId: UserID, joiningOffset: Long)

object UserOffsetUpdate {


  implicit object encoder extends Encoder[UserOffsetUpdate] {
    override def apply(a: UserOffsetUpdate): Json = {
      Json.obj(
        ("userId", a.userId.toString.asJson),
        ("joiningOffset", a.joiningOffset.asJson),
      )
    }
  }


  implicit object decoder extends Decoder[UserOffsetUpdate] {
    override def apply(c: HCursor): Result[UserOffsetUpdate] = {
      for {
        userId        <- c.downField("userId").as[String]
        joiningOffset <- c.downField("joiningOffset").as[Long]
      } yield {
        UserOffsetUpdate(UUID.fromString(userId), joiningOffset)
      }
    }
  }


  def parseUserOffsetUpdate(json: String): Either[Error, UserOffsetUpdate] = decode[UserOffsetUpdate](json)

  def toJSON(c: UserOffsetUpdate): String = c.asJson.noSpaces


}