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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GateBadge
import com.example.ui.theme.StatusInfo
import com.example.ui.theme.StatusPurple
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusWarning
import com.example.ui.viewmodel.AppLanguage
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.MainViewModel

@Composable
fun MarketAnalyticsScreen(
    viewModel: MainViewModel,
    onNavigate: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val language by viewModel.language.collectAsState()
    val allJobs by viewModel.allJobs.collectAsState()
    val sourceHealth by viewModel.sourceHealthItems.collectAsState()

    var activeSubTab by remember { mutableIntStateOf(0) } // 0: Compensation, 1: Skills Graph, 2: What-If Simulator, 3: Sources & Health

    // What-if simulator state
    var simSalaryThreshold by remember { mutableFloatStateOf(90000f) }
    var simSelectedRole by remember { mutableStateOf<String?>(null) }

    val simulatedJobs = allJobs.filter {
        val matchesSalary = it.salaryMax >= simSalaryThreshold
        val matchesRole = simSelectedRole == null || it.roleFamily.equals(simSelectedRole, ignoreCase = true)
        matchesSalary && matchesRole
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Subtabs
        TabRow(selectedTabIndex = activeSubTab) {
            Tab(
                selected = activeSubTab == 0,
                onClick = { activeSubTab = 0 },
                text = { Text(if (language == AppLanguage.EN) "Compensation" else "Rémunération") }
            )
            Tab(
                selected = activeSubTab == 1,
                onClick = { activeSubTab = 1 },
                text = { Text(if (language == AppLanguage.EN) "Skills Matrix" else "Matrice Compétences") }
            )
            Tab(
                selected = activeSubTab == 2,
                onClick = { activeSubTab = 2 },
                text = { Text(if (language == AppLanguage.EN) "What-If Lab" else "Simulateur") }
            )
            Tab(
                selected = activeSubTab == 3,
                onClick = { activeSubTab = 3 },
                text = { Text(if (language == AppLanguage.EN) "Sources" else "Sources") }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (activeSubTab) {
            0 -> CompensationIntelligenceView(language = language)
            1 -> SkillsKnowledgeGraphView(language = language)
            2 -> WhatIfSimulatorView(
                language = language,
                salaryThreshold = simSalaryThreshold,
                onSalaryChange = { simSalaryThreshold = it },
                selectedRole = simSelectedRole,
                onRoleChange = { simSelectedRole = it },
                simulatedCount = simulatedJobs.size,
                totalCount = allJobs.size
            )
            3 -> SourcesAndDataQualityView(sourceHealth = sourceHealth, language = language)
        }
    }
}

@Composable
fun CompensationIntelligenceView(language: AppLanguage) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 32.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (language == AppLanguage.EN) "CANADIAN REMOTE COMPENSATION BENCHMARKS" else "REPÈRES SALARIAUX TÉLÉTRAVAIL CANADA",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Base Salary Distribution (CAD)",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "Mandatory Gate B: Strictly > CAD $80,000 (Targeting > CAD $90,000)",
                        style = MaterialTheme.typography.bodySmall,
                        color = StatusSuccess
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    SalaryBandRow(band = "$140,000 – $185,000 (Senior / Lead)", count = "3 roles (37.5%)", percentage = 0.375f)
                    SalaryBandRow(band = "$125,000 – $140,000 (Mid-Senior Applied)", count = "3 roles (37.5%)", percentage = 0.375f)
                    SalaryBandRow(band = "$110,000 – $125,000 (Intermediate Core)", count = "2 roles (25.0%)", percentage = 0.25f)
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Compensation Summary Statistics", style = MaterialTheme.typography.titleMedium)
                    StatRow(label = "Minimum Verified Base Salary", value = "CAD $115,000")
                    StatRow(label = "Median Observed Salary", value = "CAD $132,500")
                    StatRow(label = "Average Base Salary", value = "CAD $131,250")
                    StatRow(label = "Maximum Observed Base Salary", value = "CAD $185,000")
                    StatRow(label = "Gate B Compliance Rate", value = "100% Verified Compliant")
                }
            }
        }
    }
}

@Composable
fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1.2f)
        )
    }
}

@Composable
fun SalaryBandRow(band: String, count: String, percentage: Float) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = band, style = MaterialTheme.typography.bodySmall)
            Text(text = count, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { percentage },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = StatusSuccess,
            trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        )
    }
}

@Composable
fun SkillsKnowledgeGraphView(language: AppLanguage) {
    val skills = listOf(
        Triple("Python", "100% Demand", "Verified Strength: Advanced OOP & Scientific Packages"),
        Triple("PyTorch", "87.5% Demand", "Verified Strength: Neural Graph Optimization"),
        Triple("Docker / Compose", "87.5% Demand", "Verified Strength: Containerized SIACP & API Deployments"),
        Triple("FastAPI", "75% Demand", "Verified Strength: Asynchronous Model Inference Endpoints"),
        Triple("MLflow", "62.5% Demand", "Verified Strength: Experiment Tracking & Artifact Registry"),
        Triple("Computer Vision / YOLO", "50% Demand", "Verified Strength: ALPR Detection & EasyOCR"),
        Triple("LLM / RAG / MCP", "50% Demand", "Verified Strength: Agentic Tool Integration & Vector Context"),
        Triple("C++", "37.5% Demand", "Verified Strength: Embedded IoT & Numerical Simulation")
    )

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 32.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(skills) { (skill, freq, coverage) ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
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
                        Text(text = skill, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = coverage, style = MaterialTheme.typography.bodySmall, color = StatusSuccess)
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = StatusPurple.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = freq,
                            style = MaterialTheme.typography.labelSmall,
                            color = StatusPurple,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WhatIfSimulatorView(
    language: AppLanguage,
    salaryThreshold: Float,
    onSalaryChange: (Float) -> Unit,
    selectedRole: String?,
    onRoleChange: (String?) -> Unit,
    simulatedCount: Int,
    totalCount: Int
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 32.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (language == AppLanguage.EN) "WHAT-IF MARKET SCENARIO LAB" else "LABORATOIRE DE SCÉNARIOS WHAT-IF",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$simulatedCount of $totalCount Jobs Qualify Under Scenario",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Simulating dynamic constraints without altering authoritative candidate or master database records.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f)
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
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Salary Threshold: CAD $${salaryThreshold.toInt()}",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Slider(
                        value = salaryThreshold,
                        onValueChange = onSalaryChange,
                        valueRange = 80000f..180000f,
                        steps = 9
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Filter by Role Family:", style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = selectedRole == null,
                            onClick = { onRoleChange(null) },
                            label = { Text("All") }
                        )
                        FilterChip(
                            selected = selectedRole == "Computer Vision",
                            onClick = { onRoleChange("Computer Vision") },
                            label = { Text("Computer Vision") }
                        )
                        FilterChip(
                            selected = selectedRole == "LLM / Generative AI",
                            onClick = { onRoleChange("LLM / Generative AI") },
                            label = { Text("LLM / GenAI") }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SourcesAndDataQualityView(
    sourceHealth: List<com.example.data.model.SourceHealthItem>,
    language: AppLanguage
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 32.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                text = if (language == AppLanguage.EN) "Source Health & Verification Monitor" else "Moniteur d'État des Sources & Vérification",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        items(sourceHealth) { source ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = source.sourceName, style = MaterialTheme.typography.titleMedium)
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = StatusSuccess.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = source.status,
                                color = StatusSuccess,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = source.url, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text(text = "Success Rate: ${source.successRate}", style = MaterialTheme.typography.bodySmall, color = StatusSuccess)
                        Text(text = "Jobs Verified: ${source.jobsFound}", style = MaterialTheme.typography.bodySmall)
                        Text(text = "Last Sync: ${source.lastSync}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
