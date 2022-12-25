package io.github.malyszaryczlowiek
package kessengerlibrary.serdes.user

import kessengerlibrary.serdes.user.UserDeserializer
import io.github.malyszaryczlowiek.kessengerlibrary.model.User

import org.apache.kafka.common.serialization.{Deserializer, Serde, Serializer}

class UserSerde extends Serde[User] {

  override def serializer(): Serializer[User] = new UserSerializer

  override def deserializer(): Deserializer[User] = new UserDeserializer
}




