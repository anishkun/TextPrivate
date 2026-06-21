package com.anishkun.hidetext.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anishkun.hidetext.domain.model.Message

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: String,
    val senderId: String,
    val encryptedContent: String,
    val timestamp: Long,
    val isFromMe: Boolean
)

fun MessageEntity.toDomainMessage(): Message {
    return Message(
        id = id,
        senderId = senderId,
        encryptedContent = encryptedContent,
        timestamp = timestamp,
        isFromMe = isFromMe
    )
}

fun Message.toEntity(): MessageEntity {
    return MessageEntity(
        id = id,
        senderId = senderId,
        encryptedContent = encryptedContent,
        timestamp = timestamp,
        isFromMe = isFromMe
    )
}
