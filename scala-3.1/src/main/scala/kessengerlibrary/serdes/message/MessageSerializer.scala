package io.github.malyszaryczlowiek
package kessengerlibrary.serdes.message

import kessengerlibrary.messages.Message
import kessengerlibrary.messages.Message.given

import io.circe.syntax.*
import org.apache.kafka.common.serialization.Serializer

class MessageSerializer extends Serializer[Message] {
  
  override def serialize(topic: String, data: Message): Array[Byte] =
    data.asJson.noSpaces.getBytes

}
