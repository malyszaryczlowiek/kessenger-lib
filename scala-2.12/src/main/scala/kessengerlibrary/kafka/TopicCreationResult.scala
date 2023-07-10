package io.github.malyszaryczlowiek
package kessengerlibrary.kafka

sealed trait TopicCreationResult
case object Done extends TopicCreationResult
case class Error(error: String) extends TopicCreationResult
