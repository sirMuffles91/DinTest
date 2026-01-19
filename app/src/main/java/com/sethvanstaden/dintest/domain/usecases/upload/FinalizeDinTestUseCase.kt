package com.sethvanstaden.dintest.domain.usecases.upload

import com.sethvanstaden.dintest.data.local.mapper.TestResultMapper
import com.sethvanstaden.dintest.data.local.repository.TestResultRepository
import com.sethvanstaden.dintest.domain.model.TestResult
import com.sethvanstaden.dintest.domain.uploader.DinUploader

class FinalizeDinTestUseCase(
    private val uploader: DinUploader,
    private val resultsRepository: TestResultRepository,
    private val mapper: TestResultMapper
) {
    suspend fun execute(final: TestResult): FinalizeResult {
        return try {
            uploader.upload(final)
            runCatching {
                resultsRepository.save(mapper.toEntity(final))
            }
            FinalizeResult.Success(final.totalScore)
        } catch (t: Throwable) {
            FinalizeResult.Failure(t)
        }
    }
}

sealed class FinalizeResult {
    data class Success(val score: Int) : FinalizeResult()
    data class Failure(val error: Throwable) : FinalizeResult()
}