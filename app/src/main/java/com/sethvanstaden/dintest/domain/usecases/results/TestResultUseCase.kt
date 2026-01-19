package com.sethvanstaden.dintest.domain.usecases.results

import com.sethvanstaden.dintest.data.local.repository.TestResultRepository
import com.sethvanstaden.dintest.domain.model.TestResultUiModel
import com.sethvanstaden.dintest.ui.features.results.TestResultUiMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TestResultUseCase(
    private val repo: TestResultRepository,
    private val uiMapper: TestResultUiMapper
) {
    operator fun invoke(): Flow<List<TestResultUiModel>> = repo.observeAll().map { list -> list.map(uiMapper::map) }
}