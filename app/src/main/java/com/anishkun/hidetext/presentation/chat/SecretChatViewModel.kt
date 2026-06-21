package com.anishkun.hidetext.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anishkun.hidetext.domain.model.Message
import com.anishkun.hidetext.domain.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SecretChatViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ChatState())
    val state: StateFlow<ChatState> = _state.asStateFlow()

    init {
        repository.connectWebSocket()
        
        repository.getAllMessages()
            .onEach { messages ->
                _state.update { it.copy(messages = messages) }
            }
            .launchIn(viewModelScope)
    }

    fun handleIntent(intent: ChatIntent) {
        when (intent) {
            is ChatIntent.UpdateInput -> {
                _state.update { it.copy(messageInput = intent.text) }
            }
            is ChatIntent.SendMessage -> {
                sendMessage()
            }
        }
    }

    private fun sendMessage() {
        val currentInput = _state.value.messageInput
        if (currentInput.isBlank()) return
        
        val message = Message(
            id = UUID.randomUUID().toString(),
            senderId = "me", // Assuming "me" for local sender
            encryptedContent = currentInput, // In a real app, encrypt before sending
            timestamp = System.currentTimeMillis(),
            isFromMe = true
        )
        
        viewModelScope.launch {
            repository.sendMessage(message)
            _state.update { it.copy(messageInput = "") }
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.disconnectWebSocket()
    }
}
