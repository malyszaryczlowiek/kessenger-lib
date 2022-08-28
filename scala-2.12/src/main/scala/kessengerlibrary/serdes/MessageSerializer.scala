package io.github.malyszaryczlowiek
package kessengerlibrary.serdes

import kessengerlibrary.messages.Message
import io.circe.syntax._
import org.apache.kafka.common.serialization.Serializer

class MessageSerializer extends Serializer[Message] {
  
  override def serialize(topic: String, data: Message): Array[Byte] =
    data.asJson.noSpaces.getBytes

}
