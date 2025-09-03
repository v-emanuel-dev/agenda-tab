package com.ivip.agendatab.di

import android.content.Context
import com.ivip.agendatab.data.local.database.AppDatabase
import com.ivip.agendatab.data.local.database.DailyEntryDao
import com.ivip.agendatab.data.repository.MoodRepositoryImpl
import com.ivip.agendatab.domain.repository.MoodRepository
import com.ivip.agendatab.domain.usecase.DeleteDailyEntryUseCase
import com.ivip.agendatab.domain.usecase.GetDailyEntriesUseCase
import com.ivip.agendatab.domain.usecase.SaveDailyEntryUseCase
import com.ivip.agendatab.ui.calendar.CalendarViewModel

object DatabaseModule {

    fun provideDatabase(context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    fun provideDailyEntryDao(database: AppDatabase): DailyEntryDao {
        return database.dailyEntryDao()
    }

    fun provideMoodRepository(dao: DailyEntryDao): MoodRepository {
        return MoodRepositoryImpl(dao)
    }

    fun provideGetDailyEntriesUseCase(repository: MoodRepository): GetDailyEntriesUseCase {
        return GetDailyEntriesUseCase(repository)
    }

    fun provideSaveDailyEntryUseCase(repository: MoodRepository): SaveDailyEntryUseCase {
        return SaveDailyEntryUseCase(repository)
    }

    fun provideDeleteDailyEntryUseCase(repository: MoodRepository): DeleteDailyEntryUseCase {
        return DeleteDailyEntryUseCase(repository)
    }

    fun provideCalendarViewModel(
        getDailyEntriesUseCase: GetDailyEntriesUseCase,
        saveDailyEntryUseCase: SaveDailyEntryUseCase,
        deleteDailyEntryUseCase: DeleteDailyEntryUseCase,
        context: Context
    ): CalendarViewModel {
        return CalendarViewModel(
            getDailyEntriesUseCase,
            saveDailyEntryUseCase,
            deleteDailyEntryUseCase,
            context
        )
    }
}