package com.anishkun.hidetext.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val myPhoneNumber: Flow<String?>
    suspend fun setMyPhoneNumber(phoneNumber: String)
}
