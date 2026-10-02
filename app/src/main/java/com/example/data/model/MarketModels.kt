package com.example.data.model

data class SavedView(
    val id: String,
    val name: String,
    val filterRole: String? = null,
    val minSalary: Double = 90000.0,
    val keyword: String = "",
    val count: Int = 0
)

data class AlertItem(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val priority: AlertPriority = AlertPriority.NORMAL,
    val isRead: Boolean = false,
    val relatedJobId: String? = null
)

enum class AlertPriority {
    CRITICAL, HIGH, NORMAL, LOW
}

data class ChangeRadarItem(
    val id: String,
    val jobId: String,
    val company: String,
    val role: String,
    val changeType: String, // "SALARY_INCREASE", "REMOTE_UPDATED", "NEW_POSTING", "DEADLINE"
    val previousValue: String,
    val newValue: String,
    val date: String
)

data class SourceHealthItem(
    val sourceName: String,
    val url: String,
    val status: String, // "HEALTHY", "DEGRADED", "AVAILABLE"
    val successRate: String,
    val lastSync: String,
    val jobsFound: Int
)

data class DataConflictItem(
    val id: String,
    val field: String,
    val sourceA: String,
    val sourceB: String,
    val resolvedValue: String,
    val reason: String
)

data class CompetencyItem(
    val skill: String,
    val marketFrequency: String,
    val candidateStatus: String, // "VERIFIED_STRENGTH", "GROWING", "LEARNING_TARGET"
    val candidateEvidence: String,
    val recommendedAction: String
)
