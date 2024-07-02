package com.fittimer.data

enum class TimeUnit() { Minute, Second }
class FitTime(private var _minutes: Int = 0, private var _seconds: Int = 0) {

    private val minutes = _minutes
    private val seconds = _seconds

    constructor(fitTime: FitTime): this(
        fitTime.minutes,
        fitTime.seconds
    )

    fun setTimeByString(timeString: String, timeUnit: TimeUnit): FitTime {
        val timeConverted = timeString.toIntOrNull() ?: 0
        return when (timeUnit) {
            TimeUnit.Minute -> FitTime(timeConverted, _seconds)
            TimeUnit.Second -> FitTime(_minutes, timeConverted)
        }

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
        if (seconds < 10) {
            if (_minutes >= 1) {
                _minutes -= 1
                _seconds = 50 + seconds
            } else {
                _minutes = 0
                _seconds = 0
            }
        } else {
            _seconds -= 10
        }
        val minutes = _seconds / 60 + _minutes
        val remainingSeconds = _seconds % 60
        return FitTime(minutes, remainingSeconds)
    }

    fun decreaseOneSecond(): FitTime {
        if (seconds == 0 && minutes == 0) {
            return this
        }
        if (seconds == 0) {
            if (_minutes >= 1) {
                _minutes -= 1
                _seconds = 59
            } else {
                _minutes = 0
                _seconds = 0
            }
        } else {
            _seconds -= 1
        }
        val minutes = _seconds / 60 + _minutes
        val remainingSeconds = _seconds % 60
        return FitTime(minutes, remainingSeconds)
    }

    fun increaseOneSecond(): FitTime {
        if (seconds == 0 && minutes == 0) {
            return this
        }
        if (seconds == 0) {
            if (_minutes >= 1) {
                _minutes += 1
                _seconds = 59
            } else {
                _minutes = 0
                _seconds = 0
            }
        } else {
            _seconds += 1
        }
        val minutes = _seconds / 60 + _minutes
        val remainingSeconds = _seconds % 60
        return FitTime(minutes, remainingSeconds)
    }

    fun getMiliSeconds(): Long {
        return (_minutes * 60 + _seconds) * 1000L
    }

    override fun toString(): String {
        return formatedMinutes() + ":" + formatedSeconds()
    }

}