package com.example.kotlinproject.ui

import com.example.kotlinproject.data.FitTime

data class FitTimerUiState(
    val workoutTime: FitTime = FitTime(0, 5),
    val restTime: FitTime = FitTime(0, 5),
    val numberOfRounds: Int = 10,
    val currentRound: Int = 1,
    val currentTime: FitTime = workoutTime,
    val clockState: FitTimerClockState = FitTimerClockState.Pause,
    val workState: FitTimerState = FitTimerState.Workout
)
enum class FitTimerState { Workout, Rest }
enum class FitTimerClockState { Progressing, Stop, Pause }