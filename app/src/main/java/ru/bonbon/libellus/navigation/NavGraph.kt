package ru.bonbon.libellus.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.bonbon.libellus.screens.*

@Composable
fun NavGraph (
    navController: NavHostController,
    startDestination: String,
    context: Context,
    onValueUpdated: (Pair<String, String>) -> Unit,
    onThemeChange: (Int) -> Unit,
    currentTheme: Int?
){
    NavHost(navController = navController, startDestination = startDestination) {
        composable(
            route = Screen.Schedule.route
        ) {
            ScheduleScreen()
        }
        composable(
            route = Screen.Consultations.route
        ) {
            ConsultationsScreen()
        }
        composable(
            route = Screen.Settings.route
        ) {
            SettingsScreen(
                context = context,
                onValueUpdated = onValueUpdated,
                onThemeChange = onThemeChange,
                currentTheme = currentTheme
            )
        }
    }
}