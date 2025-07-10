package com.aniruddha81.mediprompt.pages.aiAsstPage

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aniruddha81.mediprompt.models.MessageModel

@Composable
fun MessageRow(messageModel: MessageModel) {
    val isAssistant: Boolean = (messageModel.role == "model")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (isAssistant) Arrangement.Start else Arrangement.End,
        verticalAlignment = Alignment.Top
    ) {
        // Show assistant avatar on the left for assistant messages
        if (isAssistant) {
            AvatarIcon(isAssistant = true)
            Spacer(modifier = Modifier.width(8.dp))
        }

        // Message bubble with text
        Surface(
            shape = RoundedCornerShape(
                topStart = if (isAssistant) 4.dp else 16.dp,
                topEnd = if (isAssistant) 16.dp else 4.dp,
                bottomStart = 16.dp,
                bottomEnd = 16.dp
            ),
            color = if (isAssistant)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.secondaryContainer,
            shadowElevation = 1.dp,
            modifier = Modifier
                .animateContentSize()
                .weight(0.85f, fill = false)
        ) {
            SelectionContainer {
                Text(
                    text = messageModel.message,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = if (isAssistant)
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else
                        MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        // Show user avatar on the right for user messages
        if (!isAssistant) {
            Spacer(modifier = Modifier.width(8.dp))
            AvatarIcon(isAssistant = false)
        }
    }
}

@Composable
private fun AvatarIcon(isAssistant: Boolean) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(
                if (isAssistant)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.secondary
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isAssistant) Icons.Default.SmartToy else Icons.Default.Person,
            contentDescription = if (isAssistant) "Assistant" else "User",
            tint = if (isAssistant)
                MaterialTheme.colorScheme.onPrimary
            else
                MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier.size(18.dp)
        )
    }
}