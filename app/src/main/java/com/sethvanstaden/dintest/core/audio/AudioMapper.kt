package com.sethvanstaden.dintest.core.audio

import com.sethvanstaden.dintest.R

class AudioMapper {
    fun noiseMapper(difficulty: Int): Int {
        val level = difficulty.coerceIn(1, 10)
        return when (level) {
            1 -> R.raw.noise_1
            2 -> R.raw.noise_2
            3 -> R.raw.noise_3
            4 -> R.raw.noise_4
            5 -> R.raw.noise_5
            6 -> R.raw.noise_6
            7 -> R.raw.noise_7
            8 -> R.raw.noise_8
            9 -> R.raw.noise_9
            10 -> R.raw.noise_10
            else -> R.raw.noise_5
        }
    }

    fun digitMapper(digit: Int): Int {
        require(digit in 1..9)
        return when (digit) {
            1 -> R.raw.digit_1
            2 -> R.raw.digit_2
            3 -> R.raw.digit_3
            4 -> R.raw.digit_4
            5 -> R.raw.digit_5
            6 -> R.raw.digit_6
            7 -> R.raw.digit_7
            8 -> R.raw.digit_8
            9 -> R.raw.digit_9
            else -> error("Unknown digit")
        }
    }
}