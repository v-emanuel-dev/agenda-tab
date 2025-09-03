package com.ivip.agendatab.domain.usecase

import com.ivip.agendatab.domain.model.DailyEntry
import com.ivip.agendatab.domain.repository.MoodRepository

class SaveDailyEntryUseCase(
    private val repository: MoodRepository
) {
    suspend operator fun invoke(entry: DailyEntry) {
        repository.saveDailyEntry(entry)
    }
}