package com.sethvanstaden.dintest.domain.model

import com.sethvanstaden.dintest.domain.session.DinTestSession

data class DinTestUiState(
    val roundIndex: Int = 0,
    val totalRounds: Int = DinTestSession.TOTAL_ROUNDS,
    val difficultyUsed: Int = DinTestSession.START_DIFFICULTY,
    val input: String = "",
    val isPlaying: Boolean = false,
    val canSubmit: Boolean = false,
    val isUploading: Boolean = false,
    val finalScore: Int? = null,
    val error: String? = null
)
