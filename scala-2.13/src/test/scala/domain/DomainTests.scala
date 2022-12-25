package io.github.malyszaryczlowiek
package domain

import io.github.malyszaryczlowiek.kessengerlibrary.model.User.{parseJSONtoListOfUsers, toJSON}
import io.github.malyszaryczlowiek.kessengerlibrary.model.User

import java.nio.charset.Charset
import java.time.ZoneId
import java.util.UUID

class DomainTests extends munit.FunSuite {



  test("Parsing and deparsing list of userse") {
    val u1 = User(UUID.randomUUID(), "u1")
    val u2 = User(UUID.randomUUID(), "u2")
    val l  = List(u1,u2)
    println(l)
    val json = toJSON(l)
    parseJSONtoListOfUsers(json) match {
      case Left(_) => throw new Exception("bad")
      case Right(l2) =>
        println(l2)
        assert(l == l2)
    }
  }


  test("zones") {
    val arr = ZoneId.getAvailableZoneIds.toArray.filter( zone => zone.asInstanceOf[String].contains("UTC"))
    println()
    println( ZoneId.of("UTC"))
  }

  test("zones 2") {
    ZoneId.getAvailableZoneIds
      .toArray
      .map(zone => zone.asInstanceOf[String] )
      .sorted
      .foreach(z => println(s"\'$z\',"))
  }




  test("") {
    Charset.availableCharsets().keySet().forEach(charset => println(charset))
  }





}
