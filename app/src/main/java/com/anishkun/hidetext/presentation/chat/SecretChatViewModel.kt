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
import javax.inject.Inject

import androidx.lifecycle.SavedStateHandle
import com.anishkun.hidetext.domain.crypto.CryptoManager
import com.anishkun.hidetext.domain.repository.UserPreferencesRepository
import com.anishkun.hidetext.domain.repository.ContactRepository
import kotlinx.coroutines.flow.first
import timber.log.Timber

@HiltViewModel
class SecretChatViewModel @Inject constructor(
    private val repository: ChatRepository,
    private val userPrefs: UserPreferencesRepository,
    private val contactRepository: ContactRepository,
    private val cryptoManager: CryptoManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val contactPhoneNumber: String = checkNotNull(savedStateHandle["contactPhoneNumber"])
    private var contactPublicKey: String? = null
    private var myPhoneNumber: String = ""

    private val _state = MutableStateFlow(ChatState())
    val state: StateFlow<ChatState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            myPhoneNumber = userPrefs.myPhoneNumber.first() ?: ""
            repository.connectWebSocket(myPhoneNumber)
            
            // Fetch contact's public key from local DB
            val contact = contactRepository.getAllContacts().first().find { it.phoneNumber == contactPhoneNumber }
            contactPublicKey = contact?.publicKey
        }
        
        repository.getMessagesForContact(contactPhoneNumber)
            .onEach { messages ->
                // Decrypt incoming messages
                val decryptedMessages = messages.map { msg ->
                    if (!msg.isFromMe) {
                        try {
                            val decrypted = cryptoManager.decrypt(msg.encryptedContent)
                            msg.copy(encryptedContent = decrypted) // We temporarily abuse encryptedContent field to show plain text in UI
                        } catch (e: Exception) {
                            msg.copy(encryptedContent = "[Decryption Failed]")
                        }
                    } else {
                        // For messages sent by us, they are already plain text in the DB (for this prototype)
                        msg
                    }
                }
                _state.update { it.copy(messages = decryptedMessages) }
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
        
        val publicKey = contactPublicKey
        if (publicKey == null) {
            // Can't encrypt if no public key
            Timber.e("No public key for contact")
            return
        }

        val encryptedContent = cryptoManager.encrypt(currentInput, publicKey)

        val message = Message(
            id = java.util.UUID.randomUUID().toString(),
            senderId = myPhoneNumber,
            contactPhoneNumber = contactPhoneNumber,
            encryptedContent = currentInput, // Store plain text locally for 'isFromMe'
            timestamp = System.currentTimeMillis(),
            isFromMe = true
        )
        
        viewModelScope.launch {
            repository.sendMessage(message.copy(encryptedContent = encryptedContent)) // Send encrypted version
            repository.insertMessageLocally(message) // Store unencrypted version for sender
            _state.update { it.copy(messageInput = "") }
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.disconnectWebSocket()
    }
}
