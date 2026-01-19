package com.sethvanstaden.dintest.data.local.repository

import com.sethvanstaden.dintest.data.local.entities.TestResultEntity
import kotlinx.coroutines.flow.Flow

interface TestResultRepository {
    suspend fun save(result: TestResultEntity)
    fun observeAll(): Flow<List<TestResultEntity>>
}