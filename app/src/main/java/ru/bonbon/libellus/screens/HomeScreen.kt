package ru.bonbon.libellus.screens

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import ru.bonbon.libellus.composable.NavDrawer
import ru.bonbon.libellus.navigation.NavGraph
import ru.bonbon.libellus.navigation.Screen
import ru.bonbon.libellus.repository.DataRepository
import ru.bonbon.libellus.repository.DataRepository.group

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    context: Context,
    onThemeChange: (Int) -> Unit,
    currentTheme: Int?
) {
    val tag = "HOME_SCREEN"
    var groupState by remember { mutableStateOf(Pair(DataRepository.speciality, group)) }
    val navController = rememberNavController()
    val scaffoldState =
        rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    var title by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Log.d(tag, "DEST   " + currentDestination.toString())
    navController.addOnDestinationChangedListener { controller, destination, arguments ->
        for (item in controller.backQueue) {
            Log.d(tag, item.destination.route.toString())
        }
        Log.d(tag, controller.backQueue.size.toString())
        title = when (destination.route) {
            Screen.Schedule.route -> groupState.second
            Screen.Consultations.route -> Screen.Consultations.label
            Screen.Settings.route -> Screen.Settings.label
            else -> ""
        }
    }
    val drawerItems = arrayOf<Screen>(
        Screen.Settings,
        Screen.Consultations
    )


    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = title)
                },
                navigationIcon = {
                    if ((currentDestination?.route == Screen.Schedule.route) || (currentDestination?.route == null)) {
                        IconButton(
                            content = {
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = "nav menu"
                                )
                            },
                            onClick = {
                                scope.launch {
                                    scaffoldState.drawerState.open()
                                }
                            }
                        )
                    } else {
                        IconButton(
                            content = {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "nav menu"
                                )
                            },
                            onClick = {
                                navController.popBackStack(
                                    route = Screen.Schedule.route,
                                    inclusive = false,
                                    saveState = true
                                )
                            }
                        )
                    }
                },

            )
        },
        drawerGesturesEnabled = currentDestination?.route == null || currentDestination.route == Screen.Schedule.route,
        drawerElevation = 0.dp,
        drawerContent = {
            NavDrawer(
                items = drawerItems,
                onItemClick = {
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                    if (it.route != currentDestination?.route) {
                        navController.navigate(it.route) {

                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
            )
        }
    ) {
        NavGraph(
            navController = navController,
            startDestination = Screen.Schedule.route,
            context = context,
            onValueUpdated = { groupState = it },
            onThemeChange = onThemeChange,
            currentTheme = currentTheme
        )
    }
}

