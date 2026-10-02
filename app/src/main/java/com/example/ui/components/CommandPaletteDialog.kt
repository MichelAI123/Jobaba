package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.ui.viewmodel.AppLanguage
import com.example.ui.viewmodel.AppTab

data class CommandAction(
    val titleEn: String,
    val titleFr: String,
    val subtitle: String,
    val icon: ImageVector,
    val onSelect: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommandPaletteDialog(
    isOpen: Boolean,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onNavigate: (AppTab) -> Unit,
    onToggleTheme: () -> Unit,
    onToggleLanguage: () -> Unit,
    onFilterRole: (String?) -> Unit
) {
    if (!isOpen) return

    var query by remember { mutableStateOf("") }

    val allActions = listOf(
        CommandAction(
            titleEn = "Go to Executive Dashboard",
            titleFr = "Tableau de Bord Exécutif",
            subtitle = "Overview, KPIs & Telemetry",
            icon = Icons.Default.Dashboard,
            onSelect = { onNavigate(AppTab.DASHBOARD); onDismiss() }
        ),
        CommandAction(
            titleEn = "Search Canadian Remote Jobs",
            titleFr = "Recherche d'Emplois Télétravail Canada",
            subtitle = "Verified AI/ML vacancies > $90K",
            icon = Icons.Default.Work,
            onSelect = { onNavigate(AppTab.JOBS); onDismiss() }
        ),
        CommandAction(
            titleEn = "Open ATS Resume & Cover Letter Studio",
            titleFr = "Studio de CV ATS & Lettres de Motivation",
            subtitle = "Garamond PDF preview & tailoring",
            icon = Icons.Default.Description,
            onSelect = { onNavigate(AppTab.ATS_STUDIO); onDismiss() }
        ),
        CommandAction(
            titleEn = "View 5 Submitted Applications & Audit",
            titleFr = "Voir les 5 Candidatures & Audit",
            subtitle = "Confirmation tokens, timestamps, evidence",
            icon = Icons.AutoMirrored.Filled.Send,
            onSelect = { onNavigate(AppTab.APPLICATIONS); onDismiss() }
        ),
        CommandAction(
            titleEn = "Market Intelligence & What-If Simulator",
            titleFr = "Intelligence du Marché & Simulateur",
            subtitle = "Salary distributions & skills matrix",
            icon = Icons.Default.Insights,
            onSelect = { onNavigate(AppTab.MARKET); onDismiss() }
        ),
        CommandAction(
            titleEn = "Ask Gemini AI Copilot (Thinking / Fast / Live)",
            titleFr = "Interroger le Copilote Gemini IA",
            subtitle = "High Thinking, Flash-Lite, Grounded Search & Maps",
            icon = Icons.Default.AutoAwesome,
            onSelect = { onNavigate(AppTab.COPILOT); onDismiss() }
        ),
        CommandAction(
            titleEn = "Candidate Verified Profile & Roadmap",
            titleFr = "Profil Vérifié & Feuille de Route",
            subtitle = "Michel Dongmo factual background",
            icon = Icons.Default.Person,
            onSelect = { onNavigate(AppTab.PROFILE); onDismiss() }
        ),
        CommandAction(
            titleEn = "Filter: Computer Vision Roles",
            titleFr = "Filtrer: Rôles Vision par Ordinateur",
            subtitle = "YOLOv8, OpenCV, ALPR",
            icon = Icons.Default.CameraAlt,
            onSelect = { onFilterRole("Computer Vision"); onNavigate(AppTab.JOBS); onDismiss() }
        ),
        CommandAction(
            titleEn = "Filter: LLM & Generative AI",
            titleFr = "Filtrer: Rôles LLM & IA Générative",
            subtitle = "RAG, MCP, LangChain, Transformers",
            icon = Icons.Default.Psychology,
            onSelect = { onFilterRole("LLM / Generative AI"); onNavigate(AppTab.JOBS); onDismiss() }
        ),
        CommandAction(
            titleEn = "Filter: MLOps & Platform",
            titleFr = "Filtrer: Rôles MLOps & Plateforme",
            subtitle = "Docker, MLflow, CI/CD, FastAPI",
            icon = Icons.Default.Build,
            onSelect = { onFilterRole("MLOps"); onNavigate(AppTab.JOBS); onDismiss() }
        ),
        CommandAction(
            titleEn = "Toggle Dark / Light Theme",
            titleFr = "Basculer Thème Sombre / Clair",
            subtitle = "Emerald Command theme",
            icon = Icons.Default.BrightnessMedium,
            onSelect = { onToggleTheme(); onDismiss() }
        ),
        CommandAction(
            titleEn = "Toggle Language (EN / FR)",
            titleFr = "Changer la Langue (FR / EN)",
            subtitle = "Bilingual UI localization",
            icon = Icons.Default.Translate,
            onSelect = { onToggleLanguage(); onDismiss() }
        )
    )

    val filteredActions = allActions.filter {
        val title = if (language == AppLanguage.EN) it.titleEn else it.titleFr
        title.contains(query, ignoreCase = true) || it.subtitle.contains(query, ignoreCase = true)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = if (language == AppLanguage.EN) "Universal Command Palette" else "Palette de Commande Universelle",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = {
                        Text(if (language == AppLanguage.EN) "Type a command or query..." else "Tapez une commande...")
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 350.dp)
            ) {
                items(filteredActions) { action ->
                    ListItem(
                        headlineContent = {
                            Text(if (language == AppLanguage.EN) action.titleEn else action.titleFr)
                        },
                        supportingContent = {
                            Text(action.subtitle, style = MaterialTheme.typography.bodySmall)
                        },
                        leadingContent = {
                            Icon(action.icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        },
                        modifier = Modifier
                            .clickable { action.onSelect() }
                            .fillMaxWidth()
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(if (language == AppLanguage.EN) "Close" else "Fermer")
            }
        }
    )
}
