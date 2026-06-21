package com.anishkun.hidetext.domain.repository

import com.anishkun.hidetext.data.local.entity.ContactEntity
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    fun getAllContacts(): Flow<List<ContactEntity>>
    suspend fun addContact(phoneNumber: String, displayName: String)
}
