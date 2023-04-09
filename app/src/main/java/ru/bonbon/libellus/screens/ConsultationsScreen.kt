package ru.bonbon.libellus.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import ru.bonbon.libellus.composable.ConsultDayItem
import ru.bonbon.libellus.composable.ItemNumber
import ru.bonbon.libellus.composable.Loading
import ru.bonbon.libellus.composable.TextFieldWithDropdownMenu
import ru.bonbon.libellus.model.ConsultDay
import ru.bonbon.libellus.repository.DataRepository

@Composable
fun ConsultationsScreen(){
    lateinit var dataFlow: Flow<List<ConsultDay>>
    lateinit var dataState: State<List<ConsultDay>>
    val weeksState = DataRepository.fetchWeeks().collectAsState(initial = listOf())

    var teachersState = remember {mutableStateOf(listOf<String>())}
    var teacherTextFieldValue by remember {
        mutableStateOf("")
    }
    var weekTextFieldValue by remember {
        mutableStateOf("")
    }
    if (weekTextFieldValue.isNotEmpty()){
        teachersState = DataRepository.fetchTeachers(weekTextFieldValue).collectAsState(initial = listOf()) as MutableState<List<String>>
    }
    Column(modifier = Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        if (weeksState.value.isEmpty()){
            Loading()
        }
        else{
            TextFieldWithDropdownMenu(
                textFieldValue = weekTextFieldValue,
                context = null,
                data = weeksState.value,
                isGroup = false,
                onTextFieldValueChange = {
                    weekTextFieldValue = it
                    teacherTextFieldValue = ""
                },
                label = "Неделя"
            )
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(4.dp))
            TextFieldWithDropdownMenu(
                textFieldValue = teacherTextFieldValue,
                context = null,
                data = teachersState.value,
                isGroup = false,
                onTextFieldValueChange = {
                    teacherTextFieldValue = it
                },
                label = "Преподаватель"
            )
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(4.dp))
            if (teacherTextFieldValue.isNotEmpty()){
                dataFlow = DataRepository.fetchConsultations(weekTextFieldValue, teacherTextFieldValue)
                dataState = dataFlow.collectAsState(initial = listOf())
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 6.dp,
                        bottom = 6.dp,
                        top = 6.dp,
                        end = 6.dp
                    ),
                    content = {
                        itemsIndexed(dataState.value) { index: Int, item: ConsultDay ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .padding(
                                        PaddingValues(
                                            start = 6.dp,
                                            bottom = 6.dp,
                                            top = 0.dp,
                                            end = 6.dp
                                        )
                                    )
                            ) {
                                ItemNumber(number = index+1)
                                ConsultDayItem(consultDay = item)
                            }
                        }
                    },
                )
            }
        }
    }
}