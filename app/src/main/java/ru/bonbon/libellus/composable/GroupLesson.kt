package ru.bonbon.libellus.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.bonbon.libellus.model.Day
import ru.bonbon.libellus.model.Group
import ru.bonbon.libellus.ui.theme.Shapes

@Composable
fun GroupLessons(group: Group){
    val days = group.days!!
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = group.name!!)
        LazyColumn(){
            itemsIndexed(items = days, key =  { _: Int, day: Day -> days.indexOf(day)}){
                    index, day ->
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(MaterialTheme.colors.background)
                    .clip(shape = Shapes.small)){
                    Row() {
                        Column() {
                            day.Lessons!!.forEach {
                                it.Name?.let { it1 -> Text(text = it1) }

                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun GroupLessonsPreview(){
    GroupLessons(group = Group())
}