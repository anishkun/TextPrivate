package com.anishkun.hidetext.presentation.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anishkun.hidetext.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.anishkun.hidetext.domain.crypto.CryptoManager
import com.anishkun.hidetext.data.remote.api.HideTextApi
import com.anishkun.hidetext.data.remote.api.RegisterRequest
import timber.log.Timber

@HiltViewModel
class SetupViewModel @Inject constructor(
    private val userPrefs: UserPreferencesRepository,
    private val cryptoManager: CryptoManager,
    private val api: HideTextApi
) : ViewModel() {

    fun savePhoneNumber(phoneNumber: String) {
        viewModelScope.launch {
            try {
                // Generate RSA Keys locally
                val publicKey = cryptoManager.generateKeys()
                
                // Register with backend
                val response = api.registerPublicKey(RegisterRequest(phoneNumber, publicKey))
                if (response.isSuccessful) {
                    // Only save locally if server registration succeeded
                    userPrefs.setMyPhoneNumber(phoneNumber)
                } else {
                    Timber.e("Failed to register with server: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Timber.e(e, "Error during setup")
            }
        }
    }
}
