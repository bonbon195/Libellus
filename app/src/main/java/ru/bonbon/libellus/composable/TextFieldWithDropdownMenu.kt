package ru.bonbon.libellus.composable

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.launch
import ru.bonbon.libellus.MainActivity
import ru.bonbon.libellus.MainActivity.Companion.datastore
import ru.bonbon.libellus.repository.DataRepository.group
import ru.bonbon.libellus.repository.DataRepository.speciality


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TextFieldWithDropdownMenu(
    textFieldValue: String,
    context: Context?,
    data: List<String>,
    isGroup: Boolean,
    onTextFieldValueChange: (String) -> Unit,
    label: String
) {
    var expanded by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val tag = "TEXT_FIELD_DROPDOWN"
    val scope = rememberCoroutineScope()
    var focused by remember {
        mutableStateOf(false)
    }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {},
        modifier = Modifier
            .focusRequester(focusRequester)
            .onFocusChanged {
                if (it.isFocused) {
                    focused = it.isFocused
                    expanded = !expanded
                }else if (!it.isFocused){
                    focused = false
                }

            }
            .wrapContentSize()
    ) {
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                Icon(
                    Icons.Filled.ArrowDropDown,
                    "Trailing icon for exposed dropdown menu",
                    Modifier.rotate(
                        if (expanded)
                            180f
                        else
                            360f
                    )
                )
            },
            label = { Text(text = label) },
/*            modifier = Modifier
                .clickable {
                    expanded = !expanded
                    enabled = !enabled
                    if (focused){
                        focusManager.clearFocus()
                    }else{
                        focusRequester.requestFocus()
                    }


            }
            ,*/

            textStyle = TextStyle(color = MaterialTheme.colors.onBackground),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
                focusManager.clearFocus()
            },
            modifier = Modifier
                .exposedDropdownSize(matchTextFieldWidth = true)
                .padding(4.dp),

            ) {
            data.forEach { value ->
                Text(text = value,
                    modifier = Modifier
                        .clickable {
                            onTextFieldValueChange(value)
                            expanded = false
                            focusManager.clearFocus()
                            if (isGroup) {
                                scope.launch {
                                    context!!.datastore.edit { preferences ->
                                        preferences[MainActivity.specialityKey] = speciality
                                        preferences[MainActivity.groupKey] = group
                                    }
                                }
                            }
                        }
                        .fillMaxWidth()
                        .padding(4.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
