package com.sethvanstaden.dintest.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "test_results")
data class TestResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val createdAt: Long,
    val score: Int,
    val correctCount: Int,
    val totalRounds: Int,
    val difficultyPath: String,
    val flowJson: String
)