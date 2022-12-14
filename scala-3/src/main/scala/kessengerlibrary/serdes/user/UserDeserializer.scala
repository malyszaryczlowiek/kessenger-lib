package io.github.malyszaryczlowiek
package kessengerlibrary.serdes.user

import kessengerlibrary.model.User.decoder
import kessengerlibrary.model.User

import io.circe.parser.decode
import org.apache.kafka.common.serialization.Deserializer

import java.util.UUID


class UserDeserializer extends Deserializer[User] {

  override def deserialize(topic: String, data: Array[Byte]): User =
    decode[User](new String(data)) match {
      case Left(_)            => User.nullUser
      case Right(user: User)  => user
    }

}
