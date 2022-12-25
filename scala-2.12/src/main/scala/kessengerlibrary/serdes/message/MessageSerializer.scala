package io.github.malyszaryczlowiek
package kessengerlibrary.serdes.message

import io.circe.syntax._
import io.github.malyszaryczlowiek.kessengerlibrary.model.Message
import org.apache.kafka.common.serialization.Serializer

class MessageSerializer extends Serializer[Message] {
  
  override def serialize(topic: String, data: Message): Array[Byte] =
    data.asJson.noSpaces.getBytes

}
