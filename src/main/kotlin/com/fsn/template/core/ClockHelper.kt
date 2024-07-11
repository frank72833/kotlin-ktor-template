package com.fsn.template.core

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun now(): Instant = Clock.System.now()

fun localDateTimeUtcNow(): LocalDateTime = now().toLocalDateTime(TimeZone.UTC)
