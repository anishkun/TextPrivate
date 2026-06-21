package com.anishkun.hidetext.presentation.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anishkun.hidetext.data.local.entity.ContactEntity
import com.anishkun.hidetext.domain.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.anishkun.hidetext.data.remote.api.HideTextApi
import timber.log.Timber

@HiltViewModel
class ContactListViewModel @Inject constructor(
    private val contactRepository: ContactRepository,
    private val api: HideTextApi
) : ViewModel() {

    private val _contacts = MutableStateFlow<List<ContactEntity>>(emptyList())
    val contacts: StateFlow<List<ContactEntity>> = _contacts.asStateFlow()

    init {
        contactRepository.getAllContacts()
            .onEach { _contacts.value = it }
            .launchIn(viewModelScope)
    }

    fun addContact(phoneNumber: String, displayName: String) {
        viewModelScope.launch {
            try {
                // Fetch public key from backend
                val response = api.getPublicKey(phoneNumber)
                val publicKey = if (response.isSuccessful) {
                    response.body()?.publicKey
                } else {
                    Timber.w("Failed to fetch public key for $phoneNumber")
                    null
                }
                
                // Save contact
                contactRepository.addContact(phoneNumber, displayName, publicKey)
            } catch (e: Exception) {
                Timber.e(e, "Error adding contact")
                contactRepository.addContact(phoneNumber, displayName, null)
            }
        }
    }
}
