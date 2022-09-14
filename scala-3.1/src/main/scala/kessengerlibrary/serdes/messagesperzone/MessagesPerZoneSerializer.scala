package io.github.malyszaryczlowiek
package kessengerlibrary.serdes.messagesperzone

import kessengerlibrary.messages.MessagesPerZone

import io.circe.syntax.*
import org.apache.kafka.common.serialization.Serializer

class MessagesPerZoneSerializer extends Serializer[MessagesPerZone] {
  
  override def serialize(topic: String, data: MessagesPerZone): Array[Byte] =
    data.asJson.noSpaces.getBytes

}
