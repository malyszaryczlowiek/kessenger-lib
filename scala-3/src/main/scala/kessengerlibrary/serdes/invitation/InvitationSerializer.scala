package io.github.malyszaryczlowiek
package kessengerlibrary.serdes.invitation

import io.github.malyszaryczlowiek.kessengerlibrary.model.Invitation.toKafkaJSON

import io.circe.syntax.*
import io.github.malyszaryczlowiek.kessengerlibrary.model.Invitation
import org.apache.kafka.common.serialization.Serializer

class InvitationSerializer extends Serializer[Invitation] {

  override def serialize(topic: String, data: Invitation): Array[Byte] =
    toKafkaJSON(data).getBytes

}
