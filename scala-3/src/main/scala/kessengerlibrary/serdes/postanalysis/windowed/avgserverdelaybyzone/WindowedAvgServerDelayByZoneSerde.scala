package io.github.malyszaryczlowiek
package kessengerlibrary.serdes.postanalysis.windowed.avgserverdelaybyzone

import kessengerlibrary.model.postanalysis.WindowedAvgServerDelayByZone

import org.apache.kafka.common.serialization.{Deserializer, Serde, Serializer}

class WindowedAvgServerDelayByZoneSerde extends Serde[WindowedAvgServerDelayByZone] {

  override def serializer(): Serializer[WindowedAvgServerDelayByZone] =
    new WindowedAvgServerDelayByZoneSerializer

  override def deserializer(): Deserializer[WindowedAvgServerDelayByZone] =
    new WindowedAvgServerDelayByZoneDeserializer
}
