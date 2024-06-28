package com.example.kotlinproject.ui

import com.example.kotlinproject.data.FitTime

data class FitTimerUiState(
    val workoutTime: FitTime = FitTime(0, 10),
    val restTime: FitTime = FitTime(1, 0),
    val numberOfRounds: Int = 0,
    val currentRound: Int = 1,
    val clockState: FitTimerClockState = FitTimerClockState.Stop,
    val workState: FitTimerState = FitTimerState.Rest
)
enum class FitTimerState { Workout, Rest }
enum class FitTimerClockState { Progressing, Stop, Pause }