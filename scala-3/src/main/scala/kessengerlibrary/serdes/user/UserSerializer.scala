package io.github.malyszaryczlowiek
package kessengerlibrary.serdes.user

import io.circe.syntax.*
import kessengerlibrary.model.User
import org.apache.kafka.common.serialization.Serializer


class UserSerializer extends Serializer[User] {

  override def serialize(topic: String, data: User): Array[Byte] =
    data.asJson.noSpaces.getBytes
  
}
