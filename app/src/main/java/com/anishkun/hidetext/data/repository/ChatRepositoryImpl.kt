package com.anishkun.hidetext.data.repository

import com.anishkun.hidetext.data.local.dao.MessageDao
import com.anishkun.hidetext.data.local.entity.toDomainMessage
import com.anishkun.hidetext.data.local.entity.toEntity
import com.anishkun.hidetext.data.local.entity.MessageEntity
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
            webSocketClient.incomingMessages.collect { payload ->
                try {
                    val json = org.json.JSONObject(payload)
                    val senderPhone = json.getString("senderPhone")
                    val content = json.getString("encryptedContent")
                    val timestamp = json.getLong("timestamp")

                    val receivedMessage = Message(
                        id = java.util.UUID.randomUUID().toString(),
                        senderId = senderPhone,
                        contactPhoneNumber = senderPhone, // The contact who sent it to us
                        encryptedContent = content,
                        timestamp = timestamp,
                        isFromMe = false
                    )
                    messageDao.insertMessage(receivedMessage.toEntity())
                } catch (e: Exception) {
                    // Not valid JSON or missing fields
                }
            }
        }
    }

    override fun getMessagesForContact(phoneNumber: String): Flow<List<Message>> {
        return messageDao.getMessagesForContact(phoneNumber).map { entities ->
            entities.map { it.toDomainMessage() }
        }
    }

    override suspend fun sendMessage(message: Message) {
        // Send via WebSocket (JSON or delimited string). The server expects JSON `ChatMessage`.
        // We'll update this shortly to send properly formatted JSON.
        webSocketClient.sendMessage("{\"receiverPhone\":\"${message.contactPhoneNumber}\", \"senderPhone\":\"${message.senderId}\", \"encryptedContent\":\"${message.encryptedContent}\", \"timestamp\":${message.timestamp}}")
    }

    override suspend fun insertMessageLocally(message: Message) {
        messageDao.insertMessage(
            MessageEntity(
                id = message.id,
                senderId = message.senderId,
                contactPhoneNumber = message.contactPhoneNumber,
                encryptedContent = message.encryptedContent,
                timestamp = message.timestamp,
                isFromMe = message.isFromMe
            )
        )
    }

    override fun connectWebSocket(myPhoneNumber: String) {
        webSocketClient.connect(myPhoneNumber)
    }

    override fun disconnectWebSocket() {
        webSocketClient.disconnect()
    }
}
