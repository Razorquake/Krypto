package com.razorquake.krypto.data.remote.formatters

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

fun getLocalizedDateTime(date: String): String {
    val instant = Instant.parse(date)
    val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
    return localDateTime.format(formatter)
}