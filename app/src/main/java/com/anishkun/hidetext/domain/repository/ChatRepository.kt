package com.anishkun.hidetext.domain.repository

import com.anishkun.hidetext.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getMessagesForContact(phoneNumber: String): Flow<List<Message>>
    suspend fun sendMessage(message: Message)
    fun connectWebSocket()
    fun disconnectWebSocket()
}
