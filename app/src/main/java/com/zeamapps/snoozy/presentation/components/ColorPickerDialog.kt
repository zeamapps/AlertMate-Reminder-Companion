package com.zeamapps.snoozy.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.zeamapps.snoozy.utill.SnoozyColors
import com.zeamapps.snoozy.utill.SnoozyColors.colorCodeList

@Composable
fun ColorPickerDialog(onColorSelected: (Color) -> Unit, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = {}) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .clip(RoundedCornerShape(16.dp)) // Apply rounded corners
                .background(MaterialTheme.colorScheme.background),

            ) {
            Column(
                Modifier
                    .wrapContentSize()
                    .padding(30.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Choose Tag Color",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Medium,
                    fontSize = 17.sp
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier.padding(top = 7.dp)
                ) {
                    items(colorCodeList) {
                        ColorItem(colorCode = it, onColorSelected = onColorSelected)
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp), // Space above the row
                    horizontalArrangement = Arrangement.End // Align content to the right
                ) {
                    Text(
                        text = "Cancel",
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .padding(12.dp)
                            .clickable { onDismiss() }
                    )
                }
            }
        }
    }
}

@Composable
fun ColorItem(colorCode: Color, onColorSelected: (Color) -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .height(50.dp) // or adjust based on content
            .clickable {
                onColorSelected(colorCode)
            }, elevation = CardDefaults.cardElevation(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorCode // ensure parameter name matches
        )
    ) {}
}