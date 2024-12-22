package com.zeamapps.snoozy.presentation.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.zeamapps.snoozy.utill.SnoozyColors
import com.zeamapps.snoozy.utill.SnoozyColors.MediumBlue


//@Composable
//fun StyledFloatingActionButton(onClick: () -> Unit) {
//    FloatingActionButton(
//        onClick = { onClick },
//        shape = CircleShape, // Ensures circular shape
//        containerColor = Color.Transparent, // Makes the background transparent to show the gradient
//        elevation = FloatingActionButtonDefaults.elevation(0.dp) // Removes shadow for clean look
//    ) {
//        Box(
//            modifier = Modifier
//                .size(70.dp).clickable{onClick()}
//                // Ensures the Box fills the FAB size
//                .background(
//                    brush = Brush.horizontalGradient(SnoozyColors.gradientColor),
//                    shape = CircleShape // Matches the FAB's shape
//                ),
//            contentAlignment = Alignment.Center // Centers the icon
//        ) {
//            Icon(
//                imageVector = Icons.Default.Add,
//                contentDescription = null,
//                tint = Color.White, // Sets icon color
//                modifier = Modifier.size(28.dp) // Adjust icon size
//            )
//        }
//    }
//}


@Composable
fun PulsedFloatingActionBtn(onBtnClicked: () -> Unit) {
    // Infinite transition for pulsar animation
    val infiniteTransition = rememberInfiniteTransition()
    val hapticFeedback = LocalHapticFeedback.current
    // Animate scale of the glow effect
    val pulsarScale = infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 2f, // Glow expands outward
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    // Animate alpha of the glow effect
    val pulsarAlpha = infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0f, // Glow fades out
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    // Centered layout for the FAB and pulsar effect
    Box(
        contentAlignment = Alignment.Center

    ) {
        Box(
            modifier = Modifier
                .size(50.dp) // Initial size of the glow
                .graphicsLayer(
                    scaleX = pulsarScale.value,
                    scaleY = pulsarScale.value,
                    alpha = pulsarAlpha.value // Dynamic alpha for fade-out effect
                )
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                )
        )

        // Floating Action Button
        FloatingActionButton(
            onClick = {
                onBtnClicked()
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            },
            modifier = Modifier.size(70.dp),
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Add",
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}