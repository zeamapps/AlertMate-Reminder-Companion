package com.zeamapps.snoozy.presentation.addreminder

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.style.TextAlign
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
            .padding(20.dp), // Consistent padding across the screen
        verticalArrangement = Arrangement.spacedBy(16.dp), // Better spacing
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "What Should I Remind You About?",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        // Suggestion Chips
        LazyRow(
            state = lazyListState,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(getAiReminder()) { item ->
                SuggestionCard(item) {
                    mainViewModel.aiReminderTitle.value = item
                    coroutineScope.launch {
                        val index = getAiReminder().indexOf(item)
                        lazyListState.animateScrollToItem(index)
                    }
                }
            }
        }

        // Chat Box
        ChatBox(mainViewModel, onClick)
    }
}

@Composable
fun GradientBorderSuggestionChip(title: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .clickable { onClick() }
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(24.dp)
            )
            .border(
                width = 2.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

fun getAiReminder(): List<String> {
    return listOf(
        "Prepare for tomorrow’s meeting at 8 PM",
        "Do a 10-minute workout at 6 PM",
        "Read 10 pages of a book at 9 PM",
        "Send project updates at 4 PM",
        "Plan the weekend trip on Friday at 6 PM"
    )
}

@Composable
fun SuggestionCard(title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(width = 180.dp, height = 100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBox(mainViewModel: MainViewModel, onClick: () -> Unit) {
    val isChatBoxEmpty = mainViewModel.aiReminderTitle.value.isBlank()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Input Field
        TextField(
            value = mainViewModel.aiReminderTitle.value,
            onValueChange = { mainViewModel.aiReminderTitle.value = it },
            placeholder = {
                Text(
                    text = "'Meeting at 3 PM tomorrow'",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            },
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(24.dp),
            singleLine = true
        )

        // Send Button
        IconButton(
            onClick = onClick,
            enabled = !isChatBoxEmpty,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send Reminder",
                tint = if (isChatBoxEmpty) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                else MaterialTheme.colorScheme.primary
            )
        }
    }
}