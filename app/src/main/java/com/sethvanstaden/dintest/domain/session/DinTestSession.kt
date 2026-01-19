package com.sethvanstaden.dintest.domain.session

import com.sethvanstaden.dintest.domain.model.RoundResult
import com.sethvanstaden.dintest.domain.model.RoundStart
import com.sethvanstaden.dintest.domain.model.TestResult
import com.sethvanstaden.dintest.domain.triplet.Triplet
import com.sethvanstaden.dintest.domain.triplet.TripletGenerator

class DinTestSession(
    private val tripletGenerator: TripletGenerator,
    private val idProvider: () -> String = { java.util.UUID.randomUUID().toString() },
    private val now: () -> Long = { System.currentTimeMillis() }
) {
    companion object {
        const val TOTAL_ROUNDS = 10
        const val START_DIFFICULTY = 5
        const val MIN_DIFFICULTY = 1
        const val MAX_DIFFICULTY = 10
    }

    private val _rounds = mutableListOf<RoundResult>()
    val rounds: List<RoundResult> get() = _rounds

    val testId: String = idProvider()
    val startedAt: Long = now()

    var currentRoundIndex: Int = 0
        private set

    var currentDifficulty: Int = START_DIFFICULTY
        private set

    var currentTriplet: Triplet? = null
        private set

    private var currentRoundStartedAtMs: Long? = null

    val isFinished: Boolean
        get() = _rounds.size >= TOTAL_ROUNDS

    fun startNextRound(): RoundStart {
        check(!isFinished) { "Test already finished" }
        check(currentRoundStartedAtMs == null) { "Round already started, submit before starting a new one" }

        currentRoundIndex += 1

        val previous = currentTriplet
        val triplet = tripletGenerator.next(previous)
        currentTriplet = triplet

        val startedAt = now()
        currentRoundStartedAtMs = startedAt

        val difficulty = currentDifficulty

        return RoundStart(
            roundIndex = currentRoundIndex,
            difficultyUsed = difficulty,
            triplet = triplet,
            startedAtMs = startedAt
        )
    }

    fun submit(answer: String): RoundResult {
        check(!isFinished) { "Test already finished" }

        val triplet = requireNotNull(currentTriplet) { "No active triplet. Call startNextRound() first." }
        val startedAt = requireNotNull(currentRoundStartedAtMs) { "Round not started. Call startNextRound() first." }

        val submittedAt = now()
        val normalized = answer.trim()

        val correct = normalized == triplet.asString
        val difficultyUsed = currentDifficulty
        val points = difficultyUsed

        val result = RoundResult(
            roundIndex = currentRoundIndex,
            triplet = triplet,
            difficultyUsed = difficultyUsed,
            userAnswer = normalized,
            isCorrect = correct,
            pointsAwarded = points,
            startedAt = startedAt,
            submittedAt = submittedAt
        )

        _rounds.add(result)
        currentRoundStartedAtMs = null

        currentDifficulty = (currentDifficulty + if (correct) 1 else -1)
            .coerceIn(MIN_DIFFICULTY, MAX_DIFFICULTY)

        return result
    }

    fun buildFinalResult(): TestResult {
        check(isFinished) { "Test not finished yet" }

        val finishedAt = now()
        val total = _rounds.sumOf { it.pointsAwarded }

        return TestResult(
            id = testId,
            startedAt = startedAt,
            finishedAt = finishedAt,
            rounds = _rounds.toList(),
            totalScore = total
        )
    }
}