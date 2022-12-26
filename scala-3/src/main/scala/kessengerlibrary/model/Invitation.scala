package io.github.malyszaryczlowiek
package kessengerlibrary.model

import kessengerlibrary.domain.Domain.{ChatId, ChatName, Login, UserID}

import io.circe.Decoder.Result
import io.circe.parser.decode
import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder, Error, HCursor, Json}

import java.util.UUID


case class Invitation(fromUser: Login, toUserId: UserID, toChat: ChatName, toChatId: ChatId,
                      sendingTime: Long, serverTime: Long = 0L, myJoiningOffset: Option[Long])

object Invitation {


  given kafkaEncoder: Encoder[Invitation] = (a: Invitation) =>
    a.myJoiningOffset match {
      case Some(value) =>
        Json.obj(
          ("login", Json.fromString(a.fromUser)),
          ("toUserId", Json.fromString(a.toUserId.toString)),
          ("chatName", Json.fromString(a.toChat)),
          ("chatId", Json.fromString(a.toChatId)),
          ("sendingTime", Json.fromLong(a.sendingTime)),
          ("serverTime", Json.fromLong(a.serverTime)),
          ("myJoiningOffset", Json.fromLong(value))
        )
      case None =>
        Json.obj(
          ("login",           Json.fromString(a.fromUser)),
          ("toUserId",        Json.fromString(a.toUserId.toString)),
          ("chatName",        Json.fromString(a.toChat)),
          ("chatId",          Json.fromString(a.toChatId)),
          ("sendingTime",     Json.fromLong(a.sendingTime)),
          ("serverTime",      Json.fromLong(a.serverTime)),
          ("myJoiningOffset", Json.fromLong(0L))
        )
    }



  given websocketEncoder: Encoder[Invitation] = (a: Invitation) =>
    a.myJoiningOffset match {
      case Some(value) =>
        Json.obj(
          ("inv",
            Json.obj(
              ("login", Json.fromString(a.fromUser)),
              ("toUserId", Json.fromString(a.toUserId.toString)),
              ("chatName", Json.fromString(a.toChat)),
              ("chatId", Json.fromString(a.toChatId)),
              ("sendingTime", Json.fromLong(a.sendingTime)),
              ("serverTime", Json.fromLong(a.serverTime)),
              ("myJoiningOffset", Json.fromLong(value))
            )
          )
        )
      case None =>
        Json.obj(
          ("inv",
            Json.obj(
              ("login", Json.fromString(a.fromUser)),
              ("toUserId", Json.fromString(a.toUserId.toString)),
              ("chatName", Json.fromString(a.toChat)),
              ("chatId", Json.fromString(a.toChatId)),
              ("sendingTime", Json.fromLong(a.sendingTime)),
              ("serverTime", Json.fromLong(a.serverTime)),
              ("myJoiningOffset", Json.fromLong(0L))
            )
          )
        )
    }



  given decoder: Decoder[Invitation] = (c: HCursor) =>
    for {
      fromUser    <- c.downField("login").as[String]
      toUserId    <- c.downField("toUserId").as[String]
      toChat      <- c.downField("chatName").as[String]
      toChatId    <- c.downField("chatId").as[String]
      sendingTime <- c.downField("sendingTime").as[Long]
      serverTime  <- c.downField("serverTime").as[Long]
      // offset   <- c.downField("myJoiningOffset").as[Long] // not required
    } yield {
      Invitation(fromUser, UUID.fromString(toUserId), toChat, toChatId, sendingTime, serverTime, None)
    }




  def parseInvitation(json: String): Either[Error, Invitation] = decode[Invitation](json)

  def toKafkaJSON(i: Invitation): String = i.asJson(kafkaEncoder).noSpaces

  def toWebsocketJSON(i: Invitation): String = i.asJson(websocketEncoder).noSpaces



  def nullInvitation: Invitation = Invitation("", UUID.randomUUID(), "", "", 0L, 0L, None)

}
