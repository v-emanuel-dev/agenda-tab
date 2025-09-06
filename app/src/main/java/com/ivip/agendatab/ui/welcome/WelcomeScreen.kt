package com.ivip.agendatab.ui.welcome

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ivip.agendatab.domain.model.Mood
import com.ivip.agendatab.ui.theme.MoodColors
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(
    onGetStarted: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var currentStep by remember { mutableStateOf(0) }
    var showContent by remember { mutableStateOf(false) }

    // Animação de entrada inicial apenas
    LaunchedEffect(Unit) {
        delay(300)
        showContent = true
    }

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    )

    AnimatedVisibility(
        visible = showContent,
        enter = fadeIn(animationSpec = tween(800)) + slideInVertically(
            initialOffsetY = { it / 3 },
            animationSpec = tween(800)
        )
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(backgroundGradient)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header com botão pular
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onGetStarted
                ) {
                    Text("Pular")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Indicador de etapa atual com descrição
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "${currentStep + 1} de 4",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = when (currentStep) {
                            0 -> "Boas-vindas"
                            1 -> "Recursos"
                            2 -> "Como usar"
                            else -> "Começar"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Main content que muda baseado na etapa atual
            when (currentStep) {
                0 -> WelcomeHeader()
                1 -> AppIntroduction()
                2 -> MoodTrackingExplanation()
                else -> GetStartedSection(onGetStarted = onGetStarted)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Progress indicators - agora clicáveis para navegação rápida
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                repeat(4) { index ->
                    StepIndicator(
                        isActive = index <= currentStep,
                        isCompleted = index < currentStep,
                        onClick = { currentStep = index }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Navigation buttons - melhorada com mais controle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botão Anterior (ou espaço vazio se for a primeira etapa)
                if (currentStep > 0) {
                    OutlinedButton(
                        onClick = { currentStep-- },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Anterior")
                    }
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Botão central - varia conforme a etapa
                if (currentStep < 3) {
                    Button(
                        onClick = { currentStep++ },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Próximo")
                    }
                } else {
                    Button(
                        onClick = onGetStarted,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Começar")
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun WelcomeHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "📝",
                fontSize = 48.sp
            )
        }

        Text(
            text = "Bem-vindo ao Agenda TAB",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = "Sua jornada de autoconhecimento emocional começa aqui",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 24.sp
        )
    }
}

@Composable
private fun AppIntroduction() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "💫",
            fontSize = 64.sp
        )

        Text(
            text = "Conheça seu bem-estar emocional",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(top = 16.dp)
        ) {
            FeatureItem(
                icon = "📅",
                title = "Calendário Visual",
                description = "Veja seus humores em um calendário colorido e intuitivo"
            )

            FeatureItem(
                icon = "📊",
                title = "Visão Semanal",
                description = "Acompanhe tendências e padrões em seus registros"
            )

            FeatureItem(
                icon = "🔒",
                title = "Privacidade Total",
                description = "Seus dados ficam apenas no seu dispositivo"
            )
        }
    }
}

@Composable
private fun MoodTrackingExplanation() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Como funciona o registro de humor",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Registre como você se sente todos os dias escolhendo entre os humores disponíveis e adicionando suas anotações pessoais.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 24.sp
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Mood.values().forEach { mood ->
                MoodExample(mood = mood)
            }
        }

        Surface(
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "💡 Dica: Registre seu humor sempre no mesmo horário para criar um hábito saudável!",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun GetStartedSection(onGetStarted: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "🚀",
            fontSize = 64.sp
        )

        Text(
            text = "Tudo pronto!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Você está pronto para começar sua jornada de autoconhecimento. Vamos registrar seu primeiro humor?",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 24.sp
        )

        // Removido o botão "Iniciar Jornada" daqui - só usa o da navegação inferior
    }
}

@Composable
private fun FeatureItem(
    icon: String,
    title: String,
    description: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = icon,
            fontSize = 24.sp
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun MoodExample(mood: Mood) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MoodColors.getMoodColor(mood)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = MoodColors.getMoodEmoji(mood),
                fontSize = 24.sp
            )
        }

        Text(
            text = when (mood) {
                Mood.HAPPY -> "Feliz"
                Mood.CALM -> "Calmo"
                Mood.ANXIOUS -> "Ansioso"
                Mood.DEPRESSED -> "Triste"
            },
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StepIndicator(
    isActive: Boolean,
    isCompleted: Boolean,
    onClick: () -> Unit
) {
    val color by animateColorAsState(
        targetValue = when {
            isCompleted -> MaterialTheme.colorScheme.primary
            isActive -> MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
            else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        },
        animationSpec = tween(300)
    )

    val scale by animateFloatAsState(
        targetValue = if (isActive) 1.2f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    Box(
        modifier = Modifier
            .size(16.dp) // Aumentado para ser mais fácil de clicar
            .scale(scale)
            .clip(CircleShape)
            .background(color)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        // Adiciona um ponto interno se estiver ativo
        if (isActive || isCompleted) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.8f))
            )
        }
    }
}