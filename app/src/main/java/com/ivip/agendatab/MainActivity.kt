package com.ivip.agendatab

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.ivip.agendatab.di.DatabaseModule
import com.ivip.agendatab.ui.navigation.AppNavigation
import com.ivip.agendatab.ui.theme.AgendaTabTheme
import com.ivip.agendatab.ui.theme.ThemeManager
import java.util.*

class MainActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(updateBaseContextLocale(newBase))
    }

    private fun updateBaseContextLocale(context: Context): Context {
        val locale = Locale("pt", "BR")
        Locale.setDefault(locale)

        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        return context.createConfigurationContext(configuration)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ThemeManager
        ThemeManager.initialize(this)

        val database = DatabaseModule.provideDatabase(this)
        val dao = DatabaseModule.provideDailyEntryDao(database)
        val repository = DatabaseModule.provideMoodRepository(dao)

        val getDailyEntriesUseCase = DatabaseModule.provideGetDailyEntriesUseCase(repository)
        val saveDailyEntryUseCase = DatabaseModule.provideSaveDailyEntryUseCase(repository)
        val deleteDailyEntryUseCase = DatabaseModule.provideDeleteDailyEntryUseCase(repository)

        val calendarViewModel = DatabaseModule.provideCalendarViewModel(
            getDailyEntriesUseCase,
            saveDailyEntryUseCase,
            deleteDailyEntryUseCase,
            this
        )

        setContent {
            val themeMode by ThemeManager.themeMode.collectAsState()

            AgendaTabTheme(themeMode = themeMode) {
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