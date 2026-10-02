package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.ApplicationRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface ApplicationDao {
    @Query("SELECT * FROM applications ORDER BY appliedDate DESC")
    fun getAllApplications(): Flow<List<ApplicationRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(applications: List<ApplicationRecord>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(application: ApplicationRecord)

    @Query("DELETE FROM applications WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT COUNT(*) FROM applications WHERE status = 'SUBMITTED'")
    fun getSubmittedCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM applications")
    suspend fun getTotalCount(): Int
}
