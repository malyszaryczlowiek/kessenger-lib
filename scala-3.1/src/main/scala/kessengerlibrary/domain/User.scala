package io.github.malyszaryczlowiek
package kessengerlibrary.domain

import kessengerlibrary.domain.Domain.{Login, UserID}
import io.circe.{Encoder, Json, Decoder, HCursor}
import java.util.UUID

case class User(userId: UserID, login: Login, salt: Option[String] = None, joiningOffset: Long = 0L)

object User {

  given encoder: Encoder[User] = (a: User) => Json.obj(
    ("userId", Json.fromString(a.userId.toString)),
    ("login",  Json.fromString(a.login))
  )


  given decoder: Decoder[User] = (c: HCursor) =>
    for {
      userId <- c.downField("userId").as[String]
      login  <- c.downField("login").as[String]
    } yield {
      User(UUID.fromString(userId), login)
    }
  

  /**
   * nullUser has no login and is used to signify
   * deserialization error, such user must be ignored
   * in any processing
   *
   * @return
   */
  def nullUser: User = User(UUID.fromString("a092dbb2-2a69-4876-bbe4-8453aa5b6979"), "")

}
