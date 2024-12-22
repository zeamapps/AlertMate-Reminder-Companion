package com.zeamapps.snoozy.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.zeamapps.snoozy.utill.Constants
import com.zeamapps.snoozy.utill.SnoozyColors
import kotlin.text.contains

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(title: String, onSettingsClicked: () -> Unit = {}) {
    var showBackButton = !title.contains(Constants.APP_NAME)
    TopAppBar(
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                overflow = TextOverflow.Companion.Ellipsis,
                modifier = Modifier.padding(start = 0.dp)
            )
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack, // Use a more appropriate icon
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground // Match the title color
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.background),
        actions = {
            IconButton(onClick = onSettingsClicked) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }

        }
    )
}








