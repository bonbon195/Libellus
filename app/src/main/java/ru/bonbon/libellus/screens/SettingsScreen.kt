package ru.bonbon.libellus.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import ru.bonbon.libellus.composable.ChooseGroup
import ru.bonbon.libellus.composable.Loading
import ru.bonbon.libellus.composable.TextFieldWithDropdownMenu
import ru.bonbon.libellus.repository.DataRepository
import ru.bonbon.libellus.ui.theme.Themes


@Composable
fun SettingsScreen(
    context: Context,
    onValueUpdated: (Pair<String, String>) -> Unit,
    onThemeChange: (Int) -> Unit,
    currentTheme: Int?
){
    val specsFlow = DataRepository.readScheduleOnce()
    val data = specsFlow.collectAsState(initial = null)
    var themeTextFieldValue by rememberSaveable {
        mutableStateOf(when(currentTheme){
                Themes.Light.id -> Themes.Light.label
                Themes.Dark.id -> Themes.Dark.label
                else -> Themes.System.label
            }
        )
    }
    val themes = Themes.values()
    val labels = mutableListOf<String>()
    val uriHandler = LocalUriHandler.current

    themes.forEach {
        labels.add(it.label)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (data.value != null) {
            ChooseGroup(
                context,
                data.value!!,
                false,
                onValueUpdated
            )
            TextFieldWithDropdownMenu(
                textFieldValue = themeTextFieldValue,
                context = context,
                data = labels,
                isGroup = false,
                onTextFieldValueChange = {
                    themeTextFieldValue = it
                    Log.d(tag, it)
                    when (it) {
                        Themes.Light.label -> onThemeChange(Themes.Light.id)
                        Themes.Dark.label -> onThemeChange(Themes.Dark.id)
                        Themes.System.label -> onThemeChange(Themes.System.id)
                    }
                },
                label = "Тема"
            )
        } else {
            Loading()
        }
    }
}