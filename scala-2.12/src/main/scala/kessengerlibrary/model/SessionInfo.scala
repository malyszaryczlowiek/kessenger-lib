package io.github.malyszaryczlowiek
package kessengerlibrary.model

import java.util.UUID

case class SessionInfo(sessionId: UUID, userId: UUID, validityTime: Long)
