package com.ivip.agendatab.ui.calendar.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ivip.agendatab.R
import com.ivip.agendatab.domain.model.DailyEntry
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*

@Composable
fun CalendarGrid(
    currentMonth: YearMonth,
    dailyEntries: Map<LocalDate, DailyEntry>,
    onDayClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val firstDayOfMonth = currentMonth.atDay(1)
    val lastDayOfMonth = currentMonth.atEndOfMonth()

    val firstWeek = firstDayOfMonth.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear())
    val lastWeek = lastDayOfMonth.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear())

    val startDate = firstDayOfMonth.minusDays(firstDayOfMonth.dayOfWeek.value.toLong() - 1)
    val endDate = lastDayOfMonth.plusDays(7 - lastDayOfMonth.dayOfWeek.value.toLong())

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val daysOfWeek = listOf(
                stringResource(R.string.monday_short),
                stringResource(R.string.tuesday_short),
                stringResource(R.string.wednesday_short),
                stringResource(R.string.thursday_short),
                stringResource(R.string.friday_short),
                stringResource(R.string.saturday_short),
                stringResource(R.string.sunday_short)
            )
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        var currentDate = startDate
        while (currentDate <= endDate) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(7) { dayOfWeek ->
                    val date = currentDate.plusDays(dayOfWeek.toLong())
                    val isCurrentMonth = date.month == currentMonth.month
                    val entry = dailyEntries[date]

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        if (date <= endDate) {
                            DayCell(
                                date = date,
                                entry = entry,
                                isCurrentMonth = isCurrentMonth,
                                onClick = onDayClick
                            )
                        }
                    }
                }
            }
            currentDate = currentDate.plusWeeks(1)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}