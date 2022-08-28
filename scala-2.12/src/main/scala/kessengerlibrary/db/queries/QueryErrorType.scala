package io.github.malyszaryczlowiek
package kessengerlibrary.db.queries

sealed trait QueryErrorType
case object WARNING     extends QueryErrorType
case object ERROR       extends QueryErrorType
case object FATAL_ERROR extends QueryErrorType

