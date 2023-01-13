package io.github.malyszaryczlowiek
package kessengerlibrary.serdes.writing

import io.circe.parser.decode
import kessengerlibrary.model.Writing
import kessengerlibrary.model.Writing.decoder
import org.apache.kafka.common.serialization.Deserializer
import java.util.UUID


class WritingDeserializer extends Deserializer[Writing] {

  override def deserialize(topic: String, data: Array[Byte]): Writing =
    decode(new String(data)) match {
      case Left(_) => Writing("","",UUID.randomUUID())
      case Right(value) => value
    }

}
