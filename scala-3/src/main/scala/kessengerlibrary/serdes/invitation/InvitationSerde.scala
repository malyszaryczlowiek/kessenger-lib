package io.github.malyszaryczlowiek
package kessengerlibrary.serdes.invitation

import io.github.malyszaryczlowiek.kessengerlibrary.model.Invitation
import org.apache.kafka.common.serialization.{Deserializer, Serde, Serializer}

class InvitationSerde extends Serde[Invitation] {

  override def serializer(): Serializer[Invitation] =
    new InvitationSerializer

  override def deserializer(): Deserializer[Invitation] =
    new InvitationDeserializer
}