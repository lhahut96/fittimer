package com.example.kotlinproject.ui

import android.util.Log
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinproject.R
import com.example.kotlinproject.data.TimeUnit
import kotlinx.coroutines.launch


@Composable
fun StartScreen(
    fitTimerViewModel: FitTimerViewModel = viewModel(), onStartButton: () -> Unit = {}
) {
    val fitTimerState = fitTimerViewModel.uiState.collectAsState()

    var repText by remember {
        mutableStateOf(fitTimerState.value.numberOfRounds.toString())
    }

    var workOutMinuteText by remember {
        mutableStateOf(fitTimerState.value.workoutTime.formatedMinutes())
    }

    var workOutSecondText by remember {
        mutableStateOf(fitTimerState.value.workoutTime.formatedSeconds())
    }

    var restMinuteText by remember {
        mutableStateOf(fitTimerState.value.restTime.formatedMinutes())
    }

    var restSecondText by remember {
        mutableStateOf(fitTimerState.value.restTime.formatedSeconds())
    }

    val coroutineScope = rememberCoroutineScope()
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)
    val state = rememberScrollState()

    LaunchedEffect(key1 = keyboardHeight) {
        Log.i("keyboard height", keyboardHeight.toString())
        coroutineScope.launch {
            state.scrollBy(keyboardHeight.toFloat())
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp)
            .verticalScroll(state)
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            FitTimerComponent(
                text = stringResource(id = R.string.rep_string),
                repText,
                increaseHandler = {
                    fitTimerViewModel.increase(FitTimerType.REP)
                    repText = fitTimerState.value.numberOfRounds.toString()
                },
                decreaseHandler = {
                    fitTimerViewModel.decrease(FitTimerType.REP)
                    repText = fitTimerState.value.numberOfRounds.toString()
                },
                onStringChange = {
                    fitTimerViewModel.changeRep(it)
                    repText = it
                },
                isRep = true
            )
            FitTimerComponent(
                text = stringResource(id = R.string.workout_string),
                value = workOutMinuteText,
                secondValue = workOutSecondText,
                increaseHandler = { fitTimerViewModel.increase(FitTimerType.WORKOUT) },
                decreaseHandler = { fitTimerViewModel.decrease(FitTimerType.WORKOUT) },
                onStringChange = {
                    if (it.isNotBlank()) {
                        fitTimerViewModel.changeTimeByString(
                            it,
                            TimeUnit.Minute,
                            FitTimerState.Workout
                        )
                    }
                    workOutMinuteText = it
                }
            )
            FitTimerComponent(
                text = stringResource(id = R.string.rest_string),
                value = restMinuteText,
                secondValue = restSecondText,
                increaseHandler = { fitTimerViewModel.increase(FitTimerType.REST) },
                decreaseHandler = { fitTimerViewModel.decrease(FitTimerType.REST) },
                onStringChange = {
                    if (it.isNotBlank()) {
                        fitTimerViewModel.changeTimeByString(
                            it,
                            TimeUnit.Minute,
                            FitTimerState.Rest
                        )
                    }
                    workOutMinuteText = it
                }
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = onStartButton, colors = ButtonColors(
                colorResource(id = R.color.primary),
                colorResource(id = R.color.white),
                colorResource(id = R.color.primary),
                colorResource(id = R.color.primary),
            ), modifier = Modifier
                .width(130.dp)
                .height(48.dp)
        ) {
            Text(text = "Start", fontSize = 24.sp)
        }
    }
}

@Composable
fun FitTimerComponent(
    text: String = "Default Text",
    value: String = "00:00",
    secondValue: String = "",
    increaseHandler: () -> Unit,
    decreaseHandler: () -> Unit,
    onStringChange: (it: String) -> Unit = {},
    isRep: Boolean = false,
) {

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = text, fontSize = 30.sp, fontWeight = FontWeight.Bold)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { decreaseHandler() }) {
                Icon(
                    painter = painterResource(id = R.drawable.check_indeterminate_small),
                    contentDescription = "Minus Button",
                    modifier = Modifier.size(30.dp)
                )
            }
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                BasicTextField(
                    value = value,
                    onValueChange = {
                        onStringChange(it)
                    },
                    textStyle = TextStyle(fontSize = 24.sp),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(
                        onDone = { Log.i("test", "test") },
                        onGo = { Log.i("test", "go") }),
                    modifier = Modifier
                        .width(IntrinsicSize.Min)
                        .padding(0.dp)
                        .onFocusChanged {

                            if (!it.isFocused) {
                                if (value.isBlank()) {
                                    if (isRep) onStringChange("0") else onStringChange("00")
                                }
                            }
                        },
                    singleLine = true,

                    )
                if (secondValue.isNotBlank()) {
                    Text(text = ":")
                    BasicTextField(
                        value = secondValue,
                        onValueChange = {},
                        textStyle = TextStyle(fontSize = 24.sp),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        keyboardActions = KeyboardActions(onDone = {}),
                        modifier = Modifier
                            .width(IntrinsicSize.Min)
                            .padding(0.dp),
                        singleLine = true
                    )
                }
            }

            IconButton(onClick = { increaseHandler() }) {
                Icon(
                    Icons.Rounded.Add,
                    contentDescription = "Minus Button",
                    modifier = Modifier.size(30.dp)
                )
            }
        }

    }
}