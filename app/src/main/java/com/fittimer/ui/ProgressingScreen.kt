package com.fittimer.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fittimer.R

@Composable
fun ProgressingScreen(fitTimerViewModel: FitTimerViewModel = viewModel()) {
    val scrollState = rememberScrollState()
    val fitTimerState = fitTimerViewModel.uiState.collectAsState()
    Column(
        verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp)
            .verticalScroll(scrollState)
    ) {
        LaunchedEffect(Unit) {
            fitTimerViewModel.updateCurrentStateTime()
            fitTimerViewModel.startTimer()
        }
        Text(
            text = "Round: ${fitTimerState.value.currentRound}/${fitTimerState.value.numberOfRounds}",
            fontSize = 34.sp
        )
        Text(
            text = if (fitTimerState.value.clockState == FitTimerClockState.Progressing) fitTimerState.value.workState.name else "Pause",
            fontSize = 26.sp
        )
        Row {
            val fontSize = 28.sp
            Text(text = fitTimerState.value.currentTime.formatedMinutes(), fontSize = fontSize)
            Text(text = ":", fontSize = fontSize)
            Text(text = fitTimerState.value.currentTime.formatedSeconds(), fontSize = fontSize)
        }
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = { fitTimerViewModel.goBackRound() }) {
                Icon(
                    painter = painterResource(id = R.drawable.skip_previous_24px),
                    contentDescription = "Skip Previous button",
                    modifier = Modifier.size(36.dp)
                )
            }
            Button(
                onClick = {
                    if (fitTimerState.value.clockState == FitTimerClockState.Progressing) {
                        fitTimerViewModel.pauseTimer()
                    } else {
                        fitTimerViewModel.resumeTimer()
                    }
                }, colors = ButtonColors(
                    colorResource(id = R.color.primary),
                    colorResource(id = R.color.white),
                    colorResource(id = R.color.primary),
                    colorResource(id = R.color.primary),
                ), modifier = Modifier
                    .width(140.dp)
                    .height(48.dp)
            ) {
                Text(
                    text = if (fitTimerState.value.clockState == FitTimerClockState.Progressing) "Pause" else "Resume",
                    fontSize = 24.sp
                )
            }
            IconButton(onClick = {
                fitTimerViewModel.nextRound()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.skip_next_24px),
                    contentDescription = "Skip Next button",
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}