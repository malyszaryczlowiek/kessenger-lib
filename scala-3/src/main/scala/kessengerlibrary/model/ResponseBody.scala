package io.github.malyszaryczlowiek
package kessengerlibrary.model

import io.circe.syntax.*
import io.circe.{Encoder, Json}


case class ResponseBody(num: Int, message: String) {
  override def toString: String = this.copy().asJson(ResponseBody.encoder).noSpaces
}

object  ResponseBody {

  given encoder: Encoder[ResponseBody] = (a: ResponseBody) =>
    Json.obj(
      ("num", Json.fromInt(a.num)),
      ("message", Json.fromString(a.message)),
    )


}