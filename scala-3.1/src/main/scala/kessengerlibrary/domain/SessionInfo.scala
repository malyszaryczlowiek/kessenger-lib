package io.github.malyszaryczlowiek
package kessengerlibrary.domain

import java.util.UUID

case class SessionInfo(sessionId: UUID, userId: UUID, validityTime: Long)
