package com.example.kotlinproject.ui

import androidx.lifecycle.ViewModel
import com.example.kotlinproject.data.FitTime
import com.example.kotlinproject.data.TimeUnit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FitTimerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FitTimerUiState())

    var uiState: StateFlow<FitTimerUiState> = _uiState.asStateFlow()

    val increaseRep: () -> String = {
        _uiState.update { currentState -> currentState.copy(numberOfRounds = currentState.numberOfRounds.inc()) }
        _uiState.value.numberOfRounds.toString()
    }

    val decreaseRep: () -> String = {
        if (_uiState.value.numberOfRounds > 0) {
            _uiState.update { currentState -> currentState.copy(numberOfRounds = currentState.numberOfRounds.dec()) }
            _uiState.value.numberOfRounds.toString()
        } else {
            "0"
        }
    }

    val changeRep: (stringRep: String) -> Unit = {
        val convertedToInt = it.toIntOrNull() ?: 0
        _uiState.update { currentState -> currentState.copy(numberOfRounds = convertedToInt) }
    }

    val increaseWorkOutTime: () -> FitTime = {
        val updatedFitTime = _uiState.value.workoutTime.increaseTenSecond()
        _uiState.update { currentState -> currentState.copy(workoutTime = updatedFitTime) }
        updatedFitTime
    }

    val decreaseWorkOutTime: () -> FitTime = {
        val updatedFitTime = _uiState.value.workoutTime.decreaseTenSecond()
        _uiState.update { currentState -> currentState.copy(workoutTime = updatedFitTime) }
        updatedFitTime
    }
    val increaseRestTime: () -> FitTime = {
        val updatedFitTime = _uiState.value.restTime.increaseTenSecond()
        _uiState.update { currentState -> currentState.copy(restTime = updatedFitTime) }
        updatedFitTime
    }

    val decreaseRestTime: () -> FitTime = {
        val updatedFitTime = _uiState.value.restTime.decreaseTenSecond()
        _uiState.update { currentState -> currentState.copy(restTime = updatedFitTime) }
        updatedFitTime
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
                        restTime = currentState.restTime.setTimeByString(
                            timeString,
                            timeUnit
                        )
                    )
                }
            }

        }

    val nextRound: () -> Unit = {
        if (_uiState.value.currentRound < _uiState.value.numberOfRounds) {
            _uiState.update { currentState ->
                currentState.copy(currentRound = currentState.currentRound.inc())
            }
        }
    }

    val goBackRound: () -> Unit = {
        if (_uiState.value.currentRound >= 1) {
            _uiState.update { currentState ->
                currentState.copy(currentRound = currentState.currentRound.dec())
            }
        }
    }
}

enum class FitTimerType { REP, WORKOUT, REST }