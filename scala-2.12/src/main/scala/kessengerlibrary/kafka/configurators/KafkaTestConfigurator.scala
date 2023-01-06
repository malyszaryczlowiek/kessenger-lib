package io.github.malyszaryczlowiek
package kessengerlibrary.kafka.configurators

class KafkaTestConfigurator extends KafkaConfigurator {
  override def EXTERNAL_SERVERS: String = "localhost:9093" // in test environment we have only one kafka broker

  override def INTERNAL_SERVERS: String = "kafka1:9092"

  // topics
  override def CHAT_TOPIC_REPLICATION_FACTOR: Short = 1
  override def JOINING_TOPIC_REPLICATION_FACTOR: Short = 1
  override def WRITING_TOPIC_REPLICATION_FACTOR: Short = 1
}
