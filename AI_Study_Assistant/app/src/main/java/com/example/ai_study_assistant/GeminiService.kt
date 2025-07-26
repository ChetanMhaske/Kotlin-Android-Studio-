// GeminiService.kt
package com.example.ai_study_assistant.ai

import android.util.Log
import com.example.ai_study_assistant.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig

class GeminiService {

    private val apiKey = BuildConfig.GEMINI_API_KEY

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = apiKey,
        generationConfig = generationConfig {
            temperature = 0.7f
            topK = 40
            topP = 0.95f
            maxOutputTokens = 1024
        }
    )

    suspend fun generateAnswer(prompt: String): String {
        return try {
            if (prompt.isBlank()) {
                return "Please enter a valid question."
            }

            val response = generativeModel.generateContent(prompt)
            response.text?.takeIf { it.isNotBlank() } ?: "No response received from the AI."
        } catch (e: Exception) {
            Log.e("GeminiService", "Error generating answer: ${e.message}", e)
            when {
                e.message?.contains("404") == true ->
                    "Model not found. Please check if the API key is valid and the model is available."
                e.message?.contains("API key") == true ->
                    "Invalid API key. Please check your Gemini API key."
                e.message?.contains("quota") == true ->
                    "API quota exceeded. Please try again later."
                else -> "Error: ${e.localizedMessage ?: "Unknown error occurred"}"
            }
        }
    }
}