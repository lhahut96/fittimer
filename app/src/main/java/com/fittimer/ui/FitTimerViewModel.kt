package com.fittimer.ui

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fittimer.data.FitTime
import com.fittimer.data.TimeUnit
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
                            timeString, timeUnit
                        )
                    )
                }

                FitTimerState.Rest -> _uiState.update { currentState ->
                    currentState.copy(
                        restTime = currentState.restTime.setTimeByString(
                            timeString, timeUnit
                        )
                    )
                }
            }

        }

    val nextRound: () -> Unit = {
        countDownTimer.cancel()
        if (_uiState.value.currentRound == _uiState.value.numberOfRounds && _uiState.value.workState == FitTimerState.Workout) {
        } else if (_uiState.value.currentRound < _uiState.value.numberOfRounds) {
            if (_uiState.value.workState == FitTimerState.Workout) {
                _uiState.update { currentState ->
                    currentState.copy(
                        workState = FitTimerState.Workout,
                        currentRound = currentState.currentRound.inc()
                    )
                }
            } else {
                _uiState.update { currentState ->
                    currentState.copy(
                        workState = FitTimerState.Rest,
                    )
                }
            }
        }
        updateCurrentStateTime()
        startTimer()
    }

    val goBackRound: () -> Unit = {
        countDownTimer.cancel()
        if (_uiState.value.currentRound > 1 && _uiState.value.workState == FitTimerState.Workout) {
            _uiState.update { currentState ->
                currentState.copy(
                    workState = FitTimerState.Rest, currentRound = currentState.currentRound.dec()
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    workState = FitTimerState.Workout,
                )
            }
        }

        updateCurrentStateTime()
        startTimer()
    }

    val updateCurrentStateTime: () -> Unit = {
        _uiState.update { currentState ->
            val currentStateTime =
                if (_uiState.value.workState == FitTimerState.Workout) FitTime(_uiState.value.workoutTime) else FitTime(
                    _uiState.value.restTime
                )
            currentState.copy(
                currentTime = currentStateTime
            )
        }
    }

    val resetTimer: () -> Unit = {
       _uiState.update { currentState ->
           currentState.copy(
               clockState = FitTimerClockState.Stop,
               currentRound = 1,
               workState = FitTimerState.Workout,
               currentTime = currentState.workoutTime
           )
       }
        countDownTimer.cancel()
    }


    val startTimer: () -> Unit = {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    clockState = FitTimerClockState.Progressing,

                )
            }
            countDownTimer = initTimer()
            countDownTimer.start()
        }
    }

    val initTimer: () -> CountDownTimer = {
        val newCurrentTime = _uiState.value.currentTime.increaseOneSecond()
        object : CountDownTimer(newCurrentTime.getMiliSeconds(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _uiState.update { currentState ->
                    currentState.copy(
                        currentTime = newCurrentTime.decreaseOneSecond(),
                        clockState = FitTimerClockState.Progressing
                    )
                }
            }

            override fun onFinish() {
                if (_uiState.value.currentRound == _uiState.value.numberOfRounds && _uiState.value.workState == FitTimerState.Workout) {
                    _uiState.update { currentState -> currentState.copy(clockState = FitTimerClockState.Stop) }
                    return cancel()
                } else if (_uiState.value.workState == FitTimerState.Workout) {
                    _uiState.update { currentState ->
                        currentState.copy(workState = FitTimerState.Rest)
                    }
                } else {
                    _uiState.update { currentState ->
                        currentState.copy(
                            workState = FitTimerState.Workout,
                            currentRound = currentState.currentRound.inc()
                        )
                    }
                }
                updateCurrentStateTime()
                startTimer()
            }
        }
    }

    val pauseTimer: () -> Unit = {
        viewModelScope.launch {
            countDownTimer.cancel()
            _uiState.update { currentState -> currentState.copy(clockState = FitTimerClockState.Pause) }
        }
    }

    val resumeTimer: () -> Unit = {
        viewModelScope.launch {
            countDownTimer.cancel()
            _uiState.update { currentState ->
                currentState.copy(
                    clockState = FitTimerClockState.Progressing
                )
            }
            delay(1000)
            startTimer()
        }
    }

}

enum class FitTimerType { REP, WORKOUT, REST }