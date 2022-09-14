package io.github.malyszaryczlowiek
package kessengerlibrary.serdes.message

import kessengerlibrary.messages.Message
import kessengerlibrary.messages.Message.decoder

import io.circe.parser.decode
import org.apache.kafka.common.serialization.Deserializer

class MessageDeserializer extends Deserializer[Message] {
  
  override def deserialize(topic: String, data: Array[Byte]): Message =
    decode[Message](new String(data)) match {
      case Left(_)            => Message.nullMessage
      case Right(m: Message)  => m
    }
  
}
