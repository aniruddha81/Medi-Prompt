package com.aniruddha81.mediprompt.pages.aiAsstPage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aniruddha81.mediprompt.viewModels.ChatViewModel

@Composable
fun AiAsst(modifier: Modifier = Modifier) {
    val chatViewModel = viewModel<ChatViewModel>()
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            MessageList(
                modifier = Modifier.weight(1f),
                messageList = chatViewModel.messageList
            )
            MessageInput(onMessageSend = { chatViewModel.sendMessage(it) })
        }
    }
}





