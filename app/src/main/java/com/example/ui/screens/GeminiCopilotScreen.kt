package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.ChatMessage
import com.example.data.model.MessageSender
import com.example.ui.theme.StatusInfo
import com.example.ui.theme.StatusPurple
import com.example.ui.theme.StatusSuccess
import com.example.ui.viewmodel.AppLanguage
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.MainViewModel

@Composable
fun GeminiCopilotScreen(
    viewModel: MainViewModel,
    onNavigate: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val language by viewModel.language.collectAsState()
    val chatMessages by viewModel.chatMessages.collectAsState()
    val selectedModel by viewModel.selectedGeminiModel.collectAsState()
    val selectedImageSize by viewModel.selectedImageSize.collectAsState()
    val isLoading by viewModel.isGeminiLoading.collectAsState()
    val isLiveVoiceActive by viewModel.isLiveVoiceActive.collectAsState()

    var inputPrompt by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(chatMessages.size) {
        if (chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    val modelOptions = listOf(
        "gemini-3.1-pro-preview" to "High Thinking (gemini-3.1-pro)",
        "gemini-3.1-flash-lite" to "Low-Latency (flash-lite)",
        "gemini-3.5-flash-search" to "Search Grounding (3.5-flash)",
        "gemini-3.5-flash-maps" to "Maps Grounding (3.5-flash)",
        "gemini-3-pro-image-preview" to "Generate Image (3-pro-image)",
        "gemini-3.8-flash-tts" to "TTS Voice (3.8-flash-tts)",
        "gemini-3.8-live" to "Live Voice (3.8-live)"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Model Selector Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(modelOptions) { (id, label) ->
                FilterChip(
                    selected = selectedModel == id,
                    onClick = { viewModel.selectedGeminiModel.value = id },
                    label = { Text(label, fontSize = 12.sp) },
                    shape = RoundedCornerShape(8.dp)
                )
            }
        }

        // Image size selector if image generation model is active
        if (selectedModel == "gemini-3-pro-image-preview") {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Resolution:",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                listOf("1K", "2K", "4K").forEach { size ->
                    FilterChip(
                        selected = selectedImageSize == size,
                        onClick = { viewModel.selectedImageSize.value = size },
                        label = { Text(size, fontSize = 11.sp) },
                        shape = RoundedCornerShape(6.dp)
                    )
                }
            }
        }

        // Live Voice Active Status Bar
        if (isLiveVoiceActive || selectedModel == "gemini-3.8-live") {
            Spacer(modifier = Modifier.height(6.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = StatusPurple.copy(alpha = 0.2f)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = StatusPurple,
                            modifier = Modifier.size(10.dp)
                        ) {}
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "gemini-3.8-live Live Voice Session Active",
                            style = MaterialTheme.typography.labelSmall,
                            color = StatusPurple
                        )
                    }
                    TextButton(onClick = { viewModel.toggleLiveVoice() }) {
                        Text(if (isLiveVoiceActive) "End Call" else "Start Live")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Message Thread
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(chatMessages) { msg ->
                ChatMessageItem(message = msg)
            }

            if (isLoading) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = when (selectedModel) {
                                "gemini-3.1-pro-preview" -> "gemini-3.1-pro-preview reasoning with ThinkingLevel.HIGH..."
                                "gemini-3.1-flash-lite" -> "gemini-3.1-flash-lite streaming low-latency response..."
                                "gemini-3.5-flash-search" -> "Grounding with Google Search..."
                                "gemini-3.5-flash-maps" -> "Grounding with Google Maps..."
                                "gemini-3-pro-image-preview" -> "Generating $selectedImageSize image card..."
                                else -> "Generating with Gemini..."
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Input Field & Action Bar
        Surface(
            tonalElevation = 3.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = inputPrompt,
                    onValueChange = { inputPrompt = it },
                    placeholder = {
                        Text(
                            when (selectedModel) {
                                "gemini-3.1-pro-preview" -> "Ask complex question (High Thinking)..."
                                "gemini-3.1-flash-lite" -> "Ask rapid question (Flash-Lite)..."
                                "gemini-3.5-flash-search" -> "Search live Canadian tech news..."
                                "gemini-3.5-flash-maps" -> "Ask about employer Canadian offices..."
                                "gemini-3-pro-image-preview" -> "Describe resume card to generate..."
                                else -> "Message Gemini Copilot..."
                            },
                            fontSize = 13.sp
                        )
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        val text = inputPrompt
                        inputPrompt = ""
                        viewModel.sendMessage(text)
                    },
                    enabled = inputPrompt.isNotBlank() && !isLoading,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            if (inputPrompt.isNotBlank()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                        )
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = if (inputPrompt.isNotBlank()) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = { viewModel.toggleLiveVoice() },
                    modifier = Modifier.size(44.dp)
                ) {
                    Icon(
                        if (isLiveVoiceActive) Icons.Default.Mic else Icons.Default.MicNone,
                        contentDescription = "Live Voice",
                        tint = if (isLiveVoiceActive) StatusPurple else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun ChatMessageItem(message: ChatMessage) {
    val isUser = message.sender == MessageSender.USER
    val isSystem = message.sender == MessageSender.SYSTEM

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 14.dp,
                topEnd = 14.dp,
                bottomStart = if (isUser) 14.dp else 2.dp,
                bottomEnd = if (isUser) 2.dp else 14.dp
            ),
            color = when {
                isUser -> MaterialTheme.colorScheme.primary
                isSystem -> MaterialTheme.colorScheme.surfaceVariant
                else -> MaterialTheme.colorScheme.surfaceVariant
            },
            tonalElevation = 2.dp,
            modifier = Modifier.widthIn(max = 320.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                if (!isUser) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = message.modelUsed,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 10.sp
                        )
                        if (message.thinkingText != null) {
                            Text(
                                text = "Thinking: HIGH",
                                style = MaterialTheme.typography.labelSmall,
                                color = StatusPurple,
                                fontSize = 9.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                    lineHeight = 18.sp
                )

                if (message.imageUrl != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    AsyncImage(
                        model = message.imageUrl,
                        contentDescription = "Generated Infographic",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
            }
        }
    }
}
