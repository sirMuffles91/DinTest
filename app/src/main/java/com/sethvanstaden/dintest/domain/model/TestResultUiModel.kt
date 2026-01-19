package com.sethvanstaden.dintest.domain.model

data class TestResultUiModel(
    val id: Long,
    val score: Int,
    val correctLabel: String,
    val dateLabel: String,
    val difficultyPathLabel: String?
)