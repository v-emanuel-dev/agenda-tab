package com.ivip.agendatab.domain.usecase

import com.ivip.agendatab.domain.repository.MoodRepository
import java.time.LocalDate

class DeleteDailyEntryUseCase(
    private val repository: MoodRepository
) {
    suspend operator fun invoke(date: LocalDate) {
        repository.deleteDailyEntry(date)
    }
}