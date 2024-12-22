package com.zeamapps.snoozy.presentation.addreminder

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zeamapps.snoozy.presentation.MainViewModel
import com.zeamapps.snoozy.presentation.components.OutlinedTextViewDesign
import com.zeamapps.snoozy.presentation.components.StyledButton

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CustomReminder(mainViewModel: MainViewModel, onBtnClick: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
        ) {
            OutlinedTextViewDesign(mainViewModel)
            StyledButton(
                buttonText = "Create Reminder",
                modifier = Modifier
                    .padding(20.dp)
                    .height(50.dp),
            ) {
                onBtnClick()
            }
        }
}