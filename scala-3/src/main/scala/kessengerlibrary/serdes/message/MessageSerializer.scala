package io.github.malyszaryczlowiek
package kessengerlibrary.serdes.message

import io.github.malyszaryczlowiek.kessengerlibrary.model.Message.{encoder, toKafkaJSON}

import io.circe.syntax.*
import io.github.malyszaryczlowiek.kessengerlibrary.model.Message
import org.apache.kafka.common.serialization.Serializer

class MessageSerializer extends Serializer[Message] {
  
  override def serialize(topic: String, data: Message): Array[Byte] =
    toKafkaJSON(data).getBytes
    //data.asJson(encoder).noSpaces.getBytes

}
