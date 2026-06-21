package com.anishkun.hidetext.domain.model

data class Message(
    val id: String,
    val senderId: String,
    val encryptedContent: String,
    val timestamp: Long,
    val isFromMe: Boolean
)
