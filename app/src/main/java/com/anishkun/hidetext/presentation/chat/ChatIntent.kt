package com.anishkun.hidetext.presentation.chat

sealed interface ChatIntent {
    data class UpdateInput(val text: String) : ChatIntent
    data object SendMessage : ChatIntent
    data object ToggleDisguise : ChatIntent
}
