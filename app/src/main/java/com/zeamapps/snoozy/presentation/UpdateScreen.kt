//package com.zeamapps.snoozy.presentation
//
//import android.widget.NumberPicker
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.lazy.rememberLazyListState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Text
//import androidx.compose.material3.TimeInput
//import androidx.compose.material3.rememberTimePickerState
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.mutableStateListOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.zeamapps.snoozy.ui.theme.SnoozyTheme
//import com.zeamapps.snoozy.utill.SnoozyColors
//
//@Composable
//fun MyScreen() {
//    val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
//    val selectedDays = remember { mutableStateListOf<String>() } // Tracks selected days
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Black),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//
//        // LazyRow for horizontally scrolling items
//        LazyRow(
//            contentPadding = PaddingValues(horizontal = 8.dp), // Padding around items
//            horizontalArrangement = Arrangement.spacedBy(8.dp) // Space between cards
//        ) {
//            items(daysOfWeek) { day ->
//                CardContentDesign(day, selectedDays)
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Display the joined string of selected days
//        Text(
//            text = if (selectedDays.isEmpty()) "No days selected" else selectedDays.sortedBy { daysOfWeek.indexOf(it) }.joinToString(", "),
//            color = Color.White,
//            modifier = Modifier.padding(16.dp),
//            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
//        )
//    }
//}
//
//@Composable
//fun CardContentDesign(day: String, selectedDays: MutableList<String>) {
//    var isSelectedBtn = remember { mutableStateOf(false) }
//    val backgroundColor = if (isSelectedBtn.value) Color.Gray else SnoozyColors.SmokyBlack
//
//    Card(
//        modifier = Modifier
//            .size(45.dp)
//            .padding(2.dp)
//            .clickable {
//                isSelectedBtn.value = !isSelectedBtn.value
//                if (isSelectedBtn.value) {
//                    selectedDays.add(day) // Add the day if selected
//                } else {
//
//                    selectedDays.remove(day) // Remove the day if deselected
//                }
//            },
//        shape = CircleShape,
//        colors = CardDefaults.cardColors(backgroundColor)
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(4.dp), // Padding inside the card
//            contentAlignment = Alignment.Center // Center content inside the card
//        ) {
//            Text(
//                text = day.substring(0, 1), // Show the first letter of the day
//                style = TextStyle(
//                    color = Color.White,
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 16.sp
//                )
//            )
//        }
//    }
//}
//
//@Composable
//fun NumberPicker(
//    value: Int,
//    range: IntRange,
//    onValueChange: (Int) -> Unit,
//    textStyle: TextStyle = TextStyle.Default
//) {
//    val lazyListState = rememberLazyListState(initialFirstVisibleItemIndex = range.indexOf(value))
//
//    LaunchedEffect(value) {
//        lazyListState.scrollToItem(range.indexOf(value))
//    }
//
//    Box(
//        modifier = Modifier
//            .size(80.dp, 120.dp)
//            .background(Color.Transparent),
//        contentAlignment = Alignment.Center
//    ) {
//        LazyColumn(
//            state = lazyListState,
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Center
//        ) {
//            items(range.toList()) { item ->
//                Text(
//                    text = item.toString().padStart(2, '0'),
//                    style = if (item == value) textStyle.copy(fontWeight = FontWeight.Bold)
//                    else textStyle.copy(color = textStyle.color.copy(alpha = 0.5f)),
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable {
//                            onValueChange(item)
//                        },
//                    textAlign = TextAlign.Center
//                )
//            }
//        }
//    }
//}
