package com.ivip.agendatab.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.ivip.agendatab.R
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@Composable
fun DatePickerDialog(
    initialDate: YearMonth,
    onDateSelected: (YearMonth) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var selectedYear by remember { mutableStateOf(initialDate.year) }
    var selectedMonth by remember { mutableStateOf(initialDate.monthValue) }

    val currentYear = LocalDate.now().year
    val years = (currentYear - 10..currentYear + 5).toList()
    val months = (1..12).toList()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "Selecionar Data",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Year Selector
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Ano",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        NumberSelector(
                            items = years,
                            selectedItem = selectedYear,
                            onItemSelected = { selectedYear = it },
                            displayText = { it.toString() }
                        )
                    }

                    // Month Selector
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Mês",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        NumberSelector(
                            items = months,
                            selectedItem = selectedMonth,
                            onItemSelected = { selectedMonth = it },
                            displayText = { monthNumber ->
                                java.time.Month.of(monthNumber)
                                    .getDisplayName(TextStyle.FULL, Locale.getDefault())
                            }
                        )
                    }
                }

                // Selected date preview
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "${java.time.Month.of(selectedMonth).getDisplayName(TextStyle.FULL, Locale.getDefault())} de $selectedYear",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.cancel))
                    }

                    Button(
                        onClick = {
                            onDateSelected(YearMonth.of(selectedYear, selectedMonth))
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.save))
                    }
                }
            }
        }
    }
}

@Composable
private fun <T> NumberSelector(
    items: List<T>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    displayText: (T) -> String,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Find initial scroll position
    val initialIndex = items.indexOf(selectedItem).coerceAtLeast(0)

    LaunchedEffect(selectedItem) {
        val index = items.indexOf(selectedItem)
        if (index >= 0) {
            listState.animateScrollToItem(index)
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Up arrow
        IconButton(
            onClick = {
                val currentIndex = items.indexOf(selectedItem)
                if (currentIndex > 0) {
                    onItemSelected(items[currentIndex - 1])
                }
            },
            enabled = items.indexOf(selectedItem) > 0
        ) {
            Icon(
                Icons.Default.KeyboardArrowUp,
                contentDescription = "Anterior"
            )
        }

        // Current selection display
        Surface(
            color = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = displayText(selectedItem),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Down arrow
        IconButton(
            onClick = {
                val currentIndex = items.indexOf(selectedItem)
                if (currentIndex < items.size - 1) {
                    onItemSelected(items[currentIndex + 1])
                }
            },
            enabled = items.indexOf(selectedItem) < items.size - 1
        ) {
            Icon(
                Icons.Default.KeyboardArrowDown,
                contentDescription = "Próximo"
            )
        }
    }
}