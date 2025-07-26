package com.example.ai_study_assistant.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai_study_assistant.ai.GeminiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AIViewModel : ViewModel() {

    private val geminiService = GeminiService()

    private val _userInput = MutableStateFlow("")
    val userInput: StateFlow<String> = _userInput

    private val _aiResponse = MutableStateFlow("")
    val aiResponse: StateFlow<String> = _aiResponse

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun onInputChanged(newInput: String) {
        _userInput.value = newInput
    }

    fun sendPrompt() {
        val prompt = _userInput.value
        if (prompt.isNotBlank()) {
            viewModelScope.launch {
                _isLoading.value = true
                try {
                    val response = geminiService.generateAnswer(prompt)
                    _aiResponse.value = response
                } catch (e: Exception) {
                    _aiResponse.value = "Error: ${e.localizedMessage}"
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    fun clearResponse() {
        _aiResponse.value = ""
    }

    fun clearInput() {
        _userInput.value = ""
    }
}