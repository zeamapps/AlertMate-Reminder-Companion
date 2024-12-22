package com.zeamapps.snoozy.presentation.home

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeamapps.snoozy.data.models.Reminder
import com.zeamapps.snoozy.ui.theme.CharcoalGrey
import com.zeamapps.snoozy.utill.DateFormatHandler
import com.zeamapps.snoozy.utill.SnoozyColors
import kotlinx.coroutines.launch

@Composable
fun ReminderDetailsCard(
    reminder: Reminder,
    isReminderEnabled: Boolean,
    onSwitchToggle: (Boolean) -> Unit = {},
    onClick: () -> Unit,
    onDelete: () -> Unit // Added onDelete callback){}
) {
    val offsetX = remember { Animatable(0f) } // Tracks swipe offset
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 3.dp)
    ) {
        // Swipe Background (Delete Icon)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(13.dp)),
            contentAlignment = Alignment.CenterEnd
        ) {
            Column(verticalArrangement = Arrangement.Center) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White,
                    modifier = Modifier.padding(end = 16.dp),
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Delete",
                    color = Color.White,
                    modifier = Modifier.padding(end = 10.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // Foreground Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(offsetX.value.toInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            if (offsetX.value <= -300f) { // Threshold for deletion
                                scope.launch {
                                    offsetX.animateTo(-1000f) // Animate offscreen
                                    onDelete() // Trigger deletion callback
                                }
                            } else {
                                scope.launch { offsetX.animateTo(0f) } // Reset position
                            }
                        }, onDragStart = {

                        },
                        onHorizontalDrag = { _, delta ->
                            scope.launch {
                                offsetX.snapTo(offsetX.value + delta)
                            }
                        }
                    )
                }
                .clickable { onClick() },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            ReminderCardContent(reminder, isReminderEnabled, onSwitchToggle)
        }
    }
}

@Composable
private fun ReminderCardContent(
    reminder: Reminder,
    isReminderEnabled: Boolean,
    onSwitchToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Tag Color Indicator
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(Color(reminder.tagColor.toULong()), shape = CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Text Section
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Title
            Text(
                text = reminder.tittle,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = if (isReminderEnabled) MaterialTheme.colorScheme.onBackground else Color.Gray,
                    fontWeight = FontWeight.Medium
                ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            // Description
            if (reminder.description.isNotEmpty()) {
                Text(
                    text = reminder.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.LightGray,
                        fontSize = 14.sp
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Time
            Text(
                text = DateFormatHandler().formatTimestampToTime(reminder.time),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onTertiary,
                    fontSize = 12.sp
                )
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Switch
//        Switch(
//            checked = isReminderEnabled,
//            onCheckedChange = onSwitchToggle,
//            colors = SwitchDefaults.colors(
//                checkedThumbColor = Color.White.copy(alpha = 0.5f),
//                uncheckedThumbColor = Color.Gray,
//                checkedTrackColor = MaterialTheme.colorScheme.primary,
//                uncheckedTrackColor = Color.DarkGray
//            )
//        )

//        Switch(
//            checked = isReminderEnabled,
//            onCheckedChange = onSwitchToggle,
//            colors = SwitchDefaults.colors(
//                checkedThumbColor = Color.Transparent,
//                uncheckedThumbColor = Color.Transparent,
//                checkedTrackColor = Color.Transparent,
//                uncheckedTrackColor = Color.Transparent
//            )
//        )
    }
}
