package ru.bonbon.libellus

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.bonbon.libellus.MainActivity.Companion.datastore
import ru.bonbon.libellus.repository.DataRepository.group
import ru.bonbon.libellus.repository.DataRepository.speciality
import ru.bonbon.libellus.screens.ChooseGroupScreen
import ru.bonbon.libellus.screens.HomeScreen
import ru.bonbon.libellus.ui.theme.LibellusTheme
import ru.bonbon.libellus.ui.theme.Themes


class MainActivity : ComponentActivity() {
    companion object {
        val specialityKey = stringPreferencesKey("speciality")
        val groupKey = stringPreferencesKey("group")
        val darkThemeKey = intPreferencesKey("dark_theme")
        val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "settings")
        val tag = "MAIN_APP"
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var currentTheme = 0
            runBlocking {
                speciality = datastore.data.map { preferences ->
                    preferences[specialityKey] ?: ""
                }.first()

                group  = datastore.data.map { preferences ->
                    preferences[groupKey] ?: ""
                }.first()
                currentTheme = datastore.data.map { preferences ->
                    preferences[darkThemeKey]?: 0
                }.first()
            }
            App(this.applicationContext, currentTheme)
        }
    }

}



@SuppressLint("UnrememberedMutableState")
@Composable
fun App(context: Context, currentTheme: Int){
    val systemUiController = rememberSystemUiController()
    var isDarkThemeState: Boolean? by remember {
        mutableStateOf(null)
    }
    val scope = rememberCoroutineScope()
    var themeVal: Int by remember {
        mutableStateOf(currentTheme)
    }
    var confirmed by remember {
        mutableStateOf(false)
    }

    isDarkThemeState = when(themeVal) {
        Themes.Light.id -> false
        Themes.Dark.id -> true
        else -> {isSystemInDarkTheme()}
    }

    LibellusTheme(
        darkTheme = isDarkThemeState!!
    ) {
        if (isDarkThemeState!!){
            systemUiController.setStatusBarColor(
                color = MaterialTheme.colors.surface
            )

        }else{
            systemUiController.setStatusBarColor(
                color = MaterialTheme.colors.primary
            )
        }
        if ((speciality.isBlank() || group.isBlank()) && !confirmed){
            ChooseGroupScreen(
                context = context,
                onConfirm = {
                    confirmed = true

                }
            )
        }else{
            HomeScreen(
                context = context,
                onThemeChange = {
                    themeVal = it
                    confirmed = true
                    scope.launch {
                        context.datastore.edit { preferences ->
                            preferences[MainActivity.darkThemeKey] = themeVal
                        }
                    }
                },
                currentTheme = themeVal
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LibellusTheme {

    }
}

