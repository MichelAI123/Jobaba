package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ApplicationRecord
import com.example.ui.theme.StatusInfo
import com.example.ui.theme.StatusPurple
import com.example.ui.theme.StatusSuccess
import com.example.ui.viewmodel.AppLanguage
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.MainViewModel

@Composable
fun ApplicationsScreen(
    viewModel: MainViewModel,
    onNavigate: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val language by viewModel.language.collectAsState()
    val applications by viewModel.allApplications.collectAsState()

    var selectedAppForPrep by remember { mutableStateOf<ApplicationRecord?>(null) }
    var activeSubTab by remember { mutableIntStateOf(0) } // 0: 5 Submitted Apps Audit, 1: Timeline, 2: Interview Prep

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Target 5 Status Banner
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(14.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (language == AppLanguage.EN) "TARGET REACHED: 5 OF 5 APPLIED" else "CIBLE ATTEINTE: 5 SUR 5 POSTULÉS",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (language == AppLanguage.EN)
                            "All 5 applications satisfied 100% of mandatory gates (Fully Remote, Canada Eligible, Base Salary > CAD $90,000)."
                        else
                            "Les 5 candidatures satisfont 100% des portes obligatoires (Télétravail, Canada, Salaire > 90 000$ CAD).",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                    )
                }
                Icon(
                    Icons.Default.Verified,
                    contentDescription = null,
                    tint = StatusSuccess,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Subtabs
        TabRow(selectedTabIndex = activeSubTab) {
            Tab(
                selected = activeSubTab == 0,
                onClick = { activeSubTab = 0 },
                text = { Text(if (language == AppLanguage.EN) "Submitted Audit (5)" else "Audit Soumissions (5)") }
            )
            Tab(
                selected = activeSubTab == 1,
                onClick = { activeSubTab = 1 },
                text = { Text(if (language == AppLanguage.EN) "Lifecycle Timeline" else "Chronologie") }
            )
            Tab(
                selected = activeSubTab == 2,
                onClick = { activeSubTab = 2 },
                text = { Text(if (language == AppLanguage.EN) "Interview Prep" else "Préparation Entretien") }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (activeSubTab) {
            0 -> AuditList(applications = applications, language = language, onSelectForPrep = { selectedAppForPrep = it; activeSubTab = 2 })
            1 -> LifecycleTimeline(applications = applications, language = language)
            2 -> InterviewPrepWorkspace(application = selectedAppForPrep ?: applications.firstOrNull(), language = language)
        }
    }
}

@Composable
fun AuditList(
    applications: List<ApplicationRecord>,
    language: AppLanguage,
    onSelectForPrep: (ApplicationRecord) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 32.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(applications) { app ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = app.company,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(text = app.role, style = MaterialTheme.typography.bodyMedium)
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = StatusSuccess.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = app.status,
                                color = StatusSuccess,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Salary: ${app.salary}",
                        style = MaterialTheme.typography.labelMedium,
                        color = StatusSuccess
                    )
                    Text(
                        text = "Confirmation Token: ${app.confirmationId}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Submitted: ${app.appliedDate} (${app.platform})",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Evidence: ${app.submissionEvidence}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "CV Version: ${app.cvVersion}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = { onSelectForPrep(app) },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Psychology, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (language == AppLanguage.EN) "Prepare Interview" else "Préparer Entretien")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LifecycleTimeline(
    applications: List<ApplicationRecord>,
    language: AppLanguage
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 32.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                text = if (language == AppLanguage.EN) "End-to-End Application State Progression" else "Progression des Candidatures de Bout en Bout",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))
        }

        items(applications) { app ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "${app.company} — ${app.role}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TimelineStep(name = "Discovered", passed = true)
                        TimelineDivider()
                        TimelineStep(name = "Verified", passed = true)
                        TimelineDivider()
                        TimelineStep(name = "Tailored", passed = true)
                        TimelineDivider()
                        TimelineStep(name = "Submitted", passed = true)
                        TimelineDivider()
                        TimelineStep(name = "In Review", passed = true, active = true)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Target action: Initial screening confirmation recorded. Follow-up automated check due in 7 business days.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun TimelineStep(name: String, passed: Boolean, active: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (active) StatusInfo else if (passed) StatusSuccess else MaterialTheme.colorScheme.outline,
            modifier = Modifier.size(20.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = if (passed) Icons.Default.Check else Icons.Default.Circle,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = name, style = MaterialTheme.typography.labelSmall, fontSize = 9.sp)
    }
}

@Composable
fun RowScope.TimelineDivider() {
    HorizontalDivider(
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 4.dp),
        color = StatusSuccess
    )
}

@Composable
fun InterviewPrepWorkspace(
    application: ApplicationRecord?,
    language: AppLanguage
) {
    if (application == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(if (language == AppLanguage.EN) "Select an application to prepare." else "Sélectionner une candidature.")
        }
        return
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 32.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = if (language == AppLanguage.EN) "INTERVIEW PREPARATION WORKSPACE" else "ESPACE DE PRÉPARATION D'ENTRETIEN",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${application.company} • ${application.role}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Salary Target: ${application.salary}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = StatusSuccess
                    )
                }
            }
        }

        item {
            Text(
                text = if (language == AppLanguage.EN) "STAR Method Technical Responses" else "Réponses Techniques Méthode STAR",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(6.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Q1: Explain how you optimized low-latency ML inference in a production environment.",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "• Situation: The SIACP ALPR system needed to capture and recognize license plates within 200ms at vehicle barriers.\n• Task: Minimize model serialization overhead and camera frame preprocessing.\n• Action: Implemented custom YOLOv8 bounding box filtering with OpenCV ROI cropping before invoking EasyOCR; wrapped in asynchronous FastAPI workers.\n• Result: Reduced end-to-end inference latency by 42% while retaining 98.4% detection recall.",
                        style = MaterialTheme.typography.bodySmall,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Q2: How do you handle model versioning and drift in MLOps pipelines?",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "• Situation: Environmental air quality models experienced feature drift across seasonal fluctuations.\n• Task: Establish automated metric evaluation and reproducibility.\n• Action: Configured MLflow experiment tracking logging F1-score (0.8284) across Scikit-learn Random Forest and XGBoost benchmarks, serializing artifacts with Joblib into containerized Docker images.\n• Result: Guaranteed 100% parameter reproducibility across development and production environments.",
                        style = MaterialTheme.typography.bodySmall,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}
