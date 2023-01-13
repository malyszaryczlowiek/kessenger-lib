package io.github.malyszaryczlowiek
package kessengerlibrary.serdes.writing

import kessengerlibrary.model.Writing
import org.apache.kafka.common.serialization.{Deserializer, Serde, Serializer}

class WritingSerde extends Serde[Writing]{

  override def serializer(): Serializer[Writing] = new WritingSerializer

  override def deserializer(): Deserializer[Writing] = new WritingDeserializer

}
