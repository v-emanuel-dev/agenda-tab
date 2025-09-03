package com.ivip.agendatab.data.repository

import com.ivip.agendatab.data.local.database.DailyEntryDao
import com.ivip.agendatab.data.local.entities.DailyEntryEntity
import com.ivip.agendatab.domain.model.DailyEntry
import com.ivip.agendatab.domain.repository.MoodRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class MoodRepositoryImpl(
    private val dao: DailyEntryDao
) : MoodRepository {

    override suspend fun getDailyEntry(date: LocalDate): DailyEntry? {
        return dao.getByDate(date.toString())?.toDomainModel()
    }

    override fun getDailyEntriesInRange(
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<DailyEntry>> {
        return dao.getEntriesInRange(
            startDate.toString(),
            endDate.toString()
        ).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun saveDailyEntry(entry: DailyEntry) {
        dao.insertOrUpdate(DailyEntryEntity.fromDomainModel(entry))
    }

    override suspend fun deleteDailyEntry(date: LocalDate) {
        dao.deleteByDate(date.toString())
    }
}