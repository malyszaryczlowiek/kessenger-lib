package io.github.malyszaryczlowiek
package kessengerlibrary.util

import java.time.*

/**
 *
 * // https://docs.oracle.com/javase/tutorial/datetime/iso/timezones.html
 */
object TimeConverter:

  def fromMilliSecondsToLocal(milliSeconds: Long): LocalDateTime =
    LocalDateTime.ofInstant(Instant.ofEpochSecond(milliSeconds / 1000L), ZoneId.systemDefault())


  def fromMilliSecondsToLocalInZoneId(milliSeconds: Long, zoneId: ZoneId): LocalDateTime =
    LocalDateTime.ofInstant(Instant.ofEpochSecond(milliSeconds / 1000L), zoneId)


  def fromLocalToEpochTime(local: LocalDateTime): Long =
    ZonedDateTime.ofLocal(local, ZoneId.systemDefault(), ZoneOffset.UTC).toEpochSecond * 1000L



  def fromLocalWithZoneIdToEpochTime(local: LocalDateTime, zoneId: ZoneId): Long =
    ZonedDateTime.ofLocal(local, zoneId, ZoneOffset.UTC).toEpochSecond * 1000L
