package ru.bonbon.libellus.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import ru.bonbon.libellus.composable.CurrentDayCard
import ru.bonbon.libellus.composable.ItemNumber
import ru.bonbon.libellus.composable.LessonItem
import ru.bonbon.libellus.composable.Loading
import ru.bonbon.libellus.repository.DataRepository
import java.text.SimpleDateFormat
import java.util.*


val tag = "MAIN_SCREEN"

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun ScheduleScreen() {
    val dataFlow = DataRepository.fetchSchedule()
    val dataState = dataFlow.collectAsState(initial = Pair(null, null))
    val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
    val comparator = kotlin.Comparator{
            str1: String, str2: String -> compareDates(str1, str2, simpleDateFormat)
    }
    if (dataState.value.second == null && dataState.value.first == null){
        Loading()
    }else{
        Column(modifier = Modifier.fillMaxSize()) {
            dataState.value.second?.toSortedMap(comparator)?.let { it ->
                val dates = it.keys.toMutableList()

                val currentDate = simpleDateFormat.format(Calendar.getInstance().time)
                val initialPage = if (it.containsKey(currentDate)){
                    dates.indexOf(currentDate)
                }else if(compareDates(it.lastKey(), currentDate, simpleDateFormat) < 0){
                    dates.size-1
                }else 0
                dates.forEachIndexed { dateIndex, date ->
                    dates[dateIndex] = date.replace("-", ".")
                }
                val pagerState = rememberPagerState(initialPage)
                val days = it.values.toList()
                HorizontalPager(count = days.size,
                    state = pagerState,
                    contentPadding = PaddingValues(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.94f)
                ) { page ->
                    Surface(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                    ) {
                        val rowHeight = 93
                        var heightCounter = 0
                        LazyColumn(
                            contentPadding = PaddingValues(
                                start = 8.dp,
                                bottom = 8.dp,
                                top = 8.dp,
                                end = 8.dp
                            )
                        ) {
                            itemsIndexed(days[page].Lessons!!) { index, lesson ->
                                if (index > 0 && lesson.Subgroup == 2 && days[page].Lessons!![index - 1].Time == lesson.Time) {
                                    return@itemsIndexed
                                }
                                if (lesson.Height!! > 1 && heightCounter == 0){
                                    heightCounter = lesson.Height!!
                                }
                                if (lesson.Height!! > 1 && heightCounter == lesson.Height){
                                    Row(modifier = Modifier
                                        .height((rowHeight * lesson.Height!! + 8 * (lesson.Height!!)).dp)) {
                                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                            for (j in index+1..lesson.Height!!+index){
                                                Row (modifier = Modifier
                                                    .height(rowHeight.dp), verticalAlignment = Alignment.CenterVertically){
                                                    ItemNumber(number = j)
                                                }
                                            }
                                        }
                                        LessonItem(lesson = lesson, subgroup = false)
                                    }
                                    Divider(thickness = 8.dp, color = Color.Transparent)

                                }
                                if (heightCounter == 0) {
                                    Row(modifier = Modifier
                                        .height((rowHeight).dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        ItemNumber(index + 1)
                                        Column(modifier = Modifier.fillMaxHeight()) {
                                            Row(
                                                modifier = Modifier
                                                    .height(rowHeight.dp)
                                                    .fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                if (lesson.Subgroup == 0) {
                                                    LessonItem(lesson = lesson, false)

                                                } else if (lesson.Subgroup == 1) {
                                                    LessonItem(lesson = lesson, true)
                                                    val lesson2 = days[page].Lessons!![index + 1]
                                                    if (lesson2.Subgroup == 2 && lesson2.Time == lesson.Time) {
                                                        LessonItem(lesson = lesson2, subgroup = true)
                                                    }

                                                } else {
                                                    Spacer(modifier = Modifier.weight(0.5f))
                                                    LessonItem(lesson = lesson, true)
                                                }
                                            }
                                        }
                                    }
                                    Divider(thickness = 8.dp, color = Color.Transparent)
                                }

                                if (heightCounter>0) heightCounter--
                            }
                        }
                    }
                }
                CurrentDayCard(day = days[pagerState.currentPage],
                    date = dates[pagerState.currentPage],
                    pagerState = pagerState)
            }
        }
    }
}

fun compareDates(str1: String, str2: String, simpleDateFormat: SimpleDateFormat): Int {
    val date1 = simpleDateFormat.parse(str1)
    val date2 = simpleDateFormat.parse(str2)
    return date1.compareTo(date2)
}

