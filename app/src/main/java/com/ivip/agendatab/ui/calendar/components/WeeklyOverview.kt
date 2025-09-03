package com.ivip.agendatab.ui.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ivip.agendatab.R
import com.ivip.agendatab.domain.model.DailyEntry
import com.ivip.agendatab.domain.model.Mood
import com.ivip.agendatab.ui.theme.MoodColors
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@Composable
fun WeeklyOverview(
    dailyEntries: Map<LocalDate, DailyEntry>,
    onDayClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val today = LocalDate.now()

    // Calcula o início da semana (segunda-feira)
    val startOfWeek = today.minusDays((today.dayOfWeek.value - 1).toLong())

    // Gera os 7 dias da semana atual
    val currentWeek = (0..6).map { startOfWeek.plusDays(it.toLong()) }

    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.this_week),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.heightIn(max = 400.dp)
        ) {
            items(currentWeek) { date ->
                WeekDayFullCard(
                    date = date,
                    entry = dailyEntries[date],
                    isToday = date == today,
                    onClick = { onDayClick(date) }
                )
            }
        }
    }
}

@Composable
private fun WeekDayFullCard(
    date: LocalDate,
    entry: DailyEntry?,
    isToday: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val backgroundColor = if (entry != null) {
        MoodColors.getMoodColor(entry.mood).copy(alpha = 0.1f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val moodColor = if (entry != null) {
        MoodColors.getMoodColor(entry.mood)
    } else {
        MaterialTheme.colorScheme.outline
    }

    val cardElevation = if (isToday) 4.dp else 1.dp

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = cardElevation),
        colors = CardDefaults.cardColors(
            containerColor = if (isToday) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        ),
        border = if (isToday) {
            androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        } else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left section: Date info
            Column(
                modifier = Modifier.weight(0.3f)
            ) {
                Text(
                    text = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = date.format(
                        DateTimeFormatter.ofPattern(
                            context.getString(R.string.date_format_day_month),
                            Locale.getDefault()
                        )
                    ),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (isToday) {
                    Text(
                        text = stringResource(R.string.today),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Center section: Mood and note
            Column(
                modifier = Modifier.weight(0.5f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (entry != null) {
                    val moodText = when (entry.mood) {
                        Mood.HAPPY -> stringResource(R.string.mood_happy)
                        Mood.CALM -> stringResource(R.string.mood_calm)
                        Mood.ANXIOUS -> stringResource(R.string.mood_anxious)
                        Mood.DEPRESSED -> stringResource(R.string.mood_depressed)
                    }

                    Text(
                        text = moodText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = moodColor
                    )
                    if (entry.note.isNotBlank()) {
                        Text(
                            text = entry.note,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                } else {
                    Text(
                        text = stringResource(R.string.no_mood_recorded),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }

            // Right section: Mood indicator
            Box(
                modifier = Modifier.weight(0.2f),
                contentAlignment = Alignment.CenterEnd
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(moodColor),
                    contentAlignment = Alignment.Center
                ) {
                    if (entry != null) {
                        Text(
                            text = MoodColors.getMoodEmoji(entry.mood),
                            fontSize = 20.sp
                        )
                    } else {
                        Text(
                            text = "❓",
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}