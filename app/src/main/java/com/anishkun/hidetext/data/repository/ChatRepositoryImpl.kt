package com.anishkun.hidetext.data.repository

import com.anishkun.hidetext.data.local.dao.MessageDao
import com.anishkun.hidetext.data.local.entity.toDomainMessage
import com.anishkun.hidetext.data.local.entity.toEntity
import com.anishkun.hidetext.data.remote.websocket.ChatWebSocketClient
import com.anishkun.hidetext.domain.model.Message
import com.anishkun.hidetext.domain.repository.ChatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val messageDao: MessageDao,
    private val webSocketClient: ChatWebSocketClient,
    private val externalScope: CoroutineScope // Provided via DI for observing webSocket
) : ChatRepository {

    init {
        // Observe incoming websocket messages and save them locally
        externalScope.launch {
            webSocketClient.incomingMessages.collect { encryptedPayload ->
                // In a real app, you would parse the JSON payload and decrypt it here.
                // For now, we simulate receiving an echo back.
                val receivedMessage = Message(
                    id = java.util.UUID.randomUUID().toString(),
                    senderId = "remote_user",
                    encryptedContent = encryptedPayload,
                    timestamp = System.currentTimeMillis(),
                    isFromMe = false
                )
                messageDao.insertMessage(receivedMessage.toEntity())
            }
        }
    }

    override fun getAllMessages(): Flow<List<Message>> {
        return messageDao.getAllMessages().map { entities ->
            entities.map { it.toDomainMessage() }
        }
    }

    override suspend fun sendMessage(message: Message) {
        // 1. Save locally immediately (Optimistic UI update)
        messageDao.insertMessage(message.toEntity())
        
        // 2. Send over WebSocket (In real app, we might serialize the Message object)
        webSocketClient.sendMessage(message.encryptedContent)
    }

    override fun connectWebSocket() {
        webSocketClient.connect()
    }

    override fun disconnectWebSocket() {
        webSocketClient.disconnect()
    }
}
