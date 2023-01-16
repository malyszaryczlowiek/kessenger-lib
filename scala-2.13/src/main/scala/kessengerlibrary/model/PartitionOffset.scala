package io.github.malyszaryczlowiek
package kessengerlibrary.model

import io.circe.Decoder.Result
import io.circe.parser.decode
import io.circe.{Decoder, Encoder, Error, HCursor, Json}


case class PartitionOffset(partition: Int, offset: Long)

object PartitionOffset {
  implicit object decoder extends Decoder[PartitionOffset] {
    override def apply(c: HCursor): Result[PartitionOffset] = {
      for {
        partition <- c.downField("partition").as[Int]
        offset    <- c.downField("offset").as[Long]
      } yield {
        PartitionOffset(partition, offset)
      }
    }
  }

  implicit object encoder extends Encoder[PartitionOffset] {
    override def apply(a: PartitionOffset): Json = {
      Json.obj(
        ("partition",   Json.fromInt(a.partition)),
        ("offset",      Json.fromLong(a.offset))
      )
    }
  }
}
