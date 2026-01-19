package com.sethvanstaden.dintest.ui.features.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sethvanstaden.dintest.core.audio.AudioMapper
import com.sethvanstaden.dintest.core.audio.DinAudioPlayer
import com.sethvanstaden.dintest.domain.model.DinTestUiState
import com.sethvanstaden.dintest.domain.session.DinTestSession
import com.sethvanstaden.dintest.domain.usecases.upload.FinalizeDinTestUseCase
import com.sethvanstaden.dintest.domain.usecases.upload.FinalizeResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DinTestViewModel(
    private val session: DinTestSession,
    private val audioMapper: AudioMapper,
    private val dinAudioPlayer: DinAudioPlayer,
    private val finalizeDinTestUseCase: FinalizeDinTestUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(DinTestUiState())
    val state = _state.asStateFlow()

    private var roundJob: Job? = null

    fun onScreenShown() {
        if (_state.value.roundIndex != 0) return
        scheduleStartRound(delayMs = 3000L)
    }

    fun onInputChanged(text: String) {
        val filtered = text.filter { it.isDigit() }.take(3)
        _state.update { it.copy(input = filtered, error = null) }
    }

    fun onExit() {
        roundJob?.cancel()
        dinAudioPlayer.stopAll()
    }

    fun onSubmit() {
        val s = _state.value
        if (!s.canSubmit || s.isPlaying || s.isUploading) return

        if (s.input.length != 3) {
            _state.update { it.copy(error = "Enter 3 digits") }
            return
        }

        session.submit(s.input)

        _state.update { it.copy(input = "", canSubmit = false, error = null) }

        if (session.isFinished) {
            finishTest()
        } else {
            scheduleStartRound(delayMs = 2000L)
        }
    }

    fun dismissFinalScore() {
        _state.update { it.copy(finalScore = null) }
    }

    private fun scheduleStartRound(delayMs: Long) {
        roundJob?.cancel()
        roundJob = viewModelScope.launch {
            delay(delayMs)
            startRound()
        }
    }

    private suspend fun startRound() {
        val roundStart = session.startNextRound()

        _state.update {
            it.copy(
                roundIndex = roundStart.roundIndex,
                difficultyUsed = roundStart.difficultyUsed,
                isPlaying = true,
                canSubmit = false,
                error = null
            )
        }

        val noiseResId = audioMapper.noiseMapper(roundStart.difficultyUsed)
        val digitResIds = roundStart.triplet.digits.map(audioMapper::digitMapper)

        try {
            dinAudioPlayer.playRound(noiseResId, digitResIds)
            _state.update { it.copy(isPlaying = false, canSubmit = true) }
        } catch (t: Throwable) {
            _state.update { it.copy(isPlaying = false, error = "Audio error: ${t.message}") }
        }
    }

    private fun finishTest() {
        viewModelScope.launch {
            _state.update { it.copy(isUploading = true, error = null) }

            val final = session.buildFinalResult()

            when (val result = finalizeDinTestUseCase.execute(final)) {
                is FinalizeResult.Success -> {
                    _state.update { it.copy(isUploading = false, finalScore = result.score) }
                }
                is FinalizeResult.Failure -> {
                    _state.update { it.copy(isUploading = false, error = "Upload failed. Please retry.") }
                }
            }
        }
    }

    fun retryFinalize() {
        if (!session.isFinished) return
        finishTest()
    }

    override fun onCleared() {
        dinAudioPlayer.stopAll()
        super.onCleared()
    }
}