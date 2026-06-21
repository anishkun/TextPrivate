package com.anishkun.hidetext.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey val phoneNumber: String,
    val displayName: String,
    val publicKey: String?
)
