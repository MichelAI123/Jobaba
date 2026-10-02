package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "applications")
data class ApplicationRecord(
    @PrimaryKey val id: String,
    val jobId: String,
    val company: String,
    val role: String,
    val salary: String,
    val appliedDate: String,
    val status: String, // SUBMITTED, READY_TO_APPLY, IN_REVIEW, INTERVIEW, BLOCKED
    val confirmationId: String,
    val cvVersion: String,
    val coverLetterVersion: String,
    val submissionEvidence: String,
    val platform: String,
    val notes: String = ""
)
