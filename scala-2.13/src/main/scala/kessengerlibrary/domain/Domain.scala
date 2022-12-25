package io.github.malyszaryczlowiek
package kessengerlibrary.domain

import kessengerlibrary.db.queries.QueryError

import java.util.UUID

object Domain {

  type UserID          = UUID
  type StrUserID       = String
  type Login           = String
  type Password        = String
  type ChatName        = String
  type ChatId          = String
  type Content         = String
  type WritingId       = String
  type JoinId          = String
  type ZoneId          = String
  type ServerDateTime  = String
  type MessageTime     = Long
  type ServerTime      = Long
  type Offset          = Long
  type Partition       = Int
  type GroupChat       = Boolean
  type DbResponse[A]   = Either[QueryError, A]


  def generateChatId(uuid1: UUID, uuid2: UUID): ChatId =
    s"chat--$uuid1--$uuid2"


  def generateWritingId(chatId: ChatId): WritingId =
    s"who_is_writing--${chatId.stripPrefix("chat--")}"


  def generateJoinId(user: UUID): JoinId =
    s"join--${user.toString}"

}
