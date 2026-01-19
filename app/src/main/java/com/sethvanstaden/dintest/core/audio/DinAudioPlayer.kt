package com.sethvanstaden.dintest.core.audio

interface DinAudioPlayer {
    suspend fun playRound(noiseResId: Int, digitResIds: List<Int>)
    fun stopAll()
    fun release()
}