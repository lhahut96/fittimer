package com.example.kotlinproject.ui

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import com.example.kotlinproject.data.FitTime
import com.example.kotlinproject.data.TimeUnit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FitTimerViewModel : ViewModel() {
    private lateinit var countDownTimer: CountDownTimer
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

    val startTimer: () -> Unit = {
        _uiState.update { currentState ->
            val currentStateTime =
                if (_uiState.value.workState == FitTimerState.Workout) _uiState.value.workoutTime else _uiState.value.restTime
            currentState.copy(
                currentTime = currentStateTime
            )
        }

        countDownTimer =
            object : CountDownTimer(_uiState.value.currentTime.getMiliSeconds(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            currentTime = currentState.currentTime.decreaseOneSecond(),
                            clockState = FitTimerClockState.Progressing
                        )
                    }
                }

                override fun onFinish() {
                    TODO("Not yet implemented")
                }
            }

        countDownTimer.start()
    }

    val pauseTimer: () -> Unit = {
        countDownTimer.cancel()
        _uiState.update { currentState -> currentState.copy(clockState = FitTimerClockState.Pause) }
    }

    val resumeTimer: () -> Unit = {
        countDownTimer =
            object : CountDownTimer(_uiState.value.currentTime.getMiliSeconds(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            currentTime = currentState.currentTime.decreaseOneSecond(),
                        )
                    }
                }

                override fun onFinish() {
                    TODO("Not yet implemented")
                }
            }
        countDownTimer.start()
    }
}

enum class FitTimerType { REP, WORKOUT, REST }