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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    val moodDescription = entry?.mood?.name?.lowercase() ?: "no mood selected"

    Box(
        modifier = modifier
            .size(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable { onClick(date) }
            .semantics {
                contentDescription = "Day ${date.dayOfMonth}, $moodDescription"
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (entry != null) {
                Text(
                    text = MoodColors.getMoodEmoji(entry.mood),
                    fontSize = 16.sp
                )
                Text(
                    text = date.dayOfMonth.toString(),
                    color = textColor,
                    fontSize = 10.sp,
                    fontWeight = if (date == LocalDate.now()) FontWeight.Bold else FontWeight.Normal
                )
            } else {
                Text(
                    text = date.dayOfMonth.toString(),
                    color = textColor,
                    fontSize = 14.sp,
                    fontWeight = if (date == LocalDate.now()) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}