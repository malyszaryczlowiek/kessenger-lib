package io.github.malyszaryczlowiek
package kessengerlibrary.model

import kessengerlibrary.domain.Domain.{Login, UserID}

import io.circe.parser.decode
import io.circe.syntax.*
import io.circe.{Decoder, Encoder, Error, HCursor, Json}

import java.util.UUID

case class User(userId: UserID, login: Login)

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


  def parseUser(json: String): Either[Error, User] = decode[User](json)

  def parseJSONtoListOfUsers(json: String): Either[Error, List[User]] = decode[List[User]](json)

  def toJSON(user: User): String = user.asJson.noSpaces

  def toJSON(users: List[User]): String = users.asJson.noSpaces

  /**
   * nullUser has no login and is used to signify
   * deserialization error, such user must be ignored
   * in any processing
   *
   * @return
   */
  def nullUser: User = User(UUID.fromString("a092dbb2-2a69-4876-bbe4-8453aa5b6979"), "")

}
