package ru.bonbon.libellus.composable

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.bonbon.libellus.repository.DataRepository.group
import ru.bonbon.libellus.repository.DataRepository.speciality

@SuppressLint("UnrememberedMutableState")
@Composable
fun ChooseGroup(
    context: Context,
    data: Map<String, List<String>>,
    firstOpen: Boolean,
    onValueUpdated: ((Pair<String, String>) -> Unit)?,
) {
    var groups = listOf<String>()
    var specialityTextFieldValue by rememberSaveable { mutableStateOf(speciality) }
    var groupTextFieldValue by rememberSaveable { mutableStateOf(group) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TextFieldWithDropdownMenu(
            textFieldValue = specialityTextFieldValue,
            context = null,
            data = data.keys.toList(),
            isGroup = false,
            onTextFieldValueChange = {specialityTextFieldValue = it},
            label = "Код специальности"
        )
        if (specialityTextFieldValue.isNotBlank()){
            groups = data[specialityTextFieldValue]!!
            if (!groups.contains(groupTextFieldValue)) groupTextFieldValue = ""
        }
        TextFieldWithDropdownMenu(
            textFieldValue = groupTextFieldValue,
            context = context,
            data = groups,
            isGroup = true,
            onTextFieldValueChange = {
                groupTextFieldValue = it
                speciality = specialityTextFieldValue
                group = groupTextFieldValue
                if (!firstOpen) {
                    onValueUpdated!!(Pair(specialityTextFieldValue, groupTextFieldValue))
                }
            },
            label = "Группа"
        )
    }
}