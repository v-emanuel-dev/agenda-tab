package com.ivip.agendatab.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ivip.agendatab.R
import com.ivip.agendatab.ui.theme.ThemeManager
import com.ivip.agendatab.ui.theme.ThemeMode

@Composable
fun ThemeSelector(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val currentTheme = ThemeManager.getCurrentThemeMode()
    var selectedTheme by remember { mutableStateOf(currentTheme) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .selectableGroup(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.theme_settings),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                // Light Theme Option
                ThemeOption(
                    title = stringResource(R.string.light_theme),
                    emoji = "☀️",
                    selected = selectedTheme == ThemeMode.LIGHT,
                    onClick = { selectedTheme = ThemeMode.LIGHT }
                )

                // Dark Theme Option
                ThemeOption(
                    title = stringResource(R.string.dark_theme),
                    emoji = "🌙",
                    selected = selectedTheme == ThemeMode.DARK,
                    onClick = { selectedTheme = ThemeMode.DARK }
                )

                // System Theme Option
                ThemeOption(
                    title = stringResource(R.string.system_theme),
                    emoji = "⚙️",
                    selected = selectedTheme == ThemeMode.SYSTEM,
                    onClick = { selectedTheme = ThemeMode.SYSTEM }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.cancel))
                    }

                    Button(
                        onClick = {
                            ThemeManager.setThemeMode(context, selectedTheme)
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
private fun ThemeOption(
    title: String,
    emoji: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = emoji,
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        RadioButton(
            selected = selected,
            onClick = null
        )
    }
}