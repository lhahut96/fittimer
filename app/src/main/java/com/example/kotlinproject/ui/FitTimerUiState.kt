package com.example.kotlinproject.ui

import com.example.kotlinproject.data.FitTime

data class FitTimerUiState(
    val workoutTime: FitTime = FitTime(0,10),
    val restTime: FitTime = FitTime(1,0),
    val numberOfRounds: Int = 0
)