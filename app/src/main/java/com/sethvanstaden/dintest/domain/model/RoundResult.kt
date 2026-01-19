package com.sethvanstaden.dintest.domain.model

import com.sethvanstaden.dintest.domain.triplet.Triplet

data class RoundResult(
    val roundIndex: Int,
    val triplet: Triplet,
    val difficultyUsed: Int,
    val userAnswer: String,
    val isCorrect: Boolean,
    val pointsAwarded: Int,
    val startedAt: Long,
    val submittedAt: Long
)