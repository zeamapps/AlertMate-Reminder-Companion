package com.zeamapps.snoozy.presentation.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import com.zeamapps.snoozy.notification.openNotificationSettings
import com.zeamapps.snoozy.presentation.viewmodel.ThemeMode
import com.zeamapps.snoozy.presentation.viewmodel.ThemeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    themeViewModel: ThemeViewModel,
    onNotificationSettingsClick: () -> Unit = {},
    onThemeSettingsClick: () -> Unit = {},
    onAboutClick: () -> Unit = {},
    onPrivacyPolicyClick: () -> Unit = {}
) {
    var showThemeSelector = remember { mutableStateOf(false) }
    var context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Settings") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                SettingItem(
                    title = "Notifications",
                    description = "Manage notification preferences",
                    icon = Icons.Default.Notifications,
                    onClick = { openNotificationSettings(context) }
                )
                Divider()
                SettingItem(
                    title = "Theme",
                    description = "Customize app appearance",
                    icon = Icons.Default.Palette,
                    onClick = { showThemeSelector.value = true }
                )
                Divider()
                SettingItem(
                    title = "Privacy Policy",
                    description = "Learn how we protect your data",
                    icon = Icons.Default.Security,
                    onClick = {
                        val url = "https://www.freeprivacypolicy.com/live/2a19bc85-514b-4c0f-9f2a-7fde1d0172fd"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    }
                )
                Divider()
                SettingItem(
                    title = "About",
                    description = "App version and developer info",
                    icon = Icons.Default.Info,
                    onClick = onAboutClick
                )
            }
        }
    )

    if (showThemeSelector.value) {
        ThemeSelectorDialog(viewModel = themeViewModel) {
            showThemeSelector.value = false
        }
    }
}

@Composable
fun SettingItem(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            )
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}


@Composable
fun ThemeSelectorDialog(viewModel: ThemeViewModel, onDismiss: () -> Unit) {
    val themeMode = viewModel.readTheme().collectAsState(ThemeMode.SYSTEM_DEFAULT)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Theme Mode") },
        text = {
            ThemeModeOptions(
                currentTheme = themeMode.value,
                onThemeSelected = { selectedMode ->
                    viewModel.saveTheme(selectedMode)
                    onDismiss()
                }
            )
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
            ) {
                Text("OK")
            }
        }
    )
}

@Composable
fun ThemeModeOptions(
    currentTheme: ThemeMode,
    onThemeSelected: (ThemeMode) -> Unit
) {
    Column {
        ThemeMode.entries.forEach { mode ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onThemeSelected(mode) }
                    .padding(vertical = 4.dp)
            ) {
                RadioButton(
                    selected = mode == currentTheme,
                    onClick = { onThemeSelected(mode) }
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(text = mode.name.replace("_", " ")) // Make name user-friendly
            }
        }
    }
}



