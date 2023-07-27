package io.github.malyszaryczlowiek
package kessengerlibrary.serdes.postanalysis.windowed.avgserverdelaybyuser

import kessengerlibrary.model.postanalysis.WindowedAvgServerDelayByUser

import io.circe.parser.decode
import org.apache.kafka.common.serialization.Deserializer

class WindowedAvgServerDelayByUserDeserializer extends Deserializer[WindowedAvgServerDelayByUser] {

  override def deserialize(topic: String, data: Array[Byte]): WindowedAvgServerDelayByUser =
    decode[WindowedAvgServerDelayByUser](new String(data)) match {
      case Left(_)                                 => WindowedAvgServerDelayByUser.nullMessage
      case Right(m: WindowedAvgServerDelayByUser)  => m
    }

}