package com.ivip.agendatab.ui.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ivip.agendatab.domain.model.DailyEntry
import com.ivip.agendatab.domain.model.Mood
import com.ivip.agendatab.ui.theme.MoodColors
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEntryScreen(
    date: LocalDate,
    existingEntry: DailyEntry?,
    onSave: (DailyEntry) -> Unit,
    onDelete: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var selectedMood by remember { mutableStateOf(existingEntry?.mood) }
    var noteText by remember { mutableStateOf(existingEntry?.note ?: "") }

    val backgroundColor = selectedMood?.let {
        MoodColors.getMoodColor(it).copy(alpha = 0.05f)
    } ?: MaterialTheme.colorScheme.background

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = date.format(DateTimeFormatter.ofPattern("EEEE, MMMM dd")),
                    style = MaterialTheme.typography.titleLarge
                )
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                if (existingEntry != null) {
                    IconButton(onClick = onDelete) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // Date info card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = date.format(DateTimeFormatter.ofPattern("dd")),
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = date.format(DateTimeFormatter.ofPattern("EEEE")),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = date.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Mood selection section
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "How are you feeling?",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Select the mood that best describes your day",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(Mood.values()) { mood ->
                            MoodSelectionCard(
                                mood = mood,
                                isSelected = selectedMood == mood,
                                onClick = { selectedMood = mood }
                            )
                        }
                    }

                    if (selectedMood != null) {
                        Surface(
                            color = MoodColors.getMoodColor(selectedMood!!).copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Selected: ${selectedMood!!.name.lowercase().replaceFirstChar { it.uppercase() }}",
                                modifier = Modifier.padding(12.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = MoodColors.getMoodColor(selectedMood!!)
                            )
                        }
                    }
                }
            }

            // Note section
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Tell us about your day",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Write down what made you feel this way, what happened, or any thoughts you'd like to remember",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    OutlinedTextField(
                        value = noteText,
                        onValueChange = { if (it.length <= 500) noteText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 120.dp),
                        placeholder = {
                            Text("What happened today? How are you feeling? Any thoughts or reflections...")
                        },
                        supportingText = {
                            Text("${noteText.length}/500 characters")
                        },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            // Save button
            Button(
                onClick = {
                    selectedMood?.let { mood ->
                        onSave(DailyEntry(date, mood, noteText))
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = selectedMood != null,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (existingEntry != null) "Update Entry" else "Save Entry",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            // Extra spacing at bottom for comfortable scrolling
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun MoodSelectionCard(
    mood: Mood,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = MoodColors.getMoodColor(mood)
    val cardColor = if (isSelected) backgroundColor else MaterialTheme.colorScheme.surface
    val contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface

    Card(
        modifier = Modifier
            .size(80.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        ),
        border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(3.dp, backgroundColor)
        } else null
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = MoodColors.getMoodEmoji(mood),
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = mood.name.lowercase().replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.labelSmall,
                color = contentColor.copy(alpha = 0.8f),
                fontWeight = FontWeight.Medium
            )
        }
    }
}