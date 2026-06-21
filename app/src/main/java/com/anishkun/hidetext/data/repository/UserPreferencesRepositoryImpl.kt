package com.anishkun.hidetext.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.anishkun.hidetext.domain.repository.UserPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

@Singleton
class UserPreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : UserPreferencesRepository {

    private val dataStore = context.dataStore

    companion object {
        val MY_PHONE_NUMBER = stringPreferencesKey("my_phone_number")
    }

    override val myPhoneNumber: Flow<String?> = dataStore.data.map { preferences ->
        preferences[MY_PHONE_NUMBER]
    }

    override suspend fun setMyPhoneNumber(phoneNumber: String) {
        dataStore.edit { preferences ->
            preferences[MY_PHONE_NUMBER] = phoneNumber
        }
    }
}
