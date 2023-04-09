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
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import ru.bonbon.libellus.model.ConsultDay

@OptIn(ExperimentalUnitApi::class)
@Composable
fun ConsultDayItem(consultDay: ConsultDay){
    Row(modifier = Modifier
        .fillMaxHeight()
        .height(110.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(PaddingValues(start = 6.dp, top = 6.dp, end = 6.dp, bottom = 6.dp)),
            ) {
                Row(Modifier.fillMaxWidth()) {
                    Text(text = (consultDay.Name ?: "") + (" " + (consultDay.Date?.replace("-", ".")?: "")),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colors.primary,
                        fontSize = TextUnit(value = 16f, type = TextUnitType.Sp)
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                    ,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = consultDay.Time ?: "")
                    Text(text = consultDay.Classroom ?: "")
                }
            }
        }
    }
}