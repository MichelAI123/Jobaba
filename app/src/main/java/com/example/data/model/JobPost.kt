package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jobs")
data class JobPost(
    @PrimaryKey val id: String,
    val title: String,
    val company: String,
    val location: String,
    val remoteStatus: String,
    val salaryMin: Double,
    val salaryMax: Double,
    val salaryCurrency: String = "CAD",
    val salaryVerified: Boolean = true,
    val roleFamily: String,
    val description: String,
    val requirements: String,
    val skills: String,
    val technologies: String,
    val postingDate: String,
    val jobUrl: String,
    val platform: String,
    val gateA_Remote: Boolean = true,
    val gateB_Salary: Boolean = true,
    val gateC_Canada: Boolean = true,
    val gateD_Relevance: Boolean = true,
    val matchPercentage: Int = 85,
    val matchStatus: String = "MATCHED",
    val isSaved: Boolean = false,
    val isWatchlisted: Boolean = false,
    val applicationStatus: String = "QUALIFYING" // DISCOVERED, QUALIFYING, READY_TO_APPLY, SUBMITTED, BLOCKED
) {
    val isQualifying: Boolean
        get() = gateA_Remote && gateB_Salary && gateC_Canada && gateD_Relevance

    val formattedSalary: String
        get() = if (salaryMin > 0 && salaryMax > 0) {
            "$$salaryCurrency ${String.format("%,.0f", salaryMin)} – ${String.format("%,.0f", salaryMax)}"
        } else if (salaryMin > 0) {
            "$$salaryCurrency ${String.format("%,.0f", salaryMin)}+"
        } else {
            "Competitive (> CAD $90,000)"
        }
}
