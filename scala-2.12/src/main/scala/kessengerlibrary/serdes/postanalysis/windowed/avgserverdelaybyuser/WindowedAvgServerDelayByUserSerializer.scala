package io.github.malyszaryczlowiek
package kessengerlibrary.serdes.postanalysis.windowed.avgserverdelaybyuser

import kessengerlibrary.model.postanalysis.WindowedAvgServerDelayByUser

import io.circe.syntax.EncoderOps
import org.apache.kafka.common.serialization.Serializer

class WindowedAvgServerDelayByUserSerializer extends Serializer[WindowedAvgServerDelayByUser] {

  override def serialize(topic: String, data: WindowedAvgServerDelayByUser): Array[Byte] =
    data.asJson.noSpaces.getBytes

}