package com.aniruddha81.mediprompt.pages.alarmPage

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.aniruddha81.mediprompt.viewModels.AlarmActivityViewModel

@Composable
fun AlarmScreen(
    viewModel: AlarmActivityViewModel,
    paddingValues: PaddingValues
) {
    val alarms by viewModel.alarms.collectAsState()

    LazyColumn(
        contentPadding = paddingValues,
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(items = alarms, key = { it.id }) {
            SwipeToDeleteContainer(onDelete = { viewModel.deleteAlarmItemById(it.id) }) {
                AlarmItem(
                    alarm = it
                ) {
                    viewModel.deleteAlarmItemById(it.id)
                }
            }
        }
    }
}