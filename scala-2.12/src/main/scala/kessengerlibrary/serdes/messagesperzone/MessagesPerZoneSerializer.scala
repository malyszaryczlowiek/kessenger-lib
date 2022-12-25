package io.github.malyszaryczlowiek
package kessengerlibrary.serdes.messagesperzone

import io.circe.syntax._
import io.github.malyszaryczlowiek.kessengerlibrary.model.MessagesPerZone
import org.apache.kafka.common.serialization.Serializer

class MessagesPerZoneSerializer extends Serializer[MessagesPerZone] {
  
  override def serialize(topic: String, data: MessagesPerZone): Array[Byte] =
    data.asJson.noSpaces.getBytes

}
