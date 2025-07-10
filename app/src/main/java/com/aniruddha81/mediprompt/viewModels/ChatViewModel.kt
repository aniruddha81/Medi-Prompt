package com.aniruddha81.mediprompt.viewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aniruddha81.mediprompt.Constants
import com.aniruddha81.mediprompt.models.MessageModel
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    val messageList = mutableStateListOf<MessageModel>()

    val generativeModel: GenerativeModel = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = Constants.API_KEY
    )

    fun sendMessage(question: String) {
        viewModelScope.launch {
            try {
                val chat = generativeModel.startChat(
                    history = messageList.map {
                        content(it.role) { text(it.message) }
                    }
                )
                messageList.add(MessageModel(question, "user"))
                messageList.add(MessageModel("Typing...", "model"))

                val response = chat.sendMessage(question)
                val responseText = response.text ?: "No response"

                messageList.removeAt(messageList.lastIndex) // Remove "Typing..."

                messageList.add(MessageModel(responseText, "model"))

            } catch (e: Exception) {
                if (messageList.isNotEmpty()) {
                    messageList.removeAt(messageList.lastIndex)
                }
                messageList.add(MessageModel("Error: ${e.message}", "model"))
            }
        }
    }
}