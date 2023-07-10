package io.github.malyszaryczlowiek
package kessengerlibrary.kafka

enum TopicCreationResult:
  case Done extends TopicCreationResult
  case Error(error: String) extends TopicCreationResult
