package com.anishkun.hidetext.presentation.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anishkun.hidetext.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetupViewModel @Inject constructor(
    private val userPrefs: UserPreferencesRepository
) : ViewModel() {

    fun savePhoneNumber(phoneNumber: String) {
        viewModelScope.launch {
            userPrefs.setMyPhoneNumber(phoneNumber)
        }
    }
}
