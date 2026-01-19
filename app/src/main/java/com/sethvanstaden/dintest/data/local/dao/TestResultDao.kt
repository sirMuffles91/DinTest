package com.sethvanstaden.dintest.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sethvanstaden.dintest.data.local.entities.TestResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TestResultDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(result: TestResultEntity)

    @Query("SELECT * FROM test_results ORDER BY score DESC, createdAt DESC")
    fun observeAllOrdered(): Flow<List<TestResultEntity>>
}