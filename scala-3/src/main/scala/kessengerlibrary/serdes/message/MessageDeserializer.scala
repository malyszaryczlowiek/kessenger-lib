package io.github.malyszaryczlowiek
package kessengerlibrary.serdes.message

import io.github.malyszaryczlowiek.kessengerlibrary.model.Message.kafkaEncoder

import io.circe.parser.decode
import io.github.malyszaryczlowiek.kessengerlibrary.model.Message
import org.apache.kafka.common.serialization.Deserializer

class MessageDeserializer extends Deserializer[Message] {
  
  override def deserialize(topic: String, data: Array[Byte]): Message =
    decode[Message](new String(data)) match {
      case Left(_)            => Message.nullMessage
      case Right(m: Message)  => m
    }
  
}
