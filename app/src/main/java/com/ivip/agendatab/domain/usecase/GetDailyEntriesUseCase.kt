package com.ivip.agendatab.domain.usecase

import com.ivip.agendatab.domain.model.DailyEntry
import com.ivip.agendatab.domain.repository.MoodRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class GetDailyEntriesUseCase(
    private val repository: MoodRepository
) {
    operator fun invoke(startDate: LocalDate, endDate: LocalDate): Flow<List<DailyEntry>> {
        return repository.getDailyEntriesInRange(startDate, endDate)
    }
}