package io.github.malyszaryczlowiek
package kessengerlibrary.model

import io.circe.syntax._
import io.circe.{Encoder, Json}



case class ResponseBody(num: Int, message: String) {
  override def toString: String = this.copy().asJson(ResponseBody.encoder).noSpaces
}

object  ResponseBody {

  implicit object encoder extends Encoder[ResponseBody] {
    override def apply(a: ResponseBody): Json = {
      Json.obj(
        ("num", Json.fromInt(a.num)),
        ("message", Json.fromString(a.message)),
      )
    }
  }

}