package com.zeamapps.snoozy.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.zeamapps.snoozy.presentation.viewmodel.ThemeMode

import com.zeamapps.snoozy.presentation.viewmodel.ThemeViewModel
import com.zeamapps.snoozy.ui.theme.MyAppTheme

import com.zeamapps.snoozy.ui.theme.SnoozyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        val viewModel = viewModels<MainViewModel>()
        val themeViewModel = viewModels<ThemeViewModel>()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SnoozyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyAppTheme(
                        themeViewModel.value.readTheme()
                            .collectAsState(ThemeMode.SYSTEM_DEFAULT).value
                    ) {
                        ReminderAppNavigation(
                            viewModel = viewModel,
                            themeViewModel = themeViewModel.value
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier, Color.DarkGray
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SnoozyTheme {
        Greeting("Android")
    }
}


//@Composable
//fun addReminderPage(value: MainViewModel, innerPadding: PaddingValues) {
//
//
//    val selectedTab = remember { mutableStateOf("NLP") }
//
//    val selectedTabIndex = if (selectedTab.value == "NLP") 0 else 1
//
//    Column(verticalArrangement = Arrangement.Center, modifier = Modifier.padding(innerPadding)) {
//        TabRow(
//            selectedTabIndex = selectedTabIndex,
//            containerColor = MaterialTheme.colorScheme.surface,
//            contentColor = MaterialTheme.colorScheme.primary,
//            modifier = Modifier.padding(horizontal = 16.dp).height(50.dp).clip(RoundedCornerShape(22.dp))
//        ) {
//            // NLP-Based Tab
//            Tab(
//                selected = selectedTab.value == "NLP",
//                onClick = { selectedTab.value = "NLP" },
//                modifier = Modifier.padding(vertical = 8.dp).height(50.dp)
//            ) {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.Center,
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.AllInclusive, // Replace with an NLP-related icon
//                        contentDescription = "NLP-Based Reminder",
//                        modifier = Modifier.size(20.dp),
//                        tint = if (selectedTab.value == "NLP") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text(
//                        "NLP-Based",
//                        color = if (selectedTab.value == "NLP") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
//                        style = MaterialTheme.typography.bodyLarge
//                    )
//                }
//            }
//
//            // Custom Tab
//            Tab(
//                selected = selectedTab.value == "Custom",
//                onClick = { selectedTab.value = "Custom" },
//                modifier = Modifier.padding(vertical = 8.dp).height(50.dp)
//            ) {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.Center,
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Edit, // Replace with a custom-related icon
//                        contentDescription = "Custom Reminder",
//                        modifier = Modifier.size(20.dp),
//                        tint = if (selectedTab.value == "Custom") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text(
//                        "Custom",
//                        color = if (selectedTab.value == "Custom") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
//                        style = MaterialTheme.typography.bodyLarge
//                    )
//                }
//            }
//        }
//        if (selectedTab.value == "NLP") {
//            NlpReminderSection()
//        } else {
//            CustomReminderSection(value)
//        }
//    }
//}
//
//@Composable
//fun CustomReminderSection(mainViewModel: MainViewModel) {
//    TextField(
//        value = mainViewModel.reminderTittle.value,
//        onValueChange = { mainViewModel.onReminderTitleChange(it) },
//        placeholder = { Text("Enter reminder title") }
//    )
//
//
//}
//
//@Composable
//fun NlpReminderSection() {
//    val nlpInput = remember { mutableStateOf("") }
//    val parsedReminder = remember { mutableStateOf<Reminder?>(null) }
//
//    TextField(
//        value = nlpInput.value,
//        onValueChange = { nlpInput.value = it },
//        placeholder = { Text("Type your reminder here...") }
//    )
//
//    Button(onClick = {
//        // Call NLP model and parse input
//        //   parsedReminder.value = Reminder(0L,"Hello")
//    }) {
//        Text("Parse Reminder")
//    }
//
//    parsedReminder.value?.let {
//        Text("Title: ${it.tittle}, Date: ${it.time}, Time: ${it.time}")
//        Button(onClick = { }) { Text("Save Reminder") }
//    }
//}
//

