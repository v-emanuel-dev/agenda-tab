package com.ivip.agendatab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.ivip.agendatab.di.DatabaseModule
import com.ivip.agendatab.ui.navigation.AppNavigation
import com.ivip.agendatab.ui.theme.AgendaTabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = DatabaseModule.provideDatabase(this)
        val dao = DatabaseModule.provideDailyEntryDao(database)
        val repository = DatabaseModule.provideMoodRepository(dao)

        val getDailyEntriesUseCase = DatabaseModule.provideGetDailyEntriesUseCase(repository)
        val saveDailyEntryUseCase = DatabaseModule.provideSaveDailyEntryUseCase(repository)
        val deleteDailyEntryUseCase = DatabaseModule.provideDeleteDailyEntryUseCase(repository)

        val calendarViewModel = DatabaseModule.provideCalendarViewModel(
            getDailyEntriesUseCase,
            saveDailyEntryUseCase,
            deleteDailyEntryUseCase
        )

        setContent {
            AgendaTabTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(calendarViewModel = calendarViewModel)
                }
            }
        }
    }
}