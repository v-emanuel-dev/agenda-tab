package com.ivip.agendatab.data.local.database

import androidx.room.*
import com.ivip.agendatab.data.local.entities.DailyEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyEntryDao {

    @Query("SELECT * FROM daily_entries WHERE date = :date")
    suspend fun getByDate(date: String): DailyEntryEntity?

    @Query("SELECT * FROM daily_entries WHERE date BETWEEN :startDate AND :endDate")
    fun getEntriesInRange(startDate: String, endDate: String): Flow<List<DailyEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(entry: DailyEntryEntity)

    @Query("DELETE FROM daily_entries WHERE date = :date")
    suspend fun deleteByDate(date: String)

    @Query("SELECT * FROM daily_entries")
    fun getAllEntries(): Flow<List<DailyEntryEntity>>
}