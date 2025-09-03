package com.ivip.agendatab.domain.model

import java.time.LocalDate

data class DailyEntry(
    val date: LocalDate,
    val mood: Mood,
    val note: String
)