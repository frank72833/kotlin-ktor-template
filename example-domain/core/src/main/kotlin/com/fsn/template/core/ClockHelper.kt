package com.fsn.template.core

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset.UTC

fun now(): Instant = Instant.now()

fun localDateTimeUtcNow(): LocalDateTime = LocalDateTime.ofInstant(now(), UTC)
