package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.JobPost
import kotlinx.coroutines.flow.Flow

@Dao
interface JobDao {
    @Query("SELECT * FROM jobs ORDER BY postingDate DESC")
    fun getAllJobs(): Flow<List<JobPost>>

    @Query("SELECT * FROM jobs WHERE id = :id LIMIT 1")
    fun getJobById(id: String): Flow<JobPost?>

    @Query("SELECT * FROM jobs WHERE isSaved = 1")
    fun getSavedJobs(): Flow<List<JobPost>>

    @Query("SELECT * FROM jobs WHERE isWatchlisted = 1")
    fun getWatchlistedJobs(): Flow<List<JobPost>>

    @Query("SELECT * FROM jobs WHERE applicationStatus = 'SUBMITTED'")
    fun getSubmittedJobs(): Flow<List<JobPost>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(jobs: List<JobPost>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJob(job: JobPost)

    @Update
    suspend fun updateJob(job: JobPost)

    @Query("UPDATE jobs SET isSaved = :isSaved WHERE id = :id")
    suspend fun toggleSave(id: String, isSaved: Boolean)

    @Query("UPDATE jobs SET isWatchlisted = :isWatchlisted WHERE id = :id")
    suspend fun toggleWatchlist(id: String, isWatchlisted: Boolean)

    @Query("UPDATE jobs SET applicationStatus = :status WHERE id = :id")
    suspend fun updateApplicationStatus(id: String, status: String)

    @Query("SELECT COUNT(*) FROM jobs")
    suspend fun getJobCount(): Int
}
