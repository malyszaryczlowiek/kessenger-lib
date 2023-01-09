package io.github.malyszaryczlowiek
package kessengerlibrary.model

import kessengerlibrary.domain.Domain.{ChatId, ChatName}

import io.circe.Decoder.Result
import io.circe.parser.decode
import io.circe.syntax.*
import io.circe.{Decoder, Encoder, Error, HCursor, Json}

import java.time.LocalDateTime

/**
 *
 */
case class Chat(chatId: ChatId, chatName: ChatName, groupChat: Boolean, lastMessageTime: Long, silent: Boolean)

object Chat:

  given reverseChatOrdering: Ordering[Chat] with
    override def compare(x: Chat, y: Chat): Int = orderChatWithDate.compare(x,y) * -1


  given orderChatWithDate: Ordering[Chat] with
    override def compare(x: Chat, y: Chat): Int =
      if (x.lastMessageTime < y.lastMessageTime) -1
      else if (x.lastMessageTime > y.lastMessageTime) 1
      else 0


  given encoder: Encoder[Chat] = (a: Chat) =>
    Json.obj(
      ("chatId",           Json.fromString(a.chatId)),
      ("chatName",         Json.fromString(a.chatName)),
      ("groupChat",        Json.fromBoolean(a.groupChat)),
      ("lastMessageTime",  Json.fromLong(a.lastMessageTime)),
      ("silent",           Json.fromBoolean(a.silent))
    )

  given decoder: Decoder[Chat] = (c: HCursor) =>
    for {
      chatId          <- c.downField("chatId").as[String]
      chatName        <- c.downField("chatName").as[String]
      groupChat       <- c.downField("groupChat").as[Boolean]
      lastMessageTime <- c.downField("lastMessageTime").as[Long]
      silent          <- c.downField("silent").as[Boolean]
    } yield {
      Chat(chatId, chatName, groupChat, lastMessageTime, silent)
    }

  given newChatIdDecoder : Decoder[String]  = (c: HCursor) =>
      for {
        chatId <- c.downField("chatId").as[String]
      } yield {
        chatId
      }


  def parseNewChatId(json: String): Either[Error, String] = decode[String](json)(newChatIdDecoder)

  def parseJSONtoChat(json: String): Either[Error, Chat] = decode[Chat](json)

  def parseJSONtoListOfChats(json: String): Either[Error, List[Chat]] = decode[List[Chat]](json)

  def toJSON(chat: Chat): String = chat.asJson.noSpaces

  def toJSON(chats: List[Chat]): String = chats.asJson.noSpaces

  def nullChat: Chat = Chat("","", groupChat = true, -1L, silent = false)

