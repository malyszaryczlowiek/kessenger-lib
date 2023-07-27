package io.github.malyszaryczlowiek
package kessengerlibrary.serdes.postanalysis.windowed.avgserverdelaybyzone

import kessengerlibrary.model.postanalysis.WindowedAvgServerDelayByZone

import io.circe.syntax.EncoderOps
import org.apache.kafka.common.serialization.Serializer

class WindowedAvgServerDelayByZoneSerializer extends Serializer[WindowedAvgServerDelayByZone] {

  override def serialize(topic: String, data: WindowedAvgServerDelayByZone): Array[Byte] =
    data.asJson.noSpaces.getBytes

}