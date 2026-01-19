package com.sethvanstaden.dintest.data.remote

import com.sethvanstaden.dintest.data.remote.api.DinApi
import com.sethvanstaden.dintest.data.remote.mapper.toUploadRequest
import com.sethvanstaden.dintest.domain.model.TestResult
import com.sethvanstaden.dintest.domain.uploader.DinUploader

class DinUploaderImpl(
    private val api: DinApi
) : DinUploader {
    override suspend fun upload(result: TestResult) {
        val response = api.upload(result.toUploadRequest())
        if (!response.isSuccessful) {
            error("Upload failed: HTTP ${response.code()} ${response.message()}")
        }
    }
}