package com.sethvanstaden.dintest.domain.uploader

import com.sethvanstaden.dintest.domain.model.TestResult

interface DinUploader {
    suspend fun upload(result: TestResult)
}