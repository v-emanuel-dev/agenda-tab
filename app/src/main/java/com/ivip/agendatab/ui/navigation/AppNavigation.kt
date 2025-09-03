package com.ivip.agendatab.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ivip.agendatab.ui.calendar.CalendarScreen
import com.ivip.agendatab.ui.calendar.CalendarViewModel
import com.ivip.agendatab.ui.edit.EditEntryScreen
import java.time.LocalDate

@Composable
fun AppNavigation(
    calendarViewModel: CalendarViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "calendar"
    ) {
        composable("calendar") {
            CalendarScreen(
                viewModel = calendarViewModel,
                onNavigateToEdit = { date ->
                    navController.navigate("edit/${date}")
                }
            )
        }

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