package com.ivip.agendatab.ui.theme

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM
}

object ThemeManager {
    private const val THEME_PREFERENCE_KEY = "theme_mode"

    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    val themeMode: StateFlow<ThemeMode> = _themeMode

    fun initialize(context: Context) {
        val sharedPrefs = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val savedTheme = sharedPrefs.getString(THEME_PREFERENCE_KEY, ThemeMode.SYSTEM.name)
        _themeMode.value = ThemeMode.valueOf(savedTheme ?: ThemeMode.SYSTEM.name)
    }

    fun setThemeMode(context: Context, mode: ThemeMode) {
        _themeMode.value = mode
        val sharedPrefs = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        sharedPrefs.edit().putString(THEME_PREFERENCE_KEY, mode.name).apply()
    }

    @Composable
    fun getCurrentThemeMode(): ThemeMode {
        val themeMode by themeMode.collectAsState()
        return themeMode
    }
}