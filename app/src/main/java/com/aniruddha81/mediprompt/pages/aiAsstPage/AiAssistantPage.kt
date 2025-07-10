package com.aniruddha81.mediprompt.pages.aiAsstPage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aniruddha81.mediprompt.viewModels.ChatViewModel

@Composable
fun AiAsst(modifier: Modifier = Modifier) {
    val chatViewModel = viewModel<ChatViewModel>()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Title at the top
            Text(
                text = "AI Assistant",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 8.dp)
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.background.copy(alpha = 0.7f)
                    )
            ) {
                // Message List
                MessageList(
                    modifier = Modifier.fillMaxSize(),
                    messageList = chatViewModel.messageList
                )
            }

            // Message Input at the bottom
            MessageInput(
                onMessageSend = { chatViewModel.sendMessage(it) }
            )
        }
    }
}
