package com.cop4331.cinematch.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cop4331.cinematch.ui.theme.*
@Composable
fun PrimaryButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Orange
        ),
        shape = RoundedCornerShape(30.dp),
        modifier = Modifier
            .fillMaxWidth()   // 👈 THIS FIXES WIDTH
            .height(55.dp)
    ) {
        Text(text = text, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
    }
}