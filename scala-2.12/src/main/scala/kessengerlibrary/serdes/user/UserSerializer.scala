package io.github.malyszaryczlowiek
package kessengerlibrary.serdes.user

import io.github.malyszaryczlowiek.kessengerlibrary.model.User.toJSON
import io.github.malyszaryczlowiek.kessengerlibrary.model.User

import org.apache.kafka.common.serialization.Serializer


class UserSerializer extends Serializer[User] {

  override def serialize(topic: String, data: User): Array[Byte] =
    toJSON(data).getBytes
  
}
