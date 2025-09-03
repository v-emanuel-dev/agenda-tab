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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ivip.agendatab.R
import com.ivip.agendatab.domain.model.DailyEntry
import com.ivip.agendatab.domain.model.Mood
import com.ivip.agendatab.ui.theme.MoodColors
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEntryScreen(
    date: LocalDate,
    existingEntry: DailyEntry?,
    onSave: (DailyEntry) -> Unit,
    onDelete: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
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
                    text = date.format(
                        DateTimeFormatter.ofPattern(
                            context.getString(R.string.date_format_full),
                            Locale.getDefault()
                        )
                    ),
                    style = MaterialTheme.typography.titleLarge
                )
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
                }
            },
            actions = {
                if (existingEntry != null) {
                    IconButton(onClick = onDelete) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = stringResource(R.string.delete),
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
            // Mood selection section
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.how_are_you_feeling),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = stringResource(R.string.select_mood_description),
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
                        val moodText = when (selectedMood!!) {
                            Mood.HAPPY -> stringResource(R.string.mood_happy)
                            Mood.CALM -> stringResource(R.string.mood_calm)
                            Mood.ANXIOUS -> stringResource(R.string.mood_anxious)
                            Mood.DEPRESSED -> stringResource(R.string.mood_depressed)
                        }

                        Surface(
                            color = MoodColors.getMoodColor(selectedMood!!).copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.selected_mood, moodText),
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
                        text = stringResource(R.string.tell_about_day),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = stringResource(R.string.note_description),
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
                            Text(stringResource(R.string.note_placeholder))
                        },
                        supportingText = {
                            Text(stringResource(R.string.character_count, noteText.length, 500))
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
                    text = if (existingEntry != null)
                        stringResource(R.string.update_entry)
                    else
                        stringResource(R.string.save_entry),
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

    val moodText = when (mood) {
        Mood.HAPPY -> stringResource(R.string.mood_happy)
        Mood.CALM -> stringResource(R.string.mood_calm)
        Mood.ANXIOUS -> stringResource(R.string.mood_anxious)
        Mood.DEPRESSED -> stringResource(R.string.mood_depressed)
    }

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
                text = moodText,
                style = MaterialTheme.typography.labelSmall,
                color = contentColor.copy(alpha = 0.8f),
                fontWeight = FontWeight.Medium
            )
        }
    }
}