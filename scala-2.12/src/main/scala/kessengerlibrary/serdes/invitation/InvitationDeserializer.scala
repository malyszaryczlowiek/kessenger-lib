package io.github.malyszaryczlowiek
package kessengerlibrary.serdes.invitation

import io.circe.parser.decode
import io.github.malyszaryczlowiek.kessengerlibrary.model.Invitation
import org.apache.kafka.common.serialization.Deserializer

class InvitationDeserializer extends Deserializer[Invitation] {

  override def deserialize(topic: String, data: Array[Byte]): Invitation =
    decode[Invitation](new String(data)) match {
      case Left(_)               => Invitation.nullInvitation
      case Right(i: Invitation)  => i
    }

}