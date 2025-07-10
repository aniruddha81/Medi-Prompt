package com.aniruddha81.mediprompt.pages.alarmPage

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.HapticFeedbackConstantsCompat
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.drop
import kotlin.math.abs
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDeleteContainer(
    modifier: Modifier = Modifier,
    onDelete: () -> Unit,
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    val density = LocalDensity.current
    val alignment = Alignment.CenterEnd
    var isRemoved by remember { mutableStateOf(false) }
    val threshold by remember { mutableStateOf(150.dp) }
    var offset by remember { mutableFloatStateOf(0f) }

    val swipeState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                isRemoved = true
            }
            true
        },
        positionalThreshold = with(density) {
            { threshold.toPx() }
        }
    )

    // Calculate progress for animation effects
    val swipeProgress = remember(offset) {
        val maxOffset = with(density) { threshold.toPx() }
        (min(abs(offset) / maxOffset, 1f)).coerceIn(0f, 1f)
    }

    val offsetMatch by remember(offset) {
        derivedStateOf {
            mutableStateOf(
                abs(offset) >= with(density) {
                    { threshold.toPx() }
                }.invoke()
            ).value
        }
    }

    // Animate the background color
    val bgColor by androidx.compose.animation.animateColorAsState(
        targetValue = if (swipeState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
            MaterialTheme.colorScheme.errorContainer.copy(alpha = swipeProgress * 0.9f)
        } else {
            MaterialTheme.colorScheme.surfaceContainer
        },
        animationSpec = tween(durationMillis = 300),
        label = "backgroundColor"
    )

    // Animate the delete icon
    val iconScale by animateFloatAsState(
        targetValue = if (swipeProgress > 0.2f) 1f else 0.5f,
        animationSpec = SpringSpec(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "iconScale"
    )

    val iconRotation by animateFloatAsState(
        targetValue = swipeProgress * 30f, // rotate up to 30 degrees
        animationSpec = tween(durationMillis = 300),
        label = "iconRotation"
    )

    LaunchedEffect(key1 = isRemoved) {
        if (isRemoved) {
            delay(250.toLong())
            onDelete()
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { offsetMatch }
            .drop(1)
            .collect {
                view.performHapticFeedback(
                    HapticFeedbackConstantsCompat.CLOCK_TICK
                )
            }
    }

    SwipeToDismissBox(
        modifier = modifier
            .onSizeChanged { offset = swipeState.requireOffset() },
        enableDismissFromStartToEnd = false,
        state = swipeState,
        backgroundContent = {
            // Custom animated background with delete icon
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(bgColor),
                contentAlignment = alignment
            ) {
                // Only show the delete icon when swiping from end to start
                if (swipeState.dismissDirection == SwipeToDismissBoxValue.EndToStart && swipeProgress > 0) {
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(40.dp)
                            .scale(iconScale)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.error.copy(alpha = swipeProgress))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete",
                            modifier = Modifier
                                .size(24.dp)
                                .rotate(iconRotation),
                            tint = Color.White
                        )
                    }
                }
            }
        },
    ) {
        // Apply a subtle scale animation to the content while swiping
        val contentScale = 1f - (0.05f * swipeProgress)

        Box(
            modifier = Modifier
                .scale(contentScale)
                .fillMaxSize()
        ) {
            content()
        }
    }
}