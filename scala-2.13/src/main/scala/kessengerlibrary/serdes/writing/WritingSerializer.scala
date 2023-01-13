package io.github.malyszaryczlowiek
package kessengerlibrary.serdes.writing

import io.circe.syntax._
import kessengerlibrary.model.Writing
import kessengerlibrary.model.Writing.kafkaEncoder
import org.apache.kafka.common.serialization.Serializer

class WritingSerializer extends  Serializer[Writing] {

  override def serialize(topic: String, data: Writing): Array[Byte] =
    data.asJson(kafkaEncoder).noSpaces.getBytes


}
