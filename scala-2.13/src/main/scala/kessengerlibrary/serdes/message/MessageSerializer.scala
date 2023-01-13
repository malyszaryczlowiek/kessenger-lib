package io.github.malyszaryczlowiek
package kessengerlibrary.serdes.message

import io.circe.syntax._
import kessengerlibrary.model.Message
import kessengerlibrary.model.Message.kafkaEncoder
import org.apache.kafka.common.serialization.Serializer

class MessageSerializer extends Serializer[Message] {
  
  override def serialize(topic: String, data: Message): Array[Byte] =
    data.asJson(kafkaEncoder).noSpaces.getBytes

}
