package io.github.malyszaryczlowiek
package kessengerlibrary.serdes.messagesperzone

import io.github.malyszaryczlowiek.kessengerlibrary.model.MessagesPerZone.decoder

import io.circe.parser.decode
import io.github.malyszaryczlowiek.kessengerlibrary.model.MessagesPerZone
import org.apache.kafka.common.serialization.Deserializer

class MessagesPerZoneDeserializer extends Deserializer[MessagesPerZone] {
  
  override def deserialize(topic: String, data: Array[Byte]): MessagesPerZone =
    decode[MessagesPerZone](new String(data)) match {
      case Left(_)                    => null
      case Right(m: MessagesPerZone)  => m
    }
  
}
