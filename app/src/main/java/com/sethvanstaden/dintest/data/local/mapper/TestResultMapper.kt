package com.sethvanstaden.dintest.data.local.mapper

import com.sethvanstaden.dintest.data.local.entities.TestResultEntity
import com.sethvanstaden.dintest.domain.model.TestResult
import com.squareup.moshi.Moshi

class TestResultMapper(
    moshi: Moshi
) {
    private val adapter = moshi.adapter(TestResult::class.java)

    fun toEntity(final: TestResult): TestResultEntity {
        return TestResultEntity(
            createdAt = System.currentTimeMillis(),
            score = final.totalScore,
            correctCount = final.rounds.count { it.isCorrect },
            totalRounds = final.rounds.size,
            difficultyPath = final.rounds.joinToString(",") { it.difficultyUsed.toString() },
            flowJson = adapter.toJson(final)
        )
    }
}