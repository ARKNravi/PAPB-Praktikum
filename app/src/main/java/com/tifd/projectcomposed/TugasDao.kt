package com.tifd.projectcomposed.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TugasDao {
    @Query("SELECT * FROM tugas")
    fun getAllTugas(): Flow<List<TugasEntity>>

    @Insert
    suspend fun insertTugas(tugas: TugasEntity)

    @Update
    suspend fun updateTugas(tugas: TugasEntity)

    @Delete
    suspend fun deleteTugas(tugas: TugasEntity)
}