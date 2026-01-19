package com.sethvanstaden.dintest.data.remote.mapper

import com.sethvanstaden.dintest.domain.model.TestResult
import com.sethvanstaden.dintest.data.remote.dto.TestUploadRequest
import com.sethvanstaden.dintest.data.remote.dto.UploadRound

fun TestResult.toUploadRequest(): TestUploadRequest =
    TestUploadRequest(
        score = totalScore,
        rounds = rounds.map {
            UploadRound(
                difficulty = it.difficultyUsed,
                tripletPlayed = it.triplet.asString,
                tripletAnswered = it.userAnswer
            )
        }
    )