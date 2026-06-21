package com.anishkun.hidetext.presentation.chat

import com.anishkun.hidetext.domain.model.Message

data class ChatState(
    val messages: List<Message> = emptyList(),
    val messageInput: String = ""
)
