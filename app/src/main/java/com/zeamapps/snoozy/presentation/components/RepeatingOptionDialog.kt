package com.zeamapps.snoozy.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RepeatingOptionDialog(onDismiss: (RepeatingOptions) -> Unit) {
    var selectedMode = remember { mutableStateOf(RepeatingOptions.DO_NOT_REPEAT) }
    AlertDialog(
        onDismissRequest = { onDismiss(selectedMode.value) },
        title = { Text("Select Repeating Mode") },
        text = {
            RepeatingOption({
                selectedMode.value = it
                onDismiss(it)
            }, currentOption = selectedMode.value)
        },
        confirmButton = {
            Button(
                onClick = { onDismiss(selectedMode.value) },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
            ) {
                Text("OK", color = MaterialTheme.colorScheme.onSecondary)
            }
        }
    )
}

@Composable
fun RepeatingOption(
    onRepeatingOption: (RepeatingOptions) -> Unit,
    currentOption: RepeatingOptions
) {
    Column {
        RepeatingOptions.entries.forEach {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                RadioButton(onClick = { onRepeatingOption(it) }, selected = it == currentOption)
                //  Spacer(Modifier.height(2.dp))
                Text(it.name.replace("_", " "))
            }
        }
    }
}


enum class RepeatingOptions {
    DO_NOT_REPEAT, DAILY, WEEKLY, MONTHLY
}

