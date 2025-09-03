package com.ivip.agendatab.domain.repository

import com.ivip.agendatab.domain.model.DailyEntry
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface MoodRepository {
    suspend fun getDailyEntry(date: LocalDate): DailyEntry?
    fun getDailyEntriesInRange(startDate: LocalDate, endDate: LocalDate): Flow<List<DailyEntry>>
    suspend fun saveDailyEntry(entry: DailyEntry)
    suspend fun deleteDailyEntry(date: LocalDate)
}