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

    private val generativeModel: GenerativeModel = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = Constants.API_KEY
    )

    // Medical domain keywords for filtering
    private val medicalKeywords = listOf(
        "medicine", "medical", "health", "doctor", "hospital", "symptom", "disease",
        "treatment", "prescription", "drug", "medication", "pill", "tablet", "capsule",
        "dose", "dosage", "side effect", "pharmacy", "pain", "fever", "cough", "cold",
        "allergy", "vaccine", "insulin", "blood pressure", "cholesterol", "diabetes",
        "antibiotic", "vitamin", "supplement", "diet", "nutrition", "therapy", "diagnosis",
        "emergency", "surgery", "clinic", "remedy", "cure", "healing", "wellness", "patient",
        "doctor", "nurse", "specialist", "physician", "pediatrician", "cardiologist",
        "dermatologist", "neurologist", "oncologist", "psychiatrist", "headache", "migraine",
        "nausea", "dizziness", "vomiting", "diarrhea", "constipation", "infection", "virus",
        "bacteria", "fungal", "parasite", "chronic", "acute", "terminal", "benign", "malignant"
    )

    // Greeting terms that are allowed
    private val greetingTerms = listOf(
        "hi", "hello", "hey", "greetings", "good morning", "good afternoon",
        "good evening", "howdy", "welcome", "hi there", "hello there", "namaste",
        "hola", "bonjour", "ciao"
    )

    /**
     * Check if the question is medical-related or a greeting
     */
    private fun isMedicalOrGreeting(question: String): Boolean {
        val lowerQuestion = question.lowercase()

        // Check if it's a greeting
        for (greeting in greetingTerms) {
            if (lowerQuestion.contains(greeting)) {
                return true
            }
        }

        // Check if it contains medical keywords
        for (keyword in medicalKeywords) {
            if (lowerQuestion.contains(keyword)) {
                return true
            }
        }

        return false
    }

    /**
     * Ask Gemini to determine if a question is medical-related
     * This is a more advanced check using AI itself
     */
    private suspend fun isQuestionMedicalRelated(question: String): Boolean {
        try {
            val classifierModel = GenerativeModel(
                modelName = "gemini-2.5-flash",
                apiKey = Constants.API_KEY
            )

            val prompt = """
                You are a strict classifier that determines if a question is medical-related.
                The question is: "$question"
                
                Respond with ONLY "yes" if the question is related to medicine, healthcare, medical advice, 
                symptoms, treatments, medications, diseases, or any other medical topic.
                
                Respond with ONLY "no" if the question is not related to medical topics.
                
                If the question is a greeting like "hello" or "hi", respond with "greeting".
            """.trimIndent()

            val response = classifierModel.generateContent(prompt)
            val result = response.text?.trim()?.lowercase() ?: "no"

            return result == "yes" || result == "greeting"
        } catch (e: Exception) {
            // If classification fails, fall back to keyword matching
            return isMedicalOrGreeting(question)
        }
    }

    fun sendMessage(question: String) {
        viewModelScope.launch {
            try {
                messageList.add(MessageModel(question, "user"))
                messageList.add(MessageModel("Typing...", "model"))

                // First check if question is medical-related or a greeting
                if (isQuestionMedicalRelated(question)) {
                    val chat = generativeModel.startChat(
                        history = messageList.dropLast(1).map {
                            content(it.role) { text(it.message) }
                        }
                    )

                    val response = chat.sendMessage(question)
                    val responseText = response.text ?: "No response"

                    messageList.removeAt(messageList.lastIndex) // Remove "Typing..."
                    messageList.add(MessageModel(responseText, "model"))
                } else {
                    // If not medical-related, respond with a restriction message
                    messageList.removeAt(messageList.lastIndex) // Remove "Typing..."
                    messageList.add(MessageModel(
                        "I'm sorry, I can only answer medical-related questions. " +
                        "Please ask something about medications, health conditions, or medical advice.",
                        "model"
                    ))
                }
            } catch (e: Exception) {
                if (messageList.isNotEmpty() && messageList.last().message == "Typing...") {
                    messageList.removeAt(messageList.lastIndex)
                }
                messageList.add(MessageModel("Error: ${e.message}", "model"))
            }
        }
    }
}