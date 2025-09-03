package com.ivip.agendatab.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ivip.agendatab.domain.model.Mood

@Entity(tableName = "daily_entries")
data class DailyEntryEntity(
    @PrimaryKey val date: String,
    val mood: String,
    val note: String
) {
    fun toDomainModel(): com.ivip.agendatab.domain.model.DailyEntry {
        return com.ivip.agendatab.domain.model.DailyEntry(
            date = java.time.LocalDate.parse(date),
            mood = Mood.valueOf(mood),
            note = note
        )
    }

    companion object {
        fun fromDomainModel(entry: com.ivip.agendatab.domain.model.DailyEntry): DailyEntryEntity {
            return DailyEntryEntity(
                date = entry.date.toString(),
                mood = entry.mood.name,
                note = entry.note
            )
        }
    }
}