package io.github.malyszaryczlowiek
package kessengerlibrary.serdes.postanalysis.windowed.avgserverdelay

import kessengerlibrary.model.postanalysis.WindowedAvgServerDelay

import io.circe.syntax.EncoderOps
import org.apache.kafka.common.serialization.Serializer

class WindowedAvgServerDelaySerializer extends Serializer[WindowedAvgServerDelay] {

  override def serialize(topic: String, data: WindowedAvgServerDelay): Array[Byte] =
    data.asJson.noSpaces.getBytes

}