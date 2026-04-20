package com.cop4331.cinematch.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cop4331.cinematch.ui.theme.Orange

@Composable
fun MarqueeBar() {

    val text = " CineMatch • 10,000+ movies • Netflix • Hulu • Apple TV • 5+ more • Find your next favorite • "

    val infiniteTransition = rememberInfiniteTransition(label = "")

    val translateX by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Orange)
            .height(40.dp)
    ) {
        Row(
            modifier = Modifier
                .offset(x = translateX.dp)
        ) {
            Text(
                text = text.repeat(10), // 👈 KEY FIX (infinite illusion)
                color = Color.Black,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}