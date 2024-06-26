package com.example.kotlinproject.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinproject.R

@Composable
fun FitTimerScreen(fitTimerViewModel: FitTimerViewModel = viewModel()) {
    val fitTimerState = fitTimerViewModel.uiState.collectAsState()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            FitTimerComponent(
                text = stringResource(id = R.string.rep_string),
                fitTimerState.value.numberOfRounds.toString(),
                increaseHandler = { fitTimerViewModel.increase(FitTimerType.REP) },
                decreaseHandler = { fitTimerViewModel.decrease(FitTimerType.REP) },
                onStringChange = fitTimerViewModel.changeWorkOutTimeByString,
            )
            FitTimerComponent(
                text = stringResource(id = R.string.workout_string),
                value = fitTimerState.value.workoutTime.formatedMinutes(),
                secondValue = fitTimerState.value.workoutTime.formatedSeconds(),
                increaseHandler = { fitTimerViewModel.increase(FitTimerType.WORKOUT) },
                decreaseHandler = { fitTimerViewModel.decrease(FitTimerType.WORKOUT) },
                onStringChange = fitTimerViewModel.changeWorkOutTimeByString
            )
            FitTimerComponent(
                text = stringResource(id = R.string.rest_string),
                value = fitTimerState.value.restTime.formatedMinutes(),
                secondValue = fitTimerState.value.restTime.formatedSeconds(),
                increaseHandler = { fitTimerViewModel.increase(FitTimerType.REST) },
                decreaseHandler = { fitTimerViewModel.decrease(FitTimerType.REST) },
                onStringChange = fitTimerViewModel.changeWorkOutTimeByString
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {},
            colors = ButtonColors(
                colorResource(id = R.color.primary),
                colorResource(id = R.color.white),
                colorResource(id = R.color.primary),
                colorResource(id = R.color.primary),
            ),
            modifier = Modifier
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
    onStringChange: (String) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = text, fontSize = 30.sp)
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
                    onValueChange = {},
                    textStyle = TextStyle(fontSize = 24.sp),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(onDone = { onStringChange(value) }),
                    modifier = Modifier
                        .width(IntrinsicSize.Min)
                        .padding(0.dp),
                    singleLine = true
                )
                if (!secondValue.isBlank()) {
                    Text(text = ":")
                    BasicTextField(
                        value = secondValue,
                        onValueChange = {},
                        textStyle = TextStyle(fontSize = 24.sp),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        keyboardActions = KeyboardActions(onDone = { onStringChange(value) }),
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