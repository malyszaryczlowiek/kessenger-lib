package io.github.malyszaryczlowiek
package kessengerlibrary.serdes.message

import io.github.malyszaryczlowiek.kessengerlibrary.model.Message
import org.apache.kafka.common.serialization.{Deserializer, Serde, Serializer}

class MessageSerde extends Serde[Message] {
  
  override def serializer(): Serializer[Message] = 
    new MessageSerializer

  override def deserializer(): Deserializer[Message] =
    new MessageDeserializer
}
