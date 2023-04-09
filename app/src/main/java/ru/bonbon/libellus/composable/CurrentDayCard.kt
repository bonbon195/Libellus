package ru.bonbon.libellus.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.PagerState
import ru.bonbon.libellus.model.Day

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CurrentDayCard(day: Day, date: String, pagerState: PagerState){
    Card(modifier = Modifier,

        shape = MaterialTheme.shapes.medium,
    ){
        Row(modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
        ) {
//            Spacer(modifier = Modifier.weight(0.1f))
            Column(modifier = Modifier
                .weight(0.8f)
            ) {
                Text(text = date + " " + day.Name!!,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Bold
                )
                HorizontalPagerIndicator(
                    pagerState = pagerState,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(6.dp),
                    activeColor = MaterialTheme.colors.primary
                )
            }
        }
    }
}