package com.sethvanstaden.dintest.data.remote.api

import com.sethvanstaden.dintest.data.remote.dto.TestUploadRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface DinApi {
    @POST("/")
    suspend fun upload(@Body body: TestUploadRequest): Response<Unit>
}