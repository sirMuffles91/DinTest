package com.sethvanstaden.dintest.domain.model

import com.sethvanstaden.dintest.domain.triplet.Triplet

data class RoundStart(
    val roundIndex: Int,
    val difficultyUsed: Int,
    val triplet: Triplet,
    val startedAtMs: Long
)