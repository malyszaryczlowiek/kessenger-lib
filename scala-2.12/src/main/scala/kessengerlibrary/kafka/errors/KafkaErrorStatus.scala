package io.github.malyszaryczlowiek
package kessengerlibrary.kafka.errors


sealed class KafkaErrorStatus(mes: String) {
  override def toString: String = mes
}
case object Warning    extends KafkaErrorStatus("Warning! ")
case object Error      extends KafkaErrorStatus("Error! ")
case object FatalError extends KafkaErrorStatus("Fatal Error! ")




