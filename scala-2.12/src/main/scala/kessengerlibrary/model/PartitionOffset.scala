package io.github.malyszaryczlowiek
package kessengerlibrary.model

import io.circe.Decoder.Result
import io.circe.{Decoder, HCursor}


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
}
