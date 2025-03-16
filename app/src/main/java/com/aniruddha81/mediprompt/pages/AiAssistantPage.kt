package com.aniruddha81.mediprompt.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aniruddha81.mediprompt.viewModels.ChatViewModel

@Composable
fun AiAsst(modifier: Modifier = Modifier) {
    val chatViewModel: ChatViewModel = viewModel()
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ChatPage(chatViewModel)
    }
}