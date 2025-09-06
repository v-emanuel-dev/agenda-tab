package com.ivip.agendatab.ui.calendar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ivip.agendatab.R
import com.ivip.agendatab.ui.calendar.components.CalendarGrid
import com.ivip.agendatab.ui.calendar.components.MoodLegend
import com.ivip.agendatab.ui.calendar.components.WeeklyOverview
import com.ivip.agendatab.ui.components.ThemeSelector
import com.ivip.agendatab.ui.components.DatePickerDialog
import com.ivip.agendatab.ui.components.TutorialHintCard
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel,
    onNavigateToEdit: (LocalDate) -> Unit,
    onShowWelcome: (() -> Unit)? = null, // Callback para mostrar a tela de boas-vindas
    showTutorialHint: Boolean = false // Se deve mostrar o card de dica
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showThemeSelector by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showHintCard by remember { mutableStateOf(showTutorialHint) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header with month navigation and action buttons
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
                Icon(
                    Icons.Default.KeyboardArrowLeft,
                    contentDescription = stringResource(R.string.previous_month)
                )
            }

            Text(
                text = uiState.currentMonth.format(
                    DateTimeFormatter.ofPattern(
                        stringResource(R.string.date_format_month_year),
                        Locale.getDefault()
                    )
                ),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { showDatePicker = true }
            )

            Row {
                // Botão para mostrar tutorial/boas-vindas
                if (onShowWelcome != null) {
                    IconButton(
                        onClick = onShowWelcome
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = "Mostrar Tutorial",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Botão de configurações
                IconButton(
                    onClick = { showThemeSelector = true }
                ) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = stringResource(R.string.theme_settings)
                    )
                }

                IconButton(
                    onClick = {
                        viewModel.onMonthChanged(uiState.currentMonth.plusMonths(1))
                    }
                ) {
                    Icon(
                        Icons.Default.KeyboardArrowRight,
                        contentDescription = stringResource(R.string.next_month)
                    )
                }
            }
        }

        // Tutorial hint card (opcional)
        if (showHintCard && onShowWelcome != null) {
            TutorialHintCard(
                onShowTutorial = {
                    showHintCard = false
                    onShowWelcome()
                },
                onDismiss = { showHintCard = false }
            )
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

    // Theme selector modal
    if (showThemeSelector) {
        ThemeSelector(
            onDismiss = { showThemeSelector = false }
        )
    }

    // Date picker modal
    if (showDatePicker) {
        DatePickerDialog(
            initialDate = uiState.currentMonth,
            onDateSelected = { selectedMonth ->
                viewModel.onMonthChanged(selectedMonth)
            },
            onDismiss = { showDatePicker = false }
        )
    }

    // Error handling
    uiState.errorMessage?.let { message ->
        LaunchedEffect(message) {
            viewModel.onErrorDismissed()
        }
    }
}