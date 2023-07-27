package io.github.malyszaryczlowiek
package kessengerlibrary.serdes.postanalysis.windowed.avgserverdelay

import kessengerlibrary.model.postanalysis.WindowedAvgServerDelay

import io.circe.parser.decode
import org.apache.kafka.common.serialization.Deserializer

class WindowedAvgServerDelayDeserializer extends Deserializer[WindowedAvgServerDelay] {

  override def deserialize(topic: String, data: Array[Byte]): WindowedAvgServerDelay =
    decode[WindowedAvgServerDelay](new String(data)) match {
      case Left(_)                           => WindowedAvgServerDelay.nullMessage
      case Right(m: WindowedAvgServerDelay)  => m
    }

}