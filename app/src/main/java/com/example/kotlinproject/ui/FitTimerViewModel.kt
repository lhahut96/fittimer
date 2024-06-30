package com.example.kotlinproject.ui

import android.util.Log
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

//    val increase: (type: FitTimerType) -> () -> FitTime = { type ->
//        when (type) {
//            FitTimerType.REP -> increaseRep
//            FitTimerType.WORKOUT -> increaseWorkOutTime
//            FitTimerType.REST -> increaseRestTime
//        }
//    }

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