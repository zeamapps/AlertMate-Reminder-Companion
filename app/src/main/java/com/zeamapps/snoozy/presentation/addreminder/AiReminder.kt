package com.zeamapps.snoozy.presentation.addreminder

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeamapps.snoozy.presentation.MainViewModel
import com.zeamapps.snoozy.utill.SnoozyColors
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AiReminder(mainViewModel: MainViewModel, onClick: () -> Unit) {
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), // Add padding to the column
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "What Should I Remind You About?",
            color = Color.White,
            fontWeight = FontWeight.Light,
            fontSize = 22.sp,
            modifier = Modifier.padding(vertical = 8.dp), // Add vertical padding
            maxLines = 1, // Ensures text doesn't overflow
            overflow = TextOverflow.Ellipsis // Adds ellipsis if text overflows
        )

        LazyRow(Modifier.padding(top = 10.dp), state = lazyListState) {
            items(getAiReminder()) { item ->
                GradientBorderSuggestionChip(item) {
                    mainViewModel.aiReminderTitle.value = item
                    coroutineScope.launch {
                        // Smoothly scroll to the clicked item
                        val index = getAiReminder().indexOf(item)
                        lazyListState.animateScrollToItem(index)
                    }
                }
            }
        }
        ChatBox(mainViewModel, onClick)
    }
}

@Composable
fun GradientBorderSuggestionChip(
    title: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 8.dp)
            .background(
                Color.Transparent,
                shape = RoundedCornerShape(24.dp)
            ) // Transparent background
            .border(
                width = 2.dp, // Thickness of the border
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        SnoozyColors.gradientColor
                            .get(0)
                            .copy(alpha = 0.5f),
                        SnoozyColors.gradientColor
                            .get(1)
                            .copy(alpha = 0.5f)
                    )
                ),
                shape = RoundedCornerShape(24.dp) // Rounded border
            )
            .clickable { onClick() } // Clickable for interaction
            .padding(horizontal = 12.dp, vertical = 8.dp) // Inner padding for the content
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBox(mainViewModel: MainViewModel, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp, top = 20.dp)
            .background(
                color = SnoozyColors.SmokyBlack,
                shape = RoundedCornerShape(24.dp)
            )
            .border(
                width = 2.dp, // Thickness of the border
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        SnoozyColors.gradientColor[0].copy(alpha = 0.5f),
                        SnoozyColors.gradientColor[1].copy(alpha = 0.5f)
                    )
                ),
                shape = RoundedCornerShape(24.dp) // Match the background's shape
            )
            .padding(horizontal = 12.dp, vertical = 8.dp), // Padding inside the border
        verticalAlignment = Alignment.CenterVertically
    ) {
        // TextField
        TextField(
            value = mainViewModel.aiReminderTitle.value,
            onValueChange = { mainViewModel.aiReminderTitle.value = it },
            placeholder = {
                Text(
                    text = "e.g., 'Meeting at 3 PM tomorrow.'",
                    color = Color.Gray
                )
            },
            modifier = Modifier
                .weight(1f)
                .background(Color.Transparent),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedTextColor = Color.White,
                cursorColor = Color.Gray,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(24.dp),
            singleLine = true
        )
        var isChatBoxEmpty = !mainViewModel.aiReminderTitle.value.isNotEmpty()
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Send Message",
                tint = if (isChatBoxEmpty) Color.Gray else SnoozyColors.GreenColor
            )
        }
    }
}

fun getAiReminder(): List<String> {
    return listOf(
        "Prepare for tomorrow’s meeting at 8 PM.",
        "Do a 10-minute workout at 6 PM.",
        "Read 10 pages of a book at 9 PM.",
        "Send project updates at 4 PM.",
        "Plan the weekend trip on Friday at 6 PM."
    )
}