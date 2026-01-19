package com.sethvanstaden.dintest.domain.model

data class TestResult(
    val id: String,
    val startedAt: Long,
    val finishedAt: Long,
    val rounds: List<RoundResult>,
    val totalScore: Int
)