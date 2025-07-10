package com.aniruddha81.mediprompt.pages.alarmPage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aniruddha81.mediprompt.alarm.AlarmSchedulerImpl
import com.aniruddha81.mediprompt.models.Alarm
import com.aniruddha81.mediprompt.viewModels.AlarmActivityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier,
    viewModel: AlarmActivityViewModel,
    alarmSchedulerImpl: AlarmSchedulerImpl
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.showAlertDialog.value = true
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Medication Reminder",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    ) { _ ->
        // Using Column instead of Box to properly arrange elements vertically
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
        ) {
            // Simple title bar with just the app name
            Text(
                text = "Medi-Prompt",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth().padding(bottom = 18.dp)
            )

            // Alarm list below the title
            AlarmScreen(
                viewModel = viewModel,
                paddingValues = PaddingValues(0.dp)
            )
        }

        // Dialog for adding alarms
        AddAlarmDialog(viewModel) { title, message, timeInMillis ->
            val currentTime = System.currentTimeMillis()
            if (timeInMillis <= currentTime) {
                // Time is in the past, handle error case if needed
            } else {
                val alarm = Alarm(
                    title = title,
                    message = message,
                    scheduleAt = timeInMillis,
                )
                viewModel.insertAlarm(alarm)
                alarmSchedulerImpl.schedule(alarm)
            }
        }
    }
}