package com.sethvanstaden.dintest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sethvanstaden.dintest.data.local.dao.TestResultDao
import com.sethvanstaden.dintest.data.local.entities.TestResultEntity

@Database(
    entities = [TestResultEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun testResultDao(): TestResultDao
}