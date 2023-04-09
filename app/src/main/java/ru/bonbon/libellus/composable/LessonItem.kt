package ru.bonbon.libellus.composable

import androidx.compose.foundation.layout.*

import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import ru.bonbon.libellus.model.Lesson

@OptIn(ExperimentalUnitApi::class)
@Composable
fun LessonItem(lesson: Lesson, subgroup: Boolean){
    val weight = if (subgroup) 0.5f else 1f
    Row(modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth(weight),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier
                .fillMaxHeight(),
            elevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(PaddingValues(start = 6.dp, top = 6.dp, end = 6.dp, bottom = 6.dp)),
            ) {
                Row() {
                    Text( // time
                        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                        text = lesson.Time.toString(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colors.primary,
                        fontSize = TextUnit(value = 16f, type = TextUnitType.Sp)
                    )
                }
                Column(verticalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxSize()) {
                    Text( // name
                        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                        text = lesson.Name.toString(),
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colors.onBackground,
                        fontSize = TextUnit(value = 16f, type = TextUnitType.Sp),
                        maxLines = 2
                    )
                    Row(modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        Text( // teacher
                            modifier = Modifier.wrapContentWidth(),
                            text = lesson.Teacher.toString(),
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colors.onBackground,
                            fontSize = TextUnit(value = 16f, type = TextUnitType.Sp),
                            lineHeight = TextUnit(value = 18f, type = TextUnitType.Sp)
                        )
                        Text( // classroom
                            modifier = Modifier.wrapContentWidth().wrapContentHeight(),
                            text = lesson.Classroom.toString(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colors.onBackground,
                            fontSize = TextUnit(value = 16f, type = TextUnitType.Sp),

                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun LessonItemPreview(){
    LessonItem(Lesson(), false)
}