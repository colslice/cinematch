package com.cop4331.cinematch.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cop4331.cinematch.ui.components.*
import com.cop4331.cinematch.ui.theme.*

@Composable
fun SplashScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Dark)
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        // 🔝 HEADER
        Text(
            text = "CINEMATCH",
            color = LightGray,
            fontSize = 14.sp,
            fontFamily = FontFamily.SansSerif
        )

        // 🎯 MAIN CONTENT
        Column {

            Text(
                text = "GET STARTED - IT'S FREE",
                color = Orange,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Find Your\nNext Film",
                color = Color.White,
                fontSize = 42.sp,
                fontFamily = FontFamily.Serif,
                lineHeight = 46.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Rate what you've seen. Tell us your streaming services. We do the rest.",
                color = LightGray,
                fontSize = 14.sp
            )
        }

        // 🔘 CTA BUTTONS
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            PrimaryButton(
                text = "Create free account",
                onClick = { navController.navigate("register") }
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { navController.navigate("login") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.White
                )
            ) {
                Text(text = "Log in", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }

        // 🔁 MARQUEE
        MarqueeBar()
    }
}