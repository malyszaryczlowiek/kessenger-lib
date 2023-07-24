package io.github.malyszaryczlowiek
package kessengerlibrary.model

import kessengerlibrary.domain.Domain.*

import java.sql.Timestamp


case class SparkMessage(server_time: Timestamp, chat_id: ChatId, chat_name: ChatName, group_chat: GroupChat,
                        zone_id: ZoneId, message_time: Timestamp, content: Content, user_id: StrUserID,
                        login: Login)

object SparkMessage
