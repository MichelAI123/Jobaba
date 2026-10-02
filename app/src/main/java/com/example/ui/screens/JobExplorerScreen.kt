package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.JobPost
import com.example.ui.components.GateBadge
import com.example.ui.theme.StatusInfo
import com.example.ui.theme.StatusPurple
import com.example.ui.theme.StatusSuccess
import com.example.ui.viewmodel.AppLanguage
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobExplorerScreen(
    viewModel: MainViewModel,
    onNavigate: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val language by viewModel.language.collectAsState()
    val filteredJobs by viewModel.filteredJobs.collectAsState()
    val selectedRole by viewModel.selectedRoleFamily.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedJob by viewModel.selectedJob.collectAsState()
    val savedViews by viewModel.savedViews.collectAsState()

    val roles = listOf(
        null to if (language == AppLanguage.EN) "All Roles" else "Tous",
        "Computer Vision" to "Computer Vision",
        "LLM / Generative AI" to "LLM & GenAI",
        "MLOps" to "MLOps",
        "Research / R&D" to "Research & R&D",
        "AI / Machine Learning" to "AI / ML"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Search Field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            placeholder = {
                Text(if (language == AppLanguage.EN) "Search jobs, skills, companies..." else "Rechercher postes, compétences...")
            },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.setSearchQuery("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Role Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(roles) { (roleValue, roleLabel) ->
                FilterChip(
                    selected = selectedRole == roleValue,
                    onClick = { viewModel.setRoleFilter(roleValue) },
                    label = { Text(roleLabel) },
                    shape = RoundedCornerShape(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Results Count Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${filteredJobs.size} " + if (language == AppLanguage.EN) "Qualifying Canadian Remote Jobs" else "Emplois Télétravail Canada Qualifiés",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Gate B > $90K CAD",
                style = MaterialTheme.typography.labelSmall,
                color = StatusSuccess
            )
        }

        // Job List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = 4.dp, bottom = 32.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredJobs) { job ->
                JobCardItem(
                    job = job,
                    language = language,
                    onSelect = { viewModel.openJobDetail(job) },
                    onToggleSave = { viewModel.toggleJobSave(job) },
                    onToggleWatchlist = { viewModel.toggleJobWatchlist(job) }
                )
            }
        }
    }

    // Job Detail BottomSheet or Dialog
    selectedJob?.let { job ->
        JobDetailDialog(
            job = job,
            language = language,
            onDismiss = { viewModel.selectJob(null) },
            onSubmitApplication = {
                viewModel.submitApplication(job)
            },
            onGenerateDoc = {
                viewModel.selectJob(null)
                onNavigate(AppTab.ATS_STUDIO)
            },
            onOpenCopilot = {
                viewModel.selectJob(null)
                onNavigate(AppTab.COPILOT)
            }
        )
    }
}

@Composable
fun JobCardItem(
    job: JobPost,
    language: AppLanguage,
    onSelect: () -> Unit,
    onToggleSave: () -> Unit,
    onToggleWatchlist: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = job.company,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = job.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when (job.applicationStatus) {
                        "SUBMITTED" -> StatusSuccess.copy(alpha = 0.2f)
                        "READY_TO_APPLY" -> StatusInfo.copy(alpha = 0.2f)
                        else -> MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    }
                ) {
                    Text(
                        text = job.applicationStatus,
                        style = MaterialTheme.typography.labelSmall,
                        color = when (job.applicationStatus) {
                            "SUBMITTED" -> StatusSuccess
                            "READY_TO_APPLY" -> StatusInfo
                            else -> MaterialTheme.colorScheme.primary
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Place,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = job.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = job.formattedSalary,
                style = MaterialTheme.typography.labelLarge,
                color = StatusSuccess
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Gates Verification Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                GateBadge(label = "Remote", passed = job.gateA_Remote)
                GateBadge(label = ">$90K", passed = job.gateB_Salary)
                GateBadge(label = "Canada", passed = job.gateC_Canada)
                GateBadge(label = "AI/ML", passed = job.gateD_Relevance)

                Spacer(modifier = Modifier.weight(1f))

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = StatusPurple.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "${job.matchPercentage}% Match",
                        color = StatusPurple,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Action row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
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

@Composable
fun JobDetailDialog(
    job: JobPost,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSubmitApplication: () -> Unit,
    onGenerateDoc: () -> Unit,
    onOpenCopilot: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(text = job.title, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${job.company} • ${job.location}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = job.formattedSalary,
                    style = MaterialTheme.typography.titleMedium,
                    color = StatusSuccess
                )
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 420.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        text = if (language == AppLanguage.EN) "MANDATORY QUALIFICATION GATES" else "PORTES DE QUALIFICATION OBLIGATOIRES",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        GateBadge(label = "GATE A: Remote Canada", passed = job.gateA_Remote)
                        GateBadge(label = "GATE B: >$90K CAD", passed = job.gateB_Salary)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        GateBadge(label = "GATE C: Canada Eligible", passed = job.gateC_Canada)
                        GateBadge(label = "GATE D: Tech Relevance", passed = job.gateD_Relevance)
                    }
                }

                item {
                    Text(
                        text = if (language == AppLanguage.EN) "CANDIDATE EVIDENCE MATCH" else "CORRESPONDANCE PREUVES CANDIDAT",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Match Score: ${job.matchPercentage}% • Verified Michel Dongmo Background (SIACP ALPR, Air Quality API, La Cité GPA 4.02).",
                        style = MaterialTheme.typography.bodySmall,
                        color = StatusSuccess
                    )
                }

                item {
                    Text(
                        text = if (language == AppLanguage.EN) "ROLE DESCRIPTION" else "DESCRIPTION DU POSTE",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = job.description, style = MaterialTheme.typography.bodySmall)
                }

                item {
                    Text(
                        text = if (language == AppLanguage.EN) "REQUIRED STACK & SKILLS" else "STACK TECHNIQUE REQUISE",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = job.skills, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                }

                item {
                    Text(
                        text = if (language == AppLanguage.EN) "APPLICATION STATUS" else "STATUT CANDIDATURE",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Status: ${job.applicationStatus} (Platform: ${job.platform})",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (job.applicationStatus != "SUBMITTED") {
                    Button(onClick = { onSubmitApplication() }) {
                        Text(if (language == AppLanguage.EN) "Submit Application" else "Soumettre Candidature")
                    }
                }
                FilledTonalButton(onClick = onGenerateDoc) {
                    Text(if (language == AppLanguage.EN) "ATS Studio" else "Studio ATS")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (language == AppLanguage.EN) "Close" else "Fermer")
            }
        }
    )
}
