package io.github.malyszaryczlowiek
package kessengerlibrary.serdes.invitation

import org.apache.kafka.common.serialization.Deserializer
import io.circe.parser.decode
import kessengerlibrary.model.Invitation

class InvitationDeserializer extends Deserializer[Invitation] {

  override def deserialize(topic: String, data: Array[Byte]): Invitation =
    decode[Invitation](new String(data)) match {
      case Left(_)               => Invitation.nullInvitation
      case Right(i: Invitation)  => i
    }

}