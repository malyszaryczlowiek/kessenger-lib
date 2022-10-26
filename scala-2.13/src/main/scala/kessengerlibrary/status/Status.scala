package io.github.malyszaryczlowiek
package kessengerlibrary.status

sealed trait Status
case object NotInitialized
case object Starting
case object Running
case object Error
case object Closing
case object Terminated
