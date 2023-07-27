package io.github.malyszaryczlowiek
package kessengerlibrary.serdes.postanalysis.windowed.avgserverdelay

import kessengerlibrary.model.postanalysis.WindowedAvgServerDelay

import org.apache.kafka.common.serialization.{Deserializer, Serde, Serializer}

class WindowedAvgServerDelaySerde extends Serde[WindowedAvgServerDelay] {

  override def serializer(): Serializer[WindowedAvgServerDelay] =
    new WindowedAvgServerDelaySerializer

  override def deserializer(): Deserializer[WindowedAvgServerDelay] =
    new WindowedAvgServerDelayDeserializer
}
