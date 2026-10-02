package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.StatusError
import com.example.ui.theme.StatusSuccess

@Composable
fun GateBadge(
    label: String,
    passed: Boolean,
    modifier: Modifier = Modifier
) {
    val bgColor = if (passed) StatusSuccess.copy(alpha = 0.15f) else StatusError.copy(alpha = 0.15f)
    val textColor = if (passed) StatusSuccess else StatusError
    val icon = if (passed) Icons.Default.Check else Icons.Default.Close

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .padding(horizontal = 6.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = textColor,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
