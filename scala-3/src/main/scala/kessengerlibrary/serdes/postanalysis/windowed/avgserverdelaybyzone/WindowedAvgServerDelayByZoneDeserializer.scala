package io.github.malyszaryczlowiek
package kessengerlibrary.serdes.postanalysis.windowed.avgserverdelaybyzone

import kessengerlibrary.model.postanalysis.WindowedAvgServerDelayByZone

import io.circe.parser.decode
import org.apache.kafka.common.serialization.Deserializer

class WindowedAvgServerDelayByZoneDeserializer extends Deserializer[WindowedAvgServerDelayByZone] {

  override def deserialize(topic: String, data: Array[Byte]): WindowedAvgServerDelayByZone =
    decode[WindowedAvgServerDelayByZone](new String(data)) match {
      case Left(_)                                 => WindowedAvgServerDelayByZone.nullMessage
      case Right(m: WindowedAvgServerDelayByZone)  => m
    }

}