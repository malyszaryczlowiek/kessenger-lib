package io.github.malyszaryczlowiek
package kessengerlibrary.domain

import kessengerlibrary.domain.Domain.{ChatId, ChatName}

import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, HCursor, Json, Error}
import io.circe.parser.decode
import io.circe.syntax._



/**
 *
 */
case class Chat(chatId: ChatId, chatName: ChatName, groupChat: Boolean, lastMessageTime: Long, silent: Boolean)

object Chat {

  implicit object OrderChatWithDate extends Ordering[Chat] {
    override def compare(x: Chat, y: Chat): Int = {
      if (x.lastMessageTime < y.lastMessageTime) -1
      else if (x.lastMessageTime > y.lastMessageTime) 1
      else 0
    }
  }


  implicit object ReverseChatOrdering extends Ordering[Chat]  {
    override def compare(x: Chat, y: Chat): Int = OrderChatWithDate.compare(x,y) * -1
  }


  implicit object encoder extends Encoder[Chat] {
    override def apply(a: Chat): Json = {
      Json.obj(
        ("chat_id",           Json.fromString(a.chatId)),
        ("chat_name",         Json.fromString(a.chatName)),
        ("group_chat",        Json.fromBoolean(a.groupChat)),
        ("last_message_time", Json.fromLong(a.lastMessageTime)),
        ("silent",            Json.fromBoolean(a.silent))
      )
    }
  }


  implicit object decoder extends Decoder[Chat] {
    override def apply(c: HCursor): Result[Chat] = {
      for {
        chatId          <- c.downField("chat_id").as[String]
        chatName        <- c.downField("chat_name").as[String]
        groupChat       <- c.downField("group_chat").as[Boolean]
        lastMessageTime <- c.downField("last_message_time").as[Long]
        silent          <- c.downField("silent").as[Boolean]
      } yield {
        Chat(chatId, chatName, groupChat, lastMessageTime, silent)
      }
    }
  }


  def parseJSONtoChat(json: String): Either[Error, Chat] = decode[Chat](json)

  def parseChatToJSON(chat: Chat): String = chat.asJson.noSpaces

  def parseJSONtoListOfChats(json: String): Either[Error, List[Chat]] = decode[List[Chat]](json)

  def parseListOfChatsToJSON(chats: List[Chat]): String = chats.asJson.noSpaces


  /**
   * nullUser has no login and is used to signify
   * deserialization error, such user must be ignored
   * in any processing
   *
   * @return
   */
  def nullChat: Chat = Chat("","", groupChat = true, -1L, silent = false)

}

