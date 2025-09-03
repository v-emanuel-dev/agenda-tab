package com.ivip.agendatab.ui.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ivip.agendatab.domain.model.DailyEntry
import com.ivip.agendatab.domain.model.Mood
import com.ivip.agendatab.ui.theme.MoodColors
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEntryModal(
    date: LocalDate,
    existingEntry: DailyEntry?,
    onSave: (DailyEntry) -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    var selectedMood by remember { mutableStateOf(existingEntry?.mood) }
    var noteText by remember { mutableStateOf(existingEntry?.note ?: "") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Edit Entry",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = date.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "Select Mood:",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(Mood.values()) { mood ->
                        MoodButton(
                            mood = mood,
                            isSelected = selectedMood == mood,
                            onClick = { selectedMood = mood }
                        )
                    }
                }

                Text(
                    text = "Note:",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )

                OutlinedTextField(
                    value = noteText,
                    onValueChange = { if (it.length <= 280) noteText = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("How was your day?") },
                    maxLines = 4,
                    supportingText = { Text("${noteText.length}/280") }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (existingEntry != null) {
                        OutlinedButton(
                            onClick = onDelete,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Delete")
                        }
                    }

                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            selectedMood?.let { mood ->
                                onSave(DailyEntry(date, mood, noteText))
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = selectedMood != null
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

@Composable
private fun MoodButton(
    mood: Mood,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = MoodColors.getMoodColor(mood)
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent

    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .then(
                if (isSelected) {
                    Modifier.padding(2.dp).background(
                        borderColor,
                        RoundedCornerShape(10.dp)
                    ).padding(2.dp).background(
                        backgroundColor,
                        RoundedCornerShape(8.dp)
                    )
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = mood.name.first().toString(),
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}