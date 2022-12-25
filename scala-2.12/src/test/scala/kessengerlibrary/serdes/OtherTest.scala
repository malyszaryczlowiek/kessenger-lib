package io.github.malyszaryczlowiek
package kessengerlibrary.serdes

import kessengerlibrary.kafka.errors.{FatalError, KafkaErrorStatus, Warning}

import io.github.malyszaryczlowiek.kessengerlibrary.domain.Domain
import io.github.malyszaryczlowiek.kessengerlibrary.kafka.errors

import java.util.UUID
// import kessengerlibrary.kafka.errors.statuserrors.{Error, FatalError, Warning}



class OtherTest extends munit.FunSuite {

  test("Testing inheritence of unapply method ") {
    val error: KafkaErrorStatus = Warning
  }

  test ("testing domain parsing") {
    // 2cbcf10e-1f14-4dd5-b010-956ab2cc3f86
    // 1396a2e0-dcf4-4147-b712-66e123b365fe
    val chatid = Domain.generateChatId(
      UUID.fromString("2cbcf10e-1f14-4dd5-b010-956ab2cc3f86"),
      UUID.fromString("1396a2e0-dcf4-4147-b712-66e123b365fe")
    )

    val writingId = Domain.generateWritingId(
      chatid
    )

    println(s"$writingId")


  }

}
