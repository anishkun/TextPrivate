package com.anishkun.hidetext.presentation.main

data class MainState(
    val appMode: AppMode = AppMode.DECOY,
    val calculatorDisplay: String = "0",
    val isSetupComplete: Boolean = false
)
