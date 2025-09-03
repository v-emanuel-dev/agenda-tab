package com.ivip.agendatab.ui.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ivip.agendatab.R
import com.ivip.agendatab.domain.model.DailyEntry
import com.ivip.agendatab.ui.theme.MoodColors
import java.time.LocalDate

@Composable
fun DayCell(
    date: LocalDate,
    entry: DailyEntry?,
    isCurrentMonth: Boolean,
    onClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val backgroundColor = if (entry != null) {
        MoodColors.getMoodColor(entry.mood)
    } else {
        MoodColors.DEFAULT
    }

    val textColor = if (isCurrentMonth) {
        if (entry != null) Color.White else MaterialTheme.colorScheme.onSurface
    } else {
        Color.Gray
    }

    val moodDescription = if (entry != null) {
        when (entry.mood.name) {
            "HAPPY" -> context.getString(R.string.mood_happy)
            "CALM" -> context.getString(R.string.mood_calm)
            "ANXIOUS" -> context.getString(R.string.mood_anxious)
            "DEPRESSED" -> context.getString(R.string.mood_depressed)
            else -> context.getString(R.string.no_mood_selected)
        }
    } else {
        context.getString(R.string.no_mood_selected)
    }

    Box(
        modifier = modifier
            .size(48.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable { onClick(date) }
            .semantics {
                contentDescription = context.getString(
                    R.string.day_mood_description,
                    date.dayOfMonth,
                    moodDescription
                )
            },
        contentAlignment = Alignment.Center
    ) {
        if (entry != null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = MoodColors.getMoodEmoji(entry.mood),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Text(
                    text = date.dayOfMonth.toString(),
                    color = textColor,
                    fontSize = 12.sp,
                    fontWeight = if (date == LocalDate.now()) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
        } else {
            Text(
                text = date.dayOfMonth.toString(),
                color = textColor,
                fontSize = 16.sp,
                fontWeight = if (date == LocalDate.now()) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}