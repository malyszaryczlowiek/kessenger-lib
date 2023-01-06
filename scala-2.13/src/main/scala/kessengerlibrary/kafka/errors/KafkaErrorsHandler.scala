package io.github.malyszaryczlowiek
package kessengerlibrary.kafka.errors

import org.apache.kafka.common.KafkaException
import org.apache.kafka.common.errors._


object KafkaErrorsHandler {

  private val internalError   = Left(KafkaError(FatalError, InternalError  ))
  private val chatExistsError = Left(KafkaError(Error,    ChatExistsError))
  private val serverError     = Left(KafkaError(FatalError, ServerError    ))
  private val undefinedErr    = Left(KafkaError(FatalError, UndefinedError ))


  def handleWithErrorMessage[A](ex: Throwable): Either[KafkaError, A] = {
    val message = ex.getMessage // in some exceptions message may be null
    if (message != null)  {
      // println(s"Test ERROR:${ex.getClass}, message: $message") // TODO delete, sometimes used in integration tests
      val isInternal: Boolean =
        message.contains("InvalidOffsetException") ||
          message.contains("WakeupException") ||
          message.contains("InterruptException") ||
          message.contains("AuthenticationException") ||
          message.contains("AuthorizationException") ||
          message.contains("IllegalArgumentException") ||
          message.contains("IllegalStateException") ||
          message.contains("ArithmeticException") ||
          message.contains("InvalidTopicException")

      val isServerError =
        message.contains("UnsupportedVersionException") ||
          message.contains("TimeoutException")

      val exists = message.contains("TopicExistsException")


      if ( isInternal )  internalError
      else if ( exists ) chatExistsError
      else if (isServerError) serverError
      else undefinedErr
    } else
     serverError
  }


  @deprecated(message = "Marked as deprecated due to not proper work. " +
    "Should use KafkaErrorsHandler.handleWithErrorMessage[A](ex: Throwable) instead.")
  def handleWithErrorType[A, E <: Throwable](ex: E): Either[KafkaError, A] =
    try {
      throw ex
    }
    catch {
      case e: InvalidOffsetException      => internalError
      case e: WakeupException             => internalError
      case e: InterruptException          => internalError
      case e: AuthenticationException     => internalError
      case e: AuthorizationException      => internalError
      case e: IllegalArgumentException    => internalError
      case e: IllegalStateException       => internalError
      case e: ArithmeticException         => internalError
      case e: InvalidTopicException       => internalError
      case e: TopicExistsException        => serverError
      case e: UnsupportedVersionException => serverError
      case e: KafkaException              => undefinedErr
      case e: Throwable                   => undefinedErr
    }

  @deprecated(message = "Marked as deprecated due to not proper work. " +
    "Should use KafkaErrorsHandler.handleWithErrorMessage[A](ex: Throwable) instead.")
  def handleThrowable[A](ex: Throwable): Either[KafkaError, A] =
    try {
      throw ex
    }
    catch {
      case e: InvalidOffsetException      => internalError
      case e: WakeupException             => internalError
      case e: InterruptException          => internalError
      case e: AuthenticationException     => internalError
      case e: AuthorizationException      => internalError
      case e: IllegalArgumentException    => internalError
      case e: IllegalStateException       => internalError
      case e: ArithmeticException         => internalError
      case e: InvalidTopicException       => internalError
      case e: TopicExistsException        => serverError
      case e: UnsupportedVersionException => serverError
      case e: KafkaException              => undefinedErr
      case e: Throwable                   => undefinedErr
    }

}




