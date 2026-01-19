package com.sethvanstaden.dintest.data.remote.dto

import com.sethvanstaden.dintest.data.remote.dto.UploadRound
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TestUploadRequest(
    @Json(name = "score") val score: Int,
    @Json(name = "rounds") val rounds: List<UploadRound>
)