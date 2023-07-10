package io.github.malyszaryczlowiek
package kessengerlibrary.kafka

import kessengerlibrary.kafka.errors.{KafkaError, KafkaErrorsHandler}
import kessengerlibrary.model.Chat

import org.apache.kafka.clients.admin.{Admin, CreateTopicsResult, NewTopic}
import org.apache.kafka.common.KafkaFuture
import org.apache.kafka.streams.StreamsConfig

import java.util.Properties
import scala.jdk.javaapi.CollectionConverters
import scala.util.{Failure, Success, Using}


class TopicCreator

object TopicCreator {



  /**
   *
   * @param topicName
   */
  def createTopic(setup: TopicSetup): TopicCreationResult = {

    val adminProperties: Properties = new Properties()
    adminProperties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, setup.servers)

    Using(Admin.create(adminProperties)) {
      admin =>
        val chatConfig: java.util.Map[String, String] = CollectionConverters.asJava( setup.otherConfig )
        // we create  topic
        val result: CreateTopicsResult = admin.createTopics(
          java.util.Collections.singletonList(
            new NewTopic(setup.name, setup.partitionNumber, setup.replicationFactor).configs(chatConfig)
          )
        )

        // extract task of topic creation
        val talkFuture: KafkaFuture[Void] = result.values().get(setup.name)

        // we wait patiently to create topic or get error.
        talkFuture.get //(5L, TimeUnit.SECONDS)

        // simply return topic name as proof of creation
        setup.name
    } match {
      case Failure(ex) =>
        KafkaErrorsHandler.handleWithErrorMessage[Chat](ex) match {
          case Left(kafkaError: KafkaError) =>
            Error(s"Cannot create topic '${setup.name}'. ${kafkaError.description}")
          case Right(_) => // not reachable
            Error(s"Undefined topic creation error.")
        }
      case Success(topic) => Done
    }
  }

}
