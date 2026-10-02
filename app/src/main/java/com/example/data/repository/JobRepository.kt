package com.example.data.repository

import com.example.data.local.ApplicationDao
import com.example.data.local.JobDao
import com.example.data.local.SampleData
import com.example.data.model.ApplicationRecord
import com.example.data.model.CandidateProfile
import com.example.data.model.JobPost
import kotlinx.coroutines.flow.Flow

class JobRepository(
    private val jobDao: JobDao,
    private val applicationDao: ApplicationDao
) {
    val candidateProfile = CandidateProfile()

    val allJobs: Flow<List<JobPost>> = jobDao.getAllJobs()
    val savedJobs: Flow<List<JobPost>> = jobDao.getSavedJobs()
    val watchlistedJobs: Flow<List<JobPost>> = jobDao.getWatchlistedJobs()
    val submittedJobs: Flow<List<JobPost>> = jobDao.getSubmittedJobs()
    val allApplications: Flow<List<ApplicationRecord>> = applicationDao.getAllApplications()
    val submittedCount: Flow<Int> = applicationDao.getSubmittedCount()

    suspend fun initializeIfEmpty() {
        val count = jobDao.getJobCount()
        if (count == 0) {
            jobDao.insertAll(SampleData.initialJobs)
        }
        val appCount = applicationDao.getTotalCount()
        if (appCount == 0) {
            applicationDao.insertAll(SampleData.initialApplications)
        }
    }

    suspend fun toggleSave(id: String, isSaved: Boolean) {
        jobDao.toggleSave(id, isSaved)
    }

    suspend fun toggleWatchlist(id: String, isWatchlisted: Boolean) {
        jobDao.toggleWatchlist(id, isWatchlisted)
    }

    suspend fun updateApplicationStatus(id: String, status: String) {
        jobDao.updateApplicationStatus(id, status)
    }

    suspend fun insertApplication(app: ApplicationRecord) {
        applicationDao.insert(app)
        jobDao.updateApplicationStatus(app.jobId, app.status)
    }

    suspend fun updateJob(job: JobPost) {
        jobDao.updateJob(job)
    }
}
