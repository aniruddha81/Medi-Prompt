package com.aniruddha81.mediprompt.pages.alarmPage

import android.app.TimePickerDialog
import android.os.Handler
import android.os.Looper
import android.text.format.DateFormat
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.aniruddha81.mediprompt.viewModels.AlarmActivityViewModel
import java.util.Calendar

@Composable
fun AddAlarmDialog(
    viewModel: AlarmActivityViewModel,
    onSave: (title: String, message: String, timeInMillis: Long) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    var showTimePicker by remember { mutableStateOf(false) }
    val context = LocalContext.current


    var calendar by remember {
        mutableStateOf(Calendar.getInstance().apply {
            add(Calendar.HOUR, 1)
            set(Calendar.SECOND, 0)
        })
    }


    val timePickerDialog = remember {
        TimePickerDialog(
            context,
            { _, selectedHour, selectedMinute ->
                calendar = (calendar.clone() as Calendar).apply {
                    set(Calendar.HOUR_OF_DAY, selectedHour)
                    set(Calendar.MINUTE, selectedMinute)
                    set(Calendar.SECOND, 0)
                }
//                showTimePicker = false
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        )
    }

    // Ensure the TimePicker is shown after recomposition
    LaunchedEffect(showTimePicker) {
        if (showTimePicker) {
            Handler(Looper.getMainLooper()).post {
                timePickerDialog.show()
            }
        }
    }

    if (viewModel.showAlertDialog.value) {
        AlertDialog(
            onDismissRequest = { viewModel.showAlertDialog.value = false },
            title = { Text("Create Alarm") },
            text = {
                Column {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = message,
                        onValueChange = { message = it },
                        label = { Text("Message") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showTimePicker = true }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Time Icon"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = DateFormat.format("hh:mm a", calendar).toString()
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (title.isBlank() || message.isBlank()) {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            onSave(title, message, calendar.timeInMillis)
                            viewModel.showAlertDialog.value = false
                            title = ""
                            message = ""
                            calendar = Calendar.getInstance().apply {
                                add(Calendar.HOUR, 1)
                                set(Calendar.SECOND, 0)
                            }
                        }
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.showAlertDialog.value = false
                    title = ""
                    message = ""
                    calendar = Calendar.getInstance().apply {
                        add(Calendar.HOUR, 1)
                        set(Calendar.SECOND, 0)
                    }
                }) {
                    Text("Cancel")
                }
            }
        )
    }
}
