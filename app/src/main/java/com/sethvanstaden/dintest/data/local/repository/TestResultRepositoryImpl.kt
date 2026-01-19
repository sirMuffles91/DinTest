package com.sethvanstaden.dintest.data.local.repository

import com.sethvanstaden.dintest.data.local.dao.TestResultDao
import com.sethvanstaden.dintest.data.local.entities.TestResultEntity

class TestResultRepositoryImpl(
    private val dao: TestResultDao
) : TestResultRepository {
    override suspend fun save(result: TestResultEntity) = dao.insert(result)
    override fun observeAll() = dao.observeAllOrdered()
}