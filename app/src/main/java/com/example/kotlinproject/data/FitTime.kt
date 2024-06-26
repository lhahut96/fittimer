package com.example.kotlinproject.data

import android.util.Log

class FitTime(private var _minutes: Int = 0, private var _seconds: Int = 0) {

    private val minutes = _minutes
    private val seconds = _seconds

    constructor(timeString: String) : this() {
        setTimeByString(timeString)
    }

    fun setTimeByString(timeString: String): FitTime {
        val timePair: Pair<Int, Int> = formattedTimeToMinutesAndSeconds(timeString);
        val newMinutes = timePair.first
        val newSeconds = timePair.second
        return FitTime(newMinutes, newSeconds)
    }

    fun getTimeString(): String {
        val allSeconds = minutes * 60L + seconds
        return secondsToFormattedMinutes(allSeconds)
    }

    fun secondsToFormattedMinutes(seconds: Long): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60

        // Pad minutes and seconds with leading zeros if necessary
        val formattedMinutes = minutes.toString().padStart(2, '0')
        val formattedSeconds = remainingSeconds.toString().padStart(2, '0')

        return "$formattedMinutes:$formattedSeconds"
    }

    fun formatedMinutes(): String {
        return minutes.toString().padStart(2, '0')
    }

    fun formatedSeconds(): String {
        return seconds.toString().padStart(2, '0')
    }

    fun formattedTimeToMinutesAndSeconds(timeString: String): Pair<Int, Int> {
        val parts = timeString.split(":")
        if (parts.size != 2) {
            throw IllegalArgumentException("Invalid time format. Expected 'HH:MM'")
        }

        val minutes =
            parts[0].toIntOrNull() ?: throw IllegalArgumentException("Invalid minutes format")
        val seconds =
            parts[1].toIntOrNull() ?: throw IllegalArgumentException("Invalid seconds format")

        return Pair(minutes, seconds)
    }

    fun increaseTenSecond(): FitTime {
        _seconds += 10

        val minutes = _seconds / 60 + _minutes
        val remainingSeconds = _seconds % 60
        return FitTime(minutes, remainingSeconds)
    }

    fun decreaseTenSecond(): FitTime {
        if (seconds == 0 && minutes == 0) {
            return this
        }
        if (seconds == 0) {
            _minutes -= 1
            _seconds = 50
        } else {
            _seconds -= 10
        }
        val minutes = _seconds / 60 + _minutes
        val remainingSeconds = _seconds % 60
        return FitTime(minutes, remainingSeconds)
    }


}