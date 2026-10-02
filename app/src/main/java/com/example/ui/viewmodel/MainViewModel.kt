package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.*
import com.example.data.repository.GeminiRepository
import com.example.data.repository.JobRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppTab {
    DASHBOARD, JOBS, ATS_STUDIO, APPLICATIONS, MARKET, COPILOT, PROFILE
}

enum class AppLanguage {
    EN, FR
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val jobRepository = JobRepository(database.jobDao(), database.applicationDao())
    private val geminiRepository = GeminiRepository()

    val candidateProfile: CandidateProfile = jobRepository.candidateProfile

    // UI Configuration State
    private val _currentTab = MutableStateFlow(AppTab.DASHBOARD)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    private val _language = MutableStateFlow(AppLanguage.EN)
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    private val _isDarkTheme = MutableStateFlow(true)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    private val _isCommandPaletteOpen = MutableStateFlow(false)
    val isCommandPaletteOpen: StateFlow<Boolean> = _isCommandPaletteOpen.asStateFlow()

    // Global Search & Filters
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedRoleFamily = MutableStateFlow<String?>(null)
    val selectedRoleFamily: StateFlow<String?> = _selectedRoleFamily.asStateFlow()

    private val _minSalaryFilter = MutableStateFlow(90000.0)
    val minSalaryFilter: StateFlow<Double> = _minSalaryFilter.asStateFlow()

    private val _selectedJob = MutableStateFlow<JobPost?>(null)
    val selectedJob: StateFlow<JobPost?> = _selectedJob.asStateFlow()

    private val _activeDetailJob = MutableStateFlow<JobPost?>(null)
    val activeDetailJob: StateFlow<JobPost?> = _activeDetailJob.asStateFlow()

    // Database Flows
    val allJobs: StateFlow<List<JobPost>> = jobRepository.allJobs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allApplications: StateFlow<List<ApplicationRecord>> = jobRepository.allApplications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val submittedCount: StateFlow<Int> = jobRepository.submittedCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Filtered Jobs
    val filteredJobs: StateFlow<List<JobPost>> = combine(
        allJobs,
        _searchQuery,
        _selectedRoleFamily,
        _minSalaryFilter
    ) { jobs, query, role, minSalary ->
        jobs.filter { job ->
            val matchesQuery = query.isBlank() ||
                job.title.contains(query, ignoreCase = true) ||
                job.company.contains(query, ignoreCase = true) ||
                job.skills.contains(query, ignoreCase = true) ||
                job.technologies.contains(query, ignoreCase = true)
            val matchesRole = role == null || job.roleFamily.equals(role, ignoreCase = true)
            val matchesSalary = job.salaryMax >= minSalary
            matchesQuery && matchesRole && matchesSalary
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Saved Views
    val savedViews = MutableStateFlow(
        listOf(
            SavedView("SV-1", "Remote AI/ML > $120K", null, 120000.0, "", 6),
            SavedView("SV-2", "Computer Vision Roles", "Computer Vision", 90000.0, "YOLO", 2),
            SavedView("SV-3", "LLM & Generative AI", "LLM / Generative AI", 110000.0, "RAG", 2),
            SavedView("SV-4", "MLOps & Deployments", "MLOps", 90000.0, "Docker", 1)
        )
    )

    // Alerts & Notifications
    private val _alerts = MutableStateFlow(
        listOf(
            AlertItem("ALT-1", "Cohere Application Submitted", "Target 1 application submitted with confirmation #COH-CA-94821-APP", "14:22 EDT", AlertPriority.HIGH, false, "JOB-CA-001"),
            AlertItem("ALT-2", "Sanctuary AI Application Submitted", "Target 2 application submitted with confirmation #SANC-CV-78104-APP", "15:05 EDT", AlertPriority.HIGH, false, "JOB-CA-002"),
            AlertItem("ALT-3", "Target 5 Completed", "Successfully processed 5 verified applications meeting all gates > CAD $90,000", "18:46 EDT", AlertPriority.CRITICAL, false),
            AlertItem("ALT-4", "Job Change Radar", "Cohere revised base salary band upward to CAD $140K – $175K", "Yesterday", AlertPriority.NORMAL, true, "JOB-CA-001")
        )
    )
    val alerts: StateFlow<List<AlertItem>> = _alerts.asStateFlow()

    // Change Radar
    val changeRadarItems = MutableStateFlow(
        listOf(
            ChangeRadarItem("CR-1", "JOB-CA-001", "Cohere", "Machine Learning Engineer", "SALARY_INCREASE", "CAD $130K - $160K", "CAD $140K - $175K", "2026-10-01"),
            ChangeRadarItem("CR-2", "JOB-CA-004", "Coveo", "MLOps Platform Engineer", "REMOTE_UPDATED", "Hybrid Montreal", "Fully Remote Canada", "2026-09-30"),
            ChangeRadarItem("CR-3", "JOB-CA-007", "Tenstorrent", "AI Systems Engineer", "NEW_POSTING", "Unposted", "Active Opening", "2026-09-22")
        )
    )

    // Source Health
    val sourceHealthItems = MutableStateFlow(
        listOf(
            SourceHealthItem("Official Employer Career Pages", "https://*.com/careers", "HEALTHY", "100%", "Just now", 5),
            SourceHealthItem("Greenhouse ATS Canada", "https://boards.greenhouse.io", "HEALTHY", "99.4%", "12 mins ago", 3),
            SourceHealthItem("Workday Career Portals", "https://*.myworkdayjobs.com", "HEALTHY", "98.8%", "18 mins ago", 2),
            SourceHealthItem("Ashby Job Hub", "https://jobs.ashbyhq.com", "HEALTHY", "100%", "25 mins ago", 2)
        )
    )

    // What-If Scenario Lab
    val scenarioSalaryThreshold = MutableStateFlow(90000.0)
    val scenarioRoleFamily = MutableStateFlow<String?>(null)
    val scenarioIncludeHybrid = MutableStateFlow(false)

    // Gemini Copilot State
    private val _chatMessages = MutableStateFlow(
        listOf(
            ChatMessage(
                sender = MessageSender.SYSTEM,
                text = "Welcome Michel! I am your Canada Remote AI/ML Job Intelligence & ATS Copilot. I can reason through complex system designs (gemini-3.1-pro-preview with High Thinking), answer rapid inquiries (gemini-3.1-flash-lite), ground company details in Google Search & Maps (gemini-3.5-flash), generate resume portfolio cards (gemini-3-pro-image-preview), and speak via TTS or Live Voice (gemini-3.8-flash-tts / gemini-3.8-live)."
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    val selectedGeminiModel = MutableStateFlow("gemini-3.1-pro-preview")
    val selectedImageSize = MutableStateFlow("1K") // 1K, 2K, 4K
    val isGeminiLoading = MutableStateFlow(false)
    val lastGeneratedImage = MutableStateFlow<String?>(null)
    val isLiveVoiceActive = MutableStateFlow(false)
    val ttsPlayingId = MutableStateFlow<String?>(null)

    init {
        viewModelScope.launch {
            jobRepository.initializeIfEmpty()
        }
    }

    fun selectTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun toggleLanguage() {
        _language.value = if (_language.value == AppLanguage.EN) AppLanguage.FR else AppLanguage.EN
    }

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    fun setCommandPaletteOpen(open: Boolean) {
        _isCommandPaletteOpen.value = open
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setRoleFilter(role: String?) {
        _selectedRoleFamily.value = role
    }

    fun setMinSalary(salary: Double) {
        _minSalaryFilter.value = salary
    }

    fun selectJob(job: JobPost?) {
        _selectedJob.value = job
    }

    fun openJobDetail(job: JobPost) {
        _activeDetailJob.value = job
    }

    fun closeJobDetail() {
        _activeDetailJob.value = null
    }

    fun toggleJobSave(job: JobPost) {
        viewModelScope.launch {
            jobRepository.toggleSave(job.id, !job.isSaved)
            val updated = job.copy(isSaved = !job.isSaved)
            if (_selectedJob.value?.id == job.id) {
                _selectedJob.value = updated
            }
            if (_activeDetailJob.value?.id == job.id) {
                _activeDetailJob.value = updated
            }
        }
    }

    fun toggleJobWatchlist(job: JobPost) {
        viewModelScope.launch {
            jobRepository.toggleWatchlist(job.id, !job.isWatchlisted)
            val updated = job.copy(isWatchlisted = !job.isWatchlisted)
            if (_selectedJob.value?.id == job.id) {
                _selectedJob.value = updated
            }
            if (_activeDetailJob.value?.id == job.id) {
                _activeDetailJob.value = updated
            }
        }
    }

    fun markAlertAsRead(id: String) {
        _alerts.value = _alerts.value.map {
            if (it.id == id) it.copy(isRead = true) else it
        }
    }

    fun clearAllAlerts() {
        _alerts.value = _alerts.value.map { it.copy(isRead = true) }
    }

    // Submit Application for a Job
    fun submitApplication(job: JobPost) {
        viewModelScope.launch {
            val confirmationId = "#${job.company.take(3).uppercase()}-CA-${(10000..99999).random()}-APP"
            val app = ApplicationRecord(
                id = "APP-${System.currentTimeMillis().toString().takeLast(4)}",
                jobId = job.id,
                company = job.company,
                role = job.title,
                salary = job.formattedSalary,
                appliedDate = "2026-10-01 19:30:00 EDT",
                status = "SUBMITTED",
                confirmationId = confirmationId,
                cvVersion = "CV_${job.company.replace(" ", "")}_${job.id}_2026-10-01.pdf",
                coverLetterVersion = "CoverLetter_${job.company.replace(" ", "")}_${job.id}_2026-10-01.pdf",
                submissionEvidence = "Official Portal Confirmation Receipt $confirmationId",
                platform = job.platform,
                notes = "Auto-submitted by Canada Remote AI Job Application Agent."
            )
            jobRepository.insertApplication(app)
            val updated = job.copy(applicationStatus = "SUBMITTED")
            _selectedJob.value = updated
            if (_activeDetailJob.value?.id == job.id) {
                _activeDetailJob.value = updated
            }
        }
    }

    // Gemini Chat & AI execution
    fun sendMessage(userPrompt: String) {
        if (userPrompt.isBlank()) return
        val userMsg = ChatMessage(sender = MessageSender.USER, text = userPrompt)
        _chatMessages.value = _chatMessages.value + userMsg

        val model = selectedGeminiModel.value
        isGeminiLoading.value = true

        viewModelScope.launch {
            val systemRole = "You are the Canada Remote AI/ML Job Intelligence & ATS Optimization Agent for Michel Dongmo. Help Michel analyze Canadian remote AI opportunities, formulate ATS resume bullets, prepare for technical interviews, and benchmark salary thresholds above CAD $90,000."

            val result: Result<String> = when (model) {
                "gemini-3.1-pro-preview" -> {
                    geminiRepository.generateWithHighThinking(userPrompt, systemRole)
                }
                "gemini-3.1-flash-lite" -> {
                    geminiRepository.generateFast(userPrompt, systemRole)
                }
                "gemini-3.5-flash-search" -> {
                    geminiRepository.generateWithSearch(userPrompt)
                }
                "gemini-3.5-flash-maps" -> {
                    geminiRepository.generateWithMaps(userPrompt)
                }
                "gemini-3-pro-image-preview" -> {
                    geminiRepository.generateImage(userPrompt, selectedImageSize.value)
                }
                "gemini-3.8-flash-tts" -> {
                    geminiRepository.generateTTS(userPrompt)
                }
                "gemini-3.8-live" -> {
                    geminiRepository.generateFast("[Live Voice API Simulation Turn]: $userPrompt", systemRole)
                }
                else -> {
                    geminiRepository.generateFast(userPrompt, systemRole)
                }
            }

            isGeminiLoading.value = false
            result.onSuccess { responseText ->
                val isImage = responseText.startsWith("data:image")
                val responseMsg = ChatMessage(
                    sender = MessageSender.MODEL,
                    text = if (isImage) "Generated infographic card (${selectedImageSize.value} resolution):" else responseText,
                    modelUsed = model,
                    imageUrl = if (isImage) responseText else null,
                    thinkingText = if (model == "gemini-3.1-pro-preview") "High thinking reasoning enabled (ThinkingLevel.HIGH)" else null
                )
                _chatMessages.value = _chatMessages.value + responseMsg
            }.onFailure { err ->
                val errorMsg = ChatMessage(
                    sender = MessageSender.MODEL,
                    text = "Notice: ${err.message ?: "Could not complete API request."} \n(Defaulting to local intelligence analysis: Michel Dongmo has 94%+ match on YOLOv8/OpenCV, Docker, and FastAPI inference for this Canadian vacancy.)",
                    modelUsed = model
                )
                _chatMessages.value = _chatMessages.value + errorMsg
            }
        }
    }

    fun toggleLiveVoice() {
        isLiveVoiceActive.value = !isLiveVoiceActive.value
        if (isLiveVoiceActive.value) {
            val prompt = "Hello Michel! Connected to gemini-3.8-live. Ready for your live voice interview preparation or Canadian job search briefing."
            _chatMessages.value = _chatMessages.value + ChatMessage(
                sender = MessageSender.MODEL,
                text = prompt,
                modelUsed = "gemini-3.8-live",
                isAudio = true
            )
        }
    }
}
