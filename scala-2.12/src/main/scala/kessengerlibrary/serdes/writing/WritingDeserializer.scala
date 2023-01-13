package io.github.malyszaryczlowiek
package kessengerlibrary.serdes.writing

import kessengerlibrary.model.Writing
import kessengerlibrary.model.Writing.decoder

import io.circe.parser.decode
import org.apache.kafka.common.serialization.Deserializer

import java.util.UUID


class WritingDeserializer extends Deserializer[Writing] {

  override def deserialize(topic: String, data: Array[Byte]): Writing =
    decode(new String(data)) match {
      case Left(_) => Writing("","",UUID.randomUUID())
      case Right(value) => value
    }

}
