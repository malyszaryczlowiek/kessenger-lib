package io.github.malyszaryczlowiek
package kessengerlibrary.kafka.errors

sealed class KafkaErrorMessage(mes: String) {
  override def toString: String = mes
}
case object ChatExistsError extends KafkaErrorMessage("Chat already exists Error. ")
case object TimeoutError    extends KafkaErrorMessage("Timeout Server Error. ")
case object UndefinedError  extends KafkaErrorMessage("Undefined Error. ")
case object InternalError   extends KafkaErrorMessage("Internal Error. ")
case object ServerError     extends KafkaErrorMessage("Server Error. ")
