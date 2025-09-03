package com.ivip.agendatab

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.*

class AgendaTabApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Define português brasileiro como idioma padrão
        val locale = Locale("pt", "BR")
        Locale.setDefault(locale)

        // Configura o AppCompatDelegate para usar pt-BR como padrão
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags("pt-BR")
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(updateBaseContextLocale(base))
    }

    private fun updateBaseContextLocale(context: Context): Context {
        val locale = Locale("pt", "BR")
        Locale.setDefault(locale)

        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        return context.createConfigurationContext(configuration)
    }
}