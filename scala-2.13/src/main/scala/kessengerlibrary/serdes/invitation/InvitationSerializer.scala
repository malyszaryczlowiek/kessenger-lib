package io.github.malyszaryczlowiek
package kessengerlibrary.serdes.invitation

import org.apache.kafka.common.serialization.Serializer
import io.circe.syntax._
import io.github.malyszaryczlowiek.kessengerlibrary.model.Invitation
import io.github.malyszaryczlowiek.kessengerlibrary.model.Invitation.toKafkaJSON

class InvitationSerializer extends Serializer[Invitation] {

  override def serialize(topic: String, data: Invitation): Array[Byte] =
    toKafkaJSON(data).getBytes

}
