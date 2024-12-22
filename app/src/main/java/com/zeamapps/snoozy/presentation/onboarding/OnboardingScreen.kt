package com.zeamapps.snoozy.presentation.onboarding

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeamapps.snoozy.presentation.components.StyledButton



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OnboardingScreen(onBoardingViewModel: OnBoardingViewModel) {

    Scaffold(bottomBar = {
        Box(
            modifier = Modifier
                .padding(bottom = 40.dp, start = 20.dp, end = 20.dp)
                .fillMaxWidth()
                .height(50.dp),

        ) {
            StyledButton(
                modifier = Modifier
                    .fillMaxSize(),
                buttonText = "Get Started",
                {
                    onBoardingViewModel.saveEntry()
                })
        }
    }, contentColor = Color.Black, containerColor = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize().padding(top = 140.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    "Set reminders with just a tap",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center, color = Color.White
                )
            }
            Card(modifier = Modifier.height(500.dp).fillMaxWidth().padding(50.dp)) {

            }
        }
    }
}