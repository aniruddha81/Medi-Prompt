package com.aniruddha81.mediprompt.pages.alarmPage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aniruddha81.mediprompt.viewModels.AlarmActivityViewModel

@Composable
fun AlarmScreen(
    viewModel: AlarmActivityViewModel,
    paddingValues: PaddingValues
) {
    val alarms by viewModel.alarms.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        if (alarms.isEmpty()) {
            // Show empty state
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                EmptyAlarmState()
            }
        } else {
            // Show list of alarms
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(items = alarms, key = { it.id }) { alarm ->
                    SwipeToDeleteContainer(onDelete = { viewModel.deleteAlarmItemById(alarm.id) }) {
                        AlarmItem(
                            alarm = alarm
                        ) {
                            viewModel.deleteAlarmItemById(alarm.id)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyAlarmState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "No Alarms",
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "No medication reminders yet\nTap + to create a reminder",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 60.dp)
            )
        }
    }
}
