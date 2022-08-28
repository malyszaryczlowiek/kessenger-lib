package io.github.malyszaryczlowiek
package kessengerlibrary.domain


// earlier messages
object ChatGivens {
  implicit object OrderChatWithDate extends Ordering[Chat] {
    override def compare(x: Chat, y: Chat): Int = {
      val timeX = x.timeOfLastMessage
      val timeY = y.timeOfLastMessage
      if ( timeX.isBefore(timeY) ) -1
      else if ( timeX.isAfter(timeY) ) 1
      else 0
    }
  }


}
