package io.github.malyszaryczlowiek
package kessengerlibrary.kafka


case class TopicSetup(name: String, servers: String, partitionNumber: Int, replicationFactor: Short, otherConfig: Map[String, String])
