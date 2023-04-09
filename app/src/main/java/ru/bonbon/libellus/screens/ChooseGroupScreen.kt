package ru.bonbon.libellus.screens

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.launch
import ru.bonbon.libellus.MainActivity
import ru.bonbon.libellus.MainActivity.Companion.datastore
import ru.bonbon.libellus.composable.ChooseGroup
import ru.bonbon.libellus.composable.Loading
import ru.bonbon.libellus.repository.DataRepository
import ru.bonbon.libellus.repository.DataRepository.group
import ru.bonbon.libellus.repository.DataRepository.speciality

@SuppressLint("UnrememberedMutableState")
@Composable
fun ChooseGroupScreen(
    context: Context,
    onConfirm: () -> Unit
) {
    val specsFlow = DataRepository.readScheduleOnce()
    val data = specsFlow.collectAsState(initial = null)
    val coroutineScope = rememberCoroutineScope()
    val tag = "CHOOSE_GROUP"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
        ,
        verticalArrangement = Arrangement.Center
    ){
        if (data.value != null) {
            ChooseGroup(context, data.value!!, true, null)
        }else{
            Loading()
        }
        Button(modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                if (speciality == "" || group == "" ) {
                    Log.d(tag, "SOMETHING IS WRONG")
                    Log.d(tag, group)

                }else {
                    onConfirm()
                    coroutineScope.launch {
                        context.datastore.edit { preferences ->
                            preferences[MainActivity.specialityKey] = speciality
                            preferences[MainActivity.groupKey] = group
                        }
                    }
                }
            }
        ) {
            Text(text = "Подтвердить")
        }
    }
}