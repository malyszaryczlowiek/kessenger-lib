package io.github.malyszaryczlowiek
package kessengerlibrary.env

sealed trait Environment
case object Prod extends Environment
case object Test extends Environment