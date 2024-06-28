package com.example.kotlinproject.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ProgressingScreen(fitTimerViewModel: FitTimerViewModel = viewModel()) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val fitTimerState = fitTimerViewModel.uiState.collectAsState()
        Text(text = "Round: ${fitTimerState.value.currentRound}/${fitTimerState.value.numberOfRounds}")
        Text(text = fitTimerState.value.workState.name)
    }
}