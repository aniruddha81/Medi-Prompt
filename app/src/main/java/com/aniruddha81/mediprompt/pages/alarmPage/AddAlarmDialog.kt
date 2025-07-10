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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aniruddha81.mediprompt.viewModels.AlarmActivityViewModel
import java.util.Calendar

@Composable
fun AddAlarmDialog(
    viewModel: AlarmActivityViewModel,
    onSave: (title: String, message: String, timeInMillis: Long) -> Unit
) {
    // Create state variables that persist across recompositions
    val dialogVisible = viewModel.showAlertDialog.value

    // Only initialize these states when the dialog is visible to prevent state resets
    if (dialogVisible) {
        var title by remember { mutableStateOf("") }
        var message by remember { mutableStateOf("") }
        var showTimePicker by remember { mutableStateOf(false) }
        val context = LocalContext.current

        // Calendar state for the time picker
        var calendar by remember {
            mutableStateOf(Calendar.getInstance().apply {
                add(Calendar.HOUR, 1)
                set(Calendar.SECOND, 0)
            })
        }

        // Create the time picker dialog with proper dismissal handling
        val timePickerDialog = remember {
            TimePickerDialog(
                context,
                { _, selectedHour, selectedMinute ->
                    calendar = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, selectedHour)
                        set(Calendar.MINUTE, selectedMinute)
                        set(Calendar.SECOND, 0)
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(context)
            ).apply {
                // Add an OnDismissListener to reset the showTimePicker state
                setOnDismissListener {
                    showTimePicker = false
                }
            }
        }

        // Launch time picker when showTimePicker becomes true
        LaunchedEffect(showTimePicker) {
            if (showTimePicker) {
                Handler(Looper.getMainLooper()).post {
                    if (!timePickerDialog.isShowing) {
                        timePickerDialog.show()
                    }
                }
            }
        }

        // Clean up resources when dialog is dismissed
        DisposableEffect(Unit) {
            onDispose {
                if (timePickerDialog.isShowing) {
                    timePickerDialog.dismiss()
                }
            }
        }

        AlertDialog(
            onDismissRequest = {
                viewModel.showAlertDialog.value = false
            },
            title = {
                Text(
                    "Create Medication Reminder",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            },
            text = {
                Column(
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Medication Name") },
                        placeholder = { Text("Enter medication name") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        value = message,
                        onValueChange = { message = it },
                        label = { Text("Instructions") },
                        placeholder = { Text("Enter dosage instructions") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        ),
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { showTimePicker = true },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = "Set Time",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = DateFormat.format("hh:mm a", calendar).toString(),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (title.isBlank() || message.isBlank()) {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            // Call onSave with current values, then reset dialog state
                            onSave(title, message, calendar.timeInMillis)
                            viewModel.showAlertDialog.value = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.showAlertDialog.value = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
