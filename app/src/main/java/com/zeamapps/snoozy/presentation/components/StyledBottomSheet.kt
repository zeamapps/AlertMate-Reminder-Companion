//package com.zeamapps.snoozy.presentation.components
//
//import android.util.Log
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Button
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.ModalBottomSheet
//import androidx.compose.material3.Text
//import androidx.compose.material3.rememberModalBottomSheetState
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import com.zeamapps.snoozy.data.models.Reminder
//import com.zeamapps.snoozy.presentation.MainViewModel
//import com.zeamapps.snoozy.presentation.addreminder.AddReminder
//import com.zeamapps.snoozy.presentation.viewmodel.ReminderViewModel
//import com.zeamapps.snoozy.utill.DateFormatHandler
//import kotlinx.coroutines.launch
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ModalBottomSheetDesign(
//    onDismiss: () -> Unit,
//    mainViewModel: MainViewModel,
//    reminderViewModel: ReminderViewModel
//) {
////    remember { mutableStateOf(true) }
////    ModalBottomSheet(onDismissRequest =
////    { onDismiss() }, dragHandle = {}, modifier = Modifier.fillMaxHeight()
////    ) {
////        //  AddReminder(mainViewModel, reminderViewModel,{ onDismiss() })
//
//
//    var coroutineScope = rememberCoroutineScope()
//
//    AddReminder(mainViewModel, reminderViewModel, {
//        onDismiss()
//    }, {
//        val reminderTimeStamp = DateFormatHandler().mergeDateAndTime(
//            mainViewModel.date.value,
//            mainViewModel.time.value
//        )
//        val reminder = Reminder(
//            tittle = mainViewModel.reminderTittle.value,
//            description = mainViewModel.reminderDesc.value,
//            time = reminderTimeStamp,
//            tagColor = mainViewModel.tagColor.value.value.toLong()
//        )
//        Log.d(
//            "Reminder",
//            "ReminderCurrentTime : " + reminderTimeStamp + " : " + mainViewModel.time.value
//        )
//        reminderViewModel.insertReminder(reminder)
//        //   Toast.makeText(localContext, "Reminder Added.", Toast.LENGTH_SHORT).show()
//        onDismiss()
//    }, 0L)
//}
////    }
////}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun FullScreenModalBottomSheet(onDismiss: () -> Unit) {
//    val sheetState = rememberModalBottomSheetState()
//    val scope = rememberCoroutineScope()
//
//    // ModalBottomSheet
//    ModalBottomSheet(
//        onDismissRequest = { onDismiss() }, // Handle dismiss action
//        sheetState = sheetState,
//        dragHandle = {},                    // Remove drag handle if unnecessary
//        modifier = Modifier.fillMaxHeight() // Ensure full height
//    ) {
//        // AddReminder(mainViewModel = MainViewModel) // Content of the modal
//    }
//
//    // Trigger to show the bottom sheet
//    Button(
//        onClick = {
//            scope.launch {
//                sheetState.show() // Opens the sheet in a fully expanded state
//            }
//        },
//        modifier = Modifier.padding(16.dp)
//    ) {
//        Text("Open Full-Screen Bottom Sheet")
//    }
//}
//
//
//
