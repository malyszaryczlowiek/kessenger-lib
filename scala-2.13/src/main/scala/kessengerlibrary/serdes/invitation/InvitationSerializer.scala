package io.github.malyszaryczlowiek
package kessengerlibrary.serdes.invitation

import org.apache.kafka.common.serialization.Serializer
import kessengerlibrary.model.Invitation
import kessengerlibrary.model.Invitation.toKafkaJSON

class InvitationSerializer extends Serializer[Invitation] {

  override def serialize(topic: String, data: Invitation): Array[Byte] =
    toKafkaJSON(data).getBytes

}
