package io.github.malyszaryczlowiek
package kessengerlibrary.kafka.configurators

class KafkaProductionConfigurator extends KafkaConfigurator {
  override def SERVERS: String = "localhost:9093,localhost:9094,localhost:9095"

  // topics
  // in production each topic has three partitions,
  // and is replicated three times,
  // so we can create three kafka brokers max.
  override def CHAT_TOPIC_REPLICATION_FACTOR: Short = 3
  override def JOINING_TOPIC_REPLICATION_FACTOR: Short = 3

}



