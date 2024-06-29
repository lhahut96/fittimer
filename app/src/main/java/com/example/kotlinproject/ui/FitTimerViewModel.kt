package com.example.kotlinproject.ui

import androidx.lifecycle.ViewModel
import com.example.kotlinproject.data.TimeUnit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FitTimerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FitTimerUiState())

    var uiState: StateFlow<FitTimerUiState> = _uiState.asStateFlow()

    private val increaseRep: () -> Unit = {
        _uiState.update { currentState -> currentState.copy(numberOfRounds = currentState.numberOfRounds.inc()) }
    }

    private val decreaseRep: () -> Unit = {
        if (_uiState.value.numberOfRounds > 0) {
            _uiState.update { currentState -> currentState.copy(numberOfRounds = currentState.numberOfRounds.dec()) }
        }
    }

    val changeRep: (stringRep: String) -> Unit = {
        val convertedToInt = it.toIntOrNull() ?: 0
        _uiState.update { currentState -> currentState.copy(numberOfRounds = convertedToInt) }
    }

    private val increaseWorkOutTime: () -> Unit = {
        _uiState.update { currentState -> currentState.copy(workoutTime = currentState.workoutTime.increaseTenSecond()) }
    }

    private val decreaseWorkOutTime: () -> Unit = {
        _uiState.update { currentState -> currentState.copy(workoutTime = currentState.workoutTime.decreaseTenSecond()) }
    }
    private val increaseRestTime: () -> Unit = {
        _uiState.update { currentState -> currentState.copy(restTime = currentState.restTime.increaseTenSecond()) }
    }

    private val decreaseRestTime: () -> Unit = {
        _uiState.update { currentState -> currentState.copy(restTime = currentState.restTime.decreaseTenSecond()) }
    }


    val changeTimeByString: (timeString: String, timeUnit: TimeUnit, fitTimerState: FitTimerState) -> Unit =
        { timeString, timeUnit, fitTimerState ->
            when (fitTimerState) {
                FitTimerState.Workout -> _uiState.update { currentState ->
                    currentState.copy(
                        workoutTime = currentState.workoutTime.setTimeByString(
                            timeString,
                            timeUnit
                        )
                    )
                }

                FitTimerState.Rest -> _uiState.update { currentState ->
                    currentState.copy(
                        workoutTime = currentState.restTime.setTimeByString(
                            timeString,
                            timeUnit
                        )
                    )
                }
            }

        }

    val increase: (type: FitTimerType) -> Unit = { type ->
        when (type) {
            FitTimerType.REP -> increaseRep()
            FitTimerType.WORKOUT -> increaseWorkOutTime()
            FitTimerType.REST -> increaseRestTime()
        }
    }

    val decrease: (type: FitTimerType) -> Unit = { type ->
        when (type) {
            FitTimerType.REP -> decreaseRep()
            FitTimerType.WORKOUT -> decreaseWorkOutTime()
            FitTimerType.REST -> decreaseRestTime()
        }

    }

    val nextRound: () -> Unit = {
        if (_uiState.value.currentRound < _uiState.value.numberOfRounds) {
            _uiState.update { currentState ->
                currentState.copy(currentRound = currentState.currentRound.inc())
            }
        }
    }

}

enum class FitTimerType { REP, WORKOUT, REST }