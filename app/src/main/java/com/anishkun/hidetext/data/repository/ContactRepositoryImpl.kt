package com.anishkun.hidetext.data.repository

import com.anishkun.hidetext.data.local.dao.ContactDao
import com.anishkun.hidetext.data.local.entity.ContactEntity
import com.anishkun.hidetext.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepositoryImpl @Inject constructor(
    private val contactDao: ContactDao
) : ContactRepository {
    override fun getAllContacts(): Flow<List<ContactEntity>> {
        return contactDao.getAllContacts()
    }

    override suspend fun addContact(phoneNumber: String, displayName: String) {
        contactDao.insertContact(ContactEntity(phoneNumber, displayName))
    }
}
