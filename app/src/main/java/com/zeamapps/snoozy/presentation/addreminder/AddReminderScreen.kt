package com.zeamapps.snoozy.presentation.addreminder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeamapps.snoozy.presentation.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReminder(
    mainViewModel: MainViewModel,
    onClickCancel: () -> Unit = {},
    onClickSave: (Boolean) -> Unit = {},
) {
    val bottomSheetScaffoldState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val reminderTxt = "Set Up Your Reminder"

    ModalBottomSheet(
        onDismissRequest = {
            onClickCancel()
        },
        sheetState = bottomSheetScaffoldState,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = {},
        modifier = Modifier
            .imePadding() // Adjust for the keyboard
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = reminderTxt,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground.copy(0.8f),
                        fontWeight = FontWeight.Medium,
                        fontSize = 17.sp
                    )
                )
//                if (id != 0L) {
//                    Text(
//                        text = "Cancel",
//                        style = MaterialTheme.typography.bodyMedium.copy(
//                            color = MaterialTheme.colorScheme.onBackground,
//                            fontWeight = FontWeight.Normal,
//                            fontSize = 18.sp
//                        ),
//                        modifier = Modifier.clickable(onClick = onClickCancel)
//                    )
//                }

            }
            // Input Field for Title
            // Request focus after BottomSheet animation
            LaunchedEffect(bottomSheetScaffoldState.currentValue) {
                if (bottomSheetScaffoldState.hasExpandedState) {
//                    focusRequester.requestFocus()
//                    keyboardController?.show()
                }
            }
              ReminderContent(mainViewModel, { onClickSave(it) }, onClickCancel)
        }
    }
}


@Composable
fun ReminderContent(value: MainViewModel, onClickSave: (Boolean) -> Unit, onClickCancel : () -> Unit) {
    val selectedTab = remember { mutableStateOf("NLP") }

    val selectedTabIndex = if (selectedTab.value == "NLP") 0 else 1

    TabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = MaterialTheme.colorScheme.onTertiaryContainer,
        contentColor = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .height(50.dp)
            .clip(RoundedCornerShape(22.dp))
    ) {
        // NLP-Based Tab
        Tab(
            selected = selectedTab.value == "NLP",
            onClick = { selectedTab.value = "NLP" },
            modifier = Modifier
                .padding(vertical = 8.dp)
                .height(50.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.AllInclusive, // Replace with an NLP-related icon
                    contentDescription = "NLP-Based Reminder",
                    modifier = Modifier.size(20.dp),
                    tint = if (selectedTab.value == "NLP") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "NLP-Based",
                    color = if (selectedTab.value == "NLP") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        // Custom Tab
        Tab(
            selected = selectedTab.value == "Custom",
            onClick = { selectedTab.value = "Custom" },
            modifier = Modifier
                .padding(vertical = 8.dp)
                .height(50.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Edit, // Replace with a custom-related icon
                    contentDescription = "Custom Reminder",
                    modifier = Modifier.size(20.dp),
                    tint = if (selectedTab.value == "Custom") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Custom",
                    color = if (selectedTab.value == "Custom") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }

    if (selectedTab.value == "NLP") {
        AiReminder(mainViewModel = value, { onClickSave(true) })
    } else {
        CustomReminder(mainViewModel = value, { onClickSave(false) }, onClickCancel)
    }
}
