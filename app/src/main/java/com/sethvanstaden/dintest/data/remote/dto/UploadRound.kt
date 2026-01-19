package com.sethvanstaden.dintest.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UploadRound(
    @Json(name = "difficulty") val difficulty: Int,
    @Json(name = "triplet_played") val tripletPlayed: String,
    @Json(name = "triplet_answered") val tripletAnswered: String
)