package ru.bonbon.libellus.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.bonbon.libellus.navigation.Screen

@Composable
fun NavDrawer(
    items: Array<Screen>,
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
    onItemClick: (Screen) -> Unit
){
    val uriHandler = LocalUriHandler.current
    Column(modifier = Modifier.fillMaxHeight()) {
        LazyColumn(modifier = Modifier){
            items(items) { item->
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onItemClick(item)
                    }
                    .padding(16.dp)
                ) {
                    Text(text = item.label, modifier = Modifier.weight(1f), style = itemTextStyle)
                }
            }
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .clickable {
                uriHandler.openUri("https://www.donationalerts.com/r/bonbon195")
            }
        ){
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
            ) {
                Text(
                    text = "Поддержать разработчика",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                    ,
                    style = itemTextStyle
                )
            }
        }

    }

}