package io.github.malyszaryczlowiek
package kessengerlibrary.serdes.postanalysis.windowed.avgserverdelaybyuser

import kessengerlibrary.model.postanalysis.WindowedAvgServerDelayByUser

import org.apache.kafka.common.serialization.{Deserializer, Serde, Serializer}

class WindowedAvgServerDelayByUserSerde extends Serde[WindowedAvgServerDelayByUser] {

  override def serializer(): Serializer[WindowedAvgServerDelayByUser] =
    new WindowedAvgServerDelayByUserSerializer

  override def deserializer(): Deserializer[WindowedAvgServerDelayByUser] =
    new WindowedAvgServerDelayByUserDeserializer
}
