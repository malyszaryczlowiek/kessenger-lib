package io.github.malyszaryczlowiek
package kessengerlibrary.serdes.messagesperzone

import kessengerlibrary.messages.MessagesPerZone

import org.apache.kafka.common.serialization.{Deserializer, Serde, Serializer}

class MessagesPerZoneSerde extends Serde[MessagesPerZone] {
  
  override def serializer(): Serializer[MessagesPerZone] =
    new MessagesPerZoneSerializer

  override def deserializer(): Deserializer[MessagesPerZone] =
    new MessagesPerZoneDeserializer
}
