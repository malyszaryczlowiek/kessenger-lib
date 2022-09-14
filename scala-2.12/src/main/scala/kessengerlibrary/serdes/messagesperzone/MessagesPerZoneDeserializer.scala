package io.github.malyszaryczlowiek
package kessengerlibrary.serdes.messagesperzone

import kessengerlibrary.messages.MessagesPerZone
import kessengerlibrary.messages.MessagesPerZone.decoder

import io.circe.parser.decode
import org.apache.kafka.common.serialization.Deserializer

class MessagesPerZoneDeserializer extends Deserializer[MessagesPerZone] {
  
  override def deserialize(topic: String, data: Array[Byte]): MessagesPerZone =
    decode[MessagesPerZone](new String(data)) match {
      case Left(_)                    => null
      case Right(m: MessagesPerZone)  => m
    }
  
}
