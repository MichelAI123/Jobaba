package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CommandPaletteDialog
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.StatusSuccess
import com.example.ui.viewmodel.AppLanguage
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val isDarkTheme by viewModel.isDarkTheme.collectAsState()
            val currentTab by viewModel.currentTab.collectAsState()
            val language by viewModel.language.collectAsState()
            val activeDetailJob by viewModel.activeDetailJob.collectAsState()
            val isCommandPaletteOpen by viewModel.isCommandPaletteOpen.collectAsState()
            val submittedApps by viewModel.allApplications.collectAsState()

            MyApplicationTheme(darkTheme = isDarkTheme) {
                if (activeDetailJob != null) {
                    JobDetailScreen(
                        job = activeDetailJob!!,
                        viewModel = viewModel,
                        onBack = { viewModel.closeJobDetail() },
                        onNavigate = {
                            viewModel.closeJobDetail()
                            viewModel.selectTab(it)
                        }
                    )
                } else {
                    // Handle system back navigation to return to Dashboard
                    if (currentTab != AppTab.DASHBOARD) {
                        BackHandler {
                            viewModel.selectTab(AppTab.DASHBOARD)
                        }
                    }

                    Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Column {
                                    Text(
                                        text = if (language == AppLanguage.EN) "Canada AI Job Command" else "Commande IA Emploi Canada",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = "Michel Dongmo • 5 Applications Target Met",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = StatusSuccess
                                    )
                                }
                            },
                            actions = {
                                IconButton(onClick = { viewModel.setCommandPaletteOpen(true) }) {
                                    Icon(Icons.Default.Search, contentDescription = "Command Palette")
                                }
                                IconButton(onClick = { viewModel.toggleLanguage() }) {
                                    Text(
                                        text = if (language == AppLanguage.EN) "FR" else "EN",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                IconButton(onClick = { viewModel.toggleTheme() }) {
                                    Icon(
                                        if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                                        contentDescription = "Toggle Theme"
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        )
                    },
                    bottomBar = {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface,
                            tonalElevation = 4.dp
                        ) {
                            NavigationBarItem(
                                selected = currentTab == AppTab.DASHBOARD,
                                onClick = { viewModel.selectTab(AppTab.DASHBOARD) },
                                icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                                label = { Text(if (language == AppLanguage.EN) "Home" else "Accueil", fontSize = 11.sp) }
                            )
                            NavigationBarItem(
                                selected = currentTab == AppTab.JOBS,
                                onClick = { viewModel.selectTab(AppTab.JOBS) },
                                icon = { Icon(Icons.Default.Work, contentDescription = "Jobs") },
                                label = { Text(if (language == AppLanguage.EN) "Jobs" else "Emplois", fontSize = 11.sp) }
                            )
                            NavigationBarItem(
                                selected = currentTab == AppTab.ATS_STUDIO,
                                onClick = { viewModel.selectTab(AppTab.ATS_STUDIO) },
                                icon = { Icon(Icons.Default.Description, contentDescription = "ATS") },
                                label = { Text("ATS", fontSize = 11.sp) }
                            )
                            NavigationBarItem(
                                selected = currentTab == AppTab.APPLICATIONS,
                                onClick = { viewModel.selectTab(AppTab.APPLICATIONS) },
                                icon = {
                                    BadgedBox(
                                        badge = {
                                            Badge { Text("${submittedApps.size}") }
                                        }
                                    ) {
                                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Applications")
                                    }
                                },
                                label = { Text(if (language == AppLanguage.EN) "Applied" else "Postulés", fontSize = 11.sp) }
                            )
                            NavigationBarItem(
                                selected = currentTab == AppTab.MARKET,
                                onClick = { viewModel.selectTab(AppTab.MARKET) },
                                icon = { Icon(Icons.Default.Insights, contentDescription = "Market") },
                                label = { Text(if (language == AppLanguage.EN) "Market" else "Marché", fontSize = 11.sp) }
                            )
                            NavigationBarItem(
                                selected = currentTab == AppTab.COPILOT,
                                onClick = { viewModel.selectTab(AppTab.COPILOT) },
                                icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "Copilot") },
                                label = { Text("Copilot", fontSize = 11.sp) }
                            )
                            NavigationBarItem(
                                selected = currentTab == AppTab.PROFILE,
                                onClick = { viewModel.selectTab(AppTab.PROFILE) },
                                icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                                label = { Text(if (language == AppLanguage.EN) "Profile" else "Profil", fontSize = 11.sp) }
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (currentTab) {
                            AppTab.DASHBOARD -> DashboardScreen(
                                viewModel = viewModel,
                                onNavigate = { viewModel.selectTab(it) }
                            )
                            AppTab.JOBS -> JobExplorerScreen(
                                viewModel = viewModel,
                                onNavigate = { viewModel.selectTab(it) }
                            )
                            AppTab.ATS_STUDIO -> AtsStudioScreen(
                                viewModel = viewModel,
                                onNavigate = { viewModel.selectTab(it) }
                            )
                            AppTab.APPLICATIONS -> ApplicationsScreen(
                                viewModel = viewModel,
                                onNavigate = { viewModel.selectTab(it) }
                            )
                            AppTab.MARKET -> MarketAnalyticsScreen(
                                viewModel = viewModel,
                                onNavigate = { viewModel.selectTab(it) }
                            )
                            AppTab.COPILOT -> GeminiCopilotScreen(
                                viewModel = viewModel,
                                onNavigate = { viewModel.selectTab(it) }
                            )
                            AppTab.PROFILE -> CandidateProfileScreen(
                                viewModel = viewModel,
                                onNavigate = { viewModel.selectTab(it) }
                            )
                        }
                    }

                    // Command Palette Modal
                    CommandPaletteDialog(
                        isOpen = isCommandPaletteOpen,
                        language = language,
                        onDismiss = { viewModel.setCommandPaletteOpen(false) },
                        onNavigate = { viewModel.selectTab(it) },
                        onToggleTheme = { viewModel.toggleTheme() },
                        onToggleLanguage = { viewModel.toggleLanguage() },
                        onFilterRole = { viewModel.setRoleFilter(it) }
                    )
                }
            }
        }
    }
}
}

