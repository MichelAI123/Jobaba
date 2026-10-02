package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.JobPost
import com.example.ui.components.GateBadge
import com.example.ui.theme.StatusInfo
import com.example.ui.theme.StatusPurple
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusWarning
import com.example.ui.viewmodel.AppLanguage
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: MainViewModel,
    onNavigate: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val language by viewModel.language.collectAsState()
    val allJobs by viewModel.allJobs.collectAsState()
    val submittedApps by viewModel.allApplications.collectAsState()
    val selectedRole by viewModel.selectedRoleFamily.collectAsState()
    val candidate = viewModel.candidateProfile

    var localSearchQuery by remember { mutableStateOf("") }
    var selectedJobForDetail by remember { mutableStateOf<JobPost?>(null) }
    var mapsGroundingPrompt by remember { mutableStateOf<String?>(null) }

    // Filter jobs for the discovery feed
    val discoveryJobs = remember(allJobs, selectedRole, localSearchQuery) {
        allJobs.filter { job ->
            val matchesRole = selectedRole == null || job.roleFamily.equals(selectedRole, ignoreCase = true)
            val matchesQuery = localSearchQuery.isBlank() ||
                job.title.contains(localSearchQuery, ignoreCase = true) ||
                job.company.contains(localSearchQuery, ignoreCase = true) ||
                job.skills.contains(localSearchQuery, ignoreCase = true)
            matchesRole && matchesQuery
        }
    }

    val roleFilters = listOf(
        null to if (language == AppLanguage.EN) "All Canadian Roles" else "Tous les Rôles",
        "Computer Vision" to "Computer Vision",
        "LLM / Generative AI" to "LLM & GenAI",
        "MLOps" to "MLOps & Deploy",
        "Research / R&D" to "Research / R&D",
        "AI / Machine Learning" to "AI / ML Core"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 36.dp)
    ) {
        // 1. PRE-LOADED MICHEL DONGMO PROFILE HERO CARD (Material3)
        item {
            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(48.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "MD",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Column {
                                Text(
                                    text = candidate.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Place,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = candidate.location,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        // Authenticated / Profile Sync Badge
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = StatusSuccess.copy(alpha = 0.15f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Surface(shape = CircleShape, color = StatusSuccess, modifier = Modifier.size(6.dp)) {}
                                Text(
                                    text = "VERIFIED CANDIDATE",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = StatusSuccess,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = candidate.headline,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Verified Key Competency Badges
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            SuggestionChip(
                                onClick = {},
                                label = { Text("YOLOv8 ALPR (SIACP)", fontSize = 11.sp) },
                                icon = { Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(14.dp)) }
                            )
                        }
                        item {
                            SuggestionChip(
                                onClick = {},
                                label = { Text("ML API F1=0.8284", fontSize = 11.sp) },
                                icon = { Icon(Icons.Default.Analytics, contentDescription = null, modifier = Modifier.size(14.dp)) }
                            )
                        }
                        item {
                            SuggestionChip(
                                onClick = {},
                                label = { Text("Collège La Cité (4.02/4.3)", fontSize = 11.sp) },
                                icon = { Icon(Icons.Default.School, contentDescription = null, modifier = Modifier.size(14.dp)) }
                            )
                        }
                        item {
                            SuggestionChip(
                                onClick = {},
                                label = { Text("Ph.D Math Physics", fontSize = 11.sp) },
                                icon = { Icon(Icons.Default.Science, contentDescription = null, modifier = Modifier.size(14.dp)) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${candidate.email} • 5 Applied",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        TextButton(onClick = { onNavigate(AppTab.PROFILE) }) {
                            Text(if (language == AppLanguage.EN) "Full Profile & CV" else "Profil & CV Complet")
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }

        // 2. DISCOVERY TELEMETRY STRIP (Material3 Card)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "${allJobs.size}", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        Text(text = "Active Canadian Jobs", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    VerticalDivider(modifier = Modifier.height(36.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "100%", style = MaterialTheme.typography.titleLarge, color = StatusSuccess, fontWeight = FontWeight.Bold)
                        Text(text = "Remote Canada", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    VerticalDivider(modifier = Modifier.height(36.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "${submittedApps.size}", style = MaterialTheme.typography.titleLarge, color = StatusPurple, fontWeight = FontWeight.Bold)
                        Text(text = "Target Applied", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        // 3. SEARCH & ROLE FILTER BAR FOR JOB DISCOVERY
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (language == AppLanguage.EN) "Active Canadian AI Job Listings" else "Offres d'Emploi IA Actives au Canada",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = StatusSuccess.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "Gate B: > CAD $90,000",
                            style = MaterialTheme.typography.labelSmall,
                            color = StatusSuccess,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }

                // Discovery search input
                OutlinedTextField(
                    value = localSearchQuery,
                    onValueChange = { localSearchQuery = it },
                    placeholder = { Text("Search Canadian AI jobs, skills, companies...", fontSize = 13.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (localSearchQuery.isNotEmpty()) {
                            IconButton(onClick = { localSearchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                // Category filter chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(roleFilters) { (roleValue, label) ->
                        FilterChip(
                            selected = selectedRole == roleValue,
                            onClick = { viewModel.setRoleFilter(roleValue) },
                            label = { Text(label, fontSize = 12.sp) },
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                }
            }
        }

        // 4. LIST OF ACTIVE CANADIAN AI JOB LISTINGS (MATERIAL3 CARDS)
        items(discoveryJobs) { job ->
            Material3JobCard(
                job = job,
                language = language,
                onViewDetails = { viewModel.openJobDetail(job) },
                onGroundMaps = {
                    mapsGroundingPrompt = "Ground location with Google Maps: Verify company office in Canada and remote hiring posture for ${job.company} located in ${job.location} for the position ${job.title}."
                    viewModel.sendMessage("Verify Canadian office and remote eligibility with Google Maps for ${job.company} (${job.location})")
                    onNavigate(AppTab.COPILOT)
                },
                onToggleSave = { viewModel.toggleJobSave(job) },
                onToggleWatchlist = { viewModel.toggleJobWatchlist(job) }
            )
        }
    }

    // Interactive Job Details & Gate Check Modal
    selectedJobForDetail?.let { job ->
        JobDetailDialog(
            job = job,
            language = language,
            onDismiss = { selectedJobForDetail = null },
            onSubmitApplication = {
                viewModel.submitApplication(job)
                selectedJobForDetail = null
            },
            onGenerateDoc = {
                selectedJobForDetail = null
                onNavigate(AppTab.ATS_STUDIO)
            },
            onOpenCopilot = {
                selectedJobForDetail = null
                onNavigate(AppTab.COPILOT)
            }
        )
    }
}

@Composable
fun Material3JobCard(
    job: JobPost,
    language: AppLanguage,
    onViewDetails: () -> Unit,
    onGroundMaps: () -> Unit,
    onToggleSave: () -> Unit,
    onToggleWatchlist: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onViewDetails() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Company, Location, Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = job.company,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = job.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when (job.applicationStatus) {
                        "SUBMITTED" -> StatusSuccess.copy(alpha = 0.2f)
                        "READY_TO_APPLY" -> StatusInfo.copy(alpha = 0.2f)
                        else -> MaterialTheme.colorScheme.primaryContainer
                    }
                ) {
                    Text(
                        text = job.applicationStatus,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = when (job.applicationStatus) {
                            "SUBMITTED" -> StatusSuccess
                            "READY_TO_APPLY" -> StatusInfo
                            else -> MaterialTheme.colorScheme.onPrimaryContainer
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Location & Remote Status Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.Place,
                    contentDescription = null,
                    modifier = Modifier.size(15.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = job.location,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(text = "•", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    text = job.remoteStatus,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = StatusSuccess
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Salary & Match Score
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = job.formattedSalary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = StatusSuccess
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = StatusPurple.copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Default.Psychology,
                            contentDescription = null,
                            tint = StatusPurple,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${job.matchPercentage}% Candidate Match",
                            color = StatusPurple,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 4 Mandatory Gates Badges (Remote, Salary, Canada, AI/ML)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                GateBadge(label = "Remote", passed = job.gateA_Remote)
                GateBadge(label = ">$90K CAD", passed = job.gateB_Salary)
                GateBadge(label = "Canada", passed = job.gateC_Canada)
                GateBadge(label = "AI/ML", passed = job.gateD_Relevance)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Required Skills Snippet
            Text(
                text = "Key Stack: ${job.skills}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Interactive Bottom Actions Row (Material3 buttons)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedButton(
                        onClick = onViewDetails,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(if (language == AppLanguage.EN) "Details & Gates" else "Détails & Portes", fontSize = 12.sp)
                    }

                    // Google Maps Grounding Quick Action
                    FilledTonalButton(
                        onClick = onGroundMaps,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.PinDrop, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Maps Grounding", fontSize = 11.sp)
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onToggleSave, modifier = Modifier.size(36.dp)) {
                        Icon(
                            if (job.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Save",
                            tint = if (job.isSaved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onToggleWatchlist, modifier = Modifier.size(36.dp)) {
                        Icon(
                            if (job.isWatchlisted) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Watchlist",
                            tint = if (job.isWatchlisted) StatusPurple else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
