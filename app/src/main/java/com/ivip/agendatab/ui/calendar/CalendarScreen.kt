package com.ivip.agendatab.ui.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ivip.agendatab.ui.calendar.components.CalendarGrid
import com.ivip.agendatab.ui.calendar.components.MoodLegend
import com.ivip.agendatab.ui.calendar.components.WeeklyOverview
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel,
    onNavigateToEdit: (LocalDate) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header with month navigation
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    viewModel.onMonthChanged(uiState.currentMonth.minusMonths(1))
                }
            ) {
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Previous month")
            }

            Text(
                text = uiState.currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            IconButton(
                onClick = {
                    viewModel.onMonthChanged(uiState.currentMonth.plusMonths(1))
                }
            ) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Next month")
            }
        }

        // Main calendar grid
        CalendarGrid(
            currentMonth = uiState.currentMonth,
            dailyEntries = uiState.dailyEntries,
            onDayClick = onNavigateToEdit,
            modifier = Modifier.fillMaxWidth()
        )

        // Weekly overview section
        WeeklyOverview(
            dailyEntries = uiState.dailyEntries,
            onDayClick = onNavigateToEdit,
            modifier = Modifier.fillMaxWidth()
        )

        // Mood legend
        MoodLegend()

        // Loading indicator
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }

    // Error handling
    uiState.errorMessage?.let { message ->
        LaunchedEffect(message) {
            viewModel.onErrorDismissed()
        }
    }
}