package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
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
import com.example.ui.viewmodel.AppLanguage
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobDetailScreen(
    job: JobPost,
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onNavigate: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val language by viewModel.language.collectAsState()
    val candidate = viewModel.candidateProfile

    // Handle Android system back button via state-driven approach
    BackHandler {
        onBack()
    }

    var applicationSubmittedJustNow by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = job.company,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to listings"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleJobSave(job) }) {
                        Icon(
                            imageVector = if (job.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (job.isSaved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = { viewModel.toggleJobWatchlist(job) }) {
                        Icon(
                            imageVector = if (job.isWatchlisted) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Watchlist",
                            tint = if (job.isWatchlisted) StatusPurple else MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 6.dp,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilledTonalButton(
                        onClick = {
                            onNavigate(AppTab.ATS_STUDIO)
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (language == AppLanguage.EN) "ATS Studio" else "Studio ATS", fontSize = 13.sp)
                    }

                    Button(
                        onClick = {
                            if (job.applicationStatus != "SUBMITTED") {
                                viewModel.submitApplication(job)
                                applicationSubmittedJustNow = true
                            }
                        },
                        enabled = job.applicationStatus != "SUBMITTED",
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (job.applicationStatus == "SUBMITTED" || applicationSubmittedJustNow)
                                StatusSuccess else MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.weight(1.3f)
                    ) {
                        Icon(
                            imageVector = if (job.applicationStatus == "SUBMITTED" || applicationSubmittedJustNow)
                                Icons.Default.CheckCircle else Icons.AutoMirrored.Filled.Send,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (job.applicationStatus == "SUBMITTED" || applicationSubmittedJustNow)
                                (if (language == AppLanguage.EN) "Submitted" else "Soumis")
                            else
                                (if (language == AppLanguage.EN) "Submit Application" else "Postuler"),
                            fontSize = 13.sp
                        )
                    }
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
        ) {
            // 1. Hero Job Card
            item {
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = job.company,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = job.title,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
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
                                    text = if (applicationSubmittedJustNow) "SUBMITTED" else job.applicationStatus,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = when (job.applicationStatus) {
                                        "SUBMITTED" -> StatusSuccess
                                        "READY_TO_APPLY" -> StatusInfo
                                        else -> MaterialTheme.colorScheme.onPrimaryContainer
                                    },
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Place, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                            Text(text = job.location, style = MaterialTheme.typography.bodyMedium)
                            Text(text = "•", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = job.remoteStatus, style = MaterialTheme.typography.bodyMedium, color = StatusSuccess, fontWeight = FontWeight.Medium)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = job.formattedSalary,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = StatusSuccess
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Platform: ${job.platform} • Posted: ${job.postingDate}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // 2. Candidate Match Score & Evidence Breakdown
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.Psychology, contentDescription = null, tint = StatusPurple, modifier = Modifier.size(22.dp))
                                Text(
                                    text = if (language == AppLanguage.EN) "Candidate Match Evidence" else "Preuves de Correspondance",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = StatusPurple.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "${job.matchPercentage}% Match",
                                    color = StatusPurple,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "✓ Computer Vision & Detection: SIACP ALPR system (YOLOv8 + EasyOCR + OpenCV)\n" +
                                   "✓ Model Inference & APIs: Air Quality Prediction Service (FastAPI + Docker, F1=0.8284)\n" +
                                   "✓ MLOps & Reproducibility: MLflow experiment tracking, Docker Compose orchestration\n" +
                                   "✓ Academic Foundation: Ongoing Ph.D in Math Physics; Collège La Cité (GPA 4.02/4.3)",
                            style = MaterialTheme.typography.bodySmall,
                            lineHeight = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // 3. Four Mandatory Qualification Gates
            item {
                Text(
                    text = if (language == AppLanguage.EN) "Qualification Gates Verification" else "Vérification des Portes d'Admissibilité",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    GateCheckItem(
                        gateName = "GATE A — Remote Status",
                        statusText = "Fully Remote across Canada (Ottawa/National)",
                        passed = job.gateA_Remote
                    )
                    GateCheckItem(
                        gateName = "GATE B — Salary Compensation",
                        statusText = "Verified Base Salary > CAD $80,000 threshold (${job.formattedSalary})",
                        passed = job.gateB_Salary
                    )
                    GateCheckItem(
                        gateName = "GATE C — Canadian Eligibility",
                        statusText = "Employment explicitly authorized from Canadian provinces",
                        passed = job.gateC_Canada
                    )
                    GateCheckItem(
                        gateName = "GATE D — Technical Relevance",
                        statusText = "Core focus on AI, ML, Computer Vision, MLOps or Generative AI",
                        passed = job.gateD_Relevance
                    )
                }
            }

            // 4. Full Job Description
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (language == AppLanguage.EN) "Full Job Description" else "Description Complète du Poste",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = job.description,
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 22.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // 5. Requirements & Responsibilities
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (language == AppLanguage.EN) "Requirements & Responsibilities" else "Exigences & Responsabilités",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = job.requirements,
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 22.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // 6. Technical Stack Chips
            item {
                Text(
                    text = if (language == AppLanguage.EN) "Required Stack & Technologies" else "Stack Technique & Outils",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(job.skills.split(",").map { it.trim() }) { skill ->
                        SuggestionChip(
                            onClick = {},
                            label = { Text(skill, fontSize = 12.sp) }
                        )
                    }
                }
            }

            // 7. Gemini Intelligence Actions for this role
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Text(
                                text = if (language == AppLanguage.EN) "Gemini Intelligence Assist" else "Assistance Gemini IA",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Analyze company hiring culture, ground remote details with Google Maps, or generate tailored interview responses for ${job.company}.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = {
                                    viewModel.sendMessage("Verify Canadian office locations and remote work verification for ${job.company} (${job.location}) using Google Maps.")
                                    onNavigate(AppTab.COPILOT)
                                },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.PinDrop, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Maps Grounding", fontSize = 12.sp)
                            }

                            FilledTonalButton(
                                onClick = {
                                    viewModel.sendMessage("Analyze role requirements for ${job.title} at ${job.company} and provide 3 key STAR technical interview questions tailored to Michel Dongmo's YOLOv8 and FastAPI background.")
                                    onNavigate(AppTab.COPILOT)
                                },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Psychology, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Interview Q&A", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GateCheckItem(
    gateName: String,
    statusText: String,
    passed: Boolean
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = gateName, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Text(text = statusText, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Surface(
                shape = CircleShape,
                color = if (passed) StatusSuccess.copy(alpha = 0.15f) else MaterialTheme.colorScheme.error.copy(alpha = 0.15f),
                modifier = Modifier.size(28.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (passed) Icons.Default.Check else Icons.Default.Close,
                        contentDescription = null,
                        tint = if (passed) StatusSuccess else MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
