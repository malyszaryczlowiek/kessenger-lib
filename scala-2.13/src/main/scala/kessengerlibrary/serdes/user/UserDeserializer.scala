package io.github.malyszaryczlowiek
package kessengerlibrary.serdes.user

import kessengerlibrary.model.User.parseUser
import kessengerlibrary.model.User
import org.apache.kafka.common.serialization.Deserializer



class UserDeserializer extends Deserializer[User] {

  override def deserialize(topic: String, data: Array[Byte]): User = {
    parseUser(new String(data)) match {
      case Left(_) => User.nullUser
      case Right(user:User) => user
    }
  }

}
