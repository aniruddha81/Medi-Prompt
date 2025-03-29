package com.aniruddha81.mediprompt.pages.alarmPage

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.aniruddha81.mediprompt.alarm.AlarmSchedulerImpl
import com.aniruddha81.mediprompt.models.Alarm
import com.aniruddha81.mediprompt.viewModels.AlarmActivityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier,
    viewModel: AlarmActivityViewModel,
    alarmSchedulerImpl : AlarmSchedulerImpl
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.showAlertDialog.value = true
                },
//                        shape = TODO(),
//                        containerColor = TODO(),
//                        contentColor = TODO(),
//                        elevation = TODO(),
//                        interactionSource = TODO()
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Alarm"
                )
            }
        }
    ) { innerPadding ->
        AlarmScreen(viewModel, innerPadding)
        AddAlarmDialog(viewModel) {title, message, timeInMillis ->
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