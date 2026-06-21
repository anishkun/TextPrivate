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

@HiltViewModel
class ContactListViewModel @Inject constructor(
    private val contactRepository: ContactRepository
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
            contactRepository.addContact(phoneNumber, displayName)
        }
    }
}
