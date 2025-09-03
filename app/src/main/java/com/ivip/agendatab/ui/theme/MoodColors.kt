package com.ivip.agendatab.ui.theme

import androidx.compose.ui.graphics.Color
import com.ivip.agendatab.domain.model.Mood

object MoodColors {
    val HAPPY = Color(0xFF4CAF50)
    val CALM = Color(0xFF2196F3)
    val ANXIOUS = Color(0xFFFF9800)
    val DEPRESSED = Color(0xFFF44336)
    val DEFAULT = Color(0xFFBDBDBD) // Cinza mais escuro e visível

    fun getMoodColor(mood: Mood?): Color {
        return when (mood) {
            Mood.HAPPY -> HAPPY
            Mood.CALM -> CALM
            Mood.ANXIOUS -> ANXIOUS
            Mood.DEPRESSED -> DEPRESSED
            null -> DEFAULT
        }
    }

    fun getMoodEmoji(mood: Mood): String {
        return when (mood) {
            Mood.HAPPY -> "😊"
            Mood.CALM -> "😌"
            Mood.ANXIOUS -> "😰"
            Mood.DEPRESSED -> "😔"
        }
    }

    fun getAllMoodColors(): List<Pair<Mood, Color>> {
        return listOf(
            Mood.HAPPY to HAPPY,
            Mood.CALM to CALM,
            Mood.ANXIOUS to ANXIOUS,
            Mood.DEPRESSED to DEPRESSED
        )
    }
}