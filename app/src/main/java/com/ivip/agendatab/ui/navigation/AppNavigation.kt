package com.ivip.agendatab.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ivip.agendatab.ui.calendar.CalendarScreen
import com.ivip.agendatab.ui.calendar.CalendarViewModel
import com.ivip.agendatab.ui.edit.EditEntryScreen
import com.ivip.agendatab.ui.welcome.WelcomeScreen
import com.ivip.agendatab.ui.welcome.WelcomeViewModel
import com.ivip.agendatab.ui.welcome.WelcomeViewModelFactory
import java.time.LocalDate

@Composable
fun AppNavigation(
    calendarViewModel: CalendarViewModel,
    context: Context
) {
    val navController = rememberNavController()

    // ViewModel para a tela de boas-vindas
    val welcomeViewModel: WelcomeViewModel = viewModel(
        factory = WelcomeViewModelFactory(context)
    )
    val welcomeUiState by welcomeViewModel.uiState.collectAsState()

    // Determina a tela inicial baseado no estado do welcome
    val startDestination = if (welcomeUiState.isFirstLaunch) "welcome" else "calendar"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Tela de boas-vindas
        composable("welcome") {
            WelcomeScreen(
                onGetStarted = {
                    welcomeViewModel.completeWelcome()
                    navController.navigate("calendar") {
                        // Remove welcome da stack para que o usuário não volte para ela
                        popUpTo("welcome") { inclusive = true }
                    }
                }
            )
        }

        // Tela principal do calendário
        composable("calendar") {
            CalendarScreen(
                viewModel = calendarViewModel,
                onNavigateToEdit = { date ->
                    navController.navigate("edit/${date}")
                },
                onShowWelcome = {
                    // Reset o estado do welcome e navega para a tela
                    welcomeViewModel.resetWelcome()
                    navController.navigate("welcome")
                }
            )
        }

        // Tela de edição de entrada
        composable(
            "edit/{date}",
            arguments = listOf(navArgument("date") { type = NavType.StringType })
        ) { backStackEntry ->
            val dateString = backStackEntry.arguments?.getString("date")
            val date = LocalDate.parse(dateString)

            EditEntryScreen(
                date = date,
                existingEntry = calendarViewModel.uiState.value.dailyEntries[date],
                onSave = { entry ->
                    calendarViewModel.onEntryUpdated(entry)
                    navController.popBackStack()
                },
                onDelete = {
                    calendarViewModel.onEntryDeleted(date)
                    navController.popBackStack()
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}