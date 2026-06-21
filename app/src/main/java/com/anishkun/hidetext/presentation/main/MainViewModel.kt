package com.anishkun.hidetext.presentation.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    private var currentInput = ""
    private val secretPin = "1337="

    fun onCalculatorInput(input: String) {
        currentInput += input
        
        // Simple functional calculator logic for the decoy display
        val newDisplay = when (input) {
            "C" -> {
                currentInput = ""
                "0"
            }
            "=" -> {
                // In a real calculator, we'd evaluate the expression here.
                // For the decoy, we just show "Error" or the input itself unless it's the PIN.
                if (currentInput == secretPin) {
                    "UNLOCKED"
                } else {
                    currentInput = "" // Reset after pressing =
                    "0"
                }
            }
            else -> currentInput
        }

        // Check gatekeeper condition
        if (currentInput == secretPin) {
            _state.update { it.copy(appMode = AppMode.SECRET, calculatorDisplay = "") }
            currentInput = "" // Reset for security
        } else {
            // Cap the input length so we don't hold infinite memory
            if (currentInput.length > 20) currentInput = ""
            _state.update { it.copy(calculatorDisplay = newDisplay) }
        }
    }
}
