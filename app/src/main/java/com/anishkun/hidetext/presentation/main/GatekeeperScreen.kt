package com.anishkun.hidetext.presentation.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.anishkun.hidetext.presentation.decoy.DecoyCalculatorScreen

@Composable
fun GatekeeperScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    AnimatedContent(
        targetState = state.appMode,
        transitionSpec = {
            fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))
        },
        label = "Gatekeeper Transition"
    ) { appMode ->
        when (appMode) {
            AppMode.DECOY -> {
                DecoyCalculatorScreen(
                    displayValue = state.calculatorDisplay,
                    onInput = viewModel::onCalculatorInput
                )
            }
            AppMode.SECRET -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("SECURE CHAT UNLOCKED", fontSize = 24.sp)
                }
            }
        }
    }
}
