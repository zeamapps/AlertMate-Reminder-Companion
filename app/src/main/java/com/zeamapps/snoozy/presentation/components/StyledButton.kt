package com.zeamapps.snoozy.presentation.components


import android.widget.Button
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StyledButton(modifier: Modifier = Modifier, buttonText: String, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFFFB3FF),
                        Color(0xFFFFB3B3)
                    )
                ),
                shape = RoundedCornerShape(26.dp)
            )
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxSize(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues() // Remove default padding for proper alignment
        ) {
            Text(
                text = buttonText,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterVertically), fontWeight = FontWeight.SemiBold, fontSize = 15.sp
            )
        }
    }
}




@Composable
@Preview
fun buttonPrev() {
    StyledButton(buttonText = "Hello", onClick = {})
}