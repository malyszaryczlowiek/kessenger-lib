package io.github.malyszaryczlowiek
package kessengerlibrary.serdes

import kessengerlibrary.domain.User
import kessengerlibrary.domain.User.given
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
