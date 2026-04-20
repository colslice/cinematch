package com.cop4331.cinematch.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cop4331.cinematch.ui.theme.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun EmailVerificationScreen(
    navController: NavController,
    email: String = "johndoe@gmail.com" // replace later with real data
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Dark)
            .padding(horizontal = 24.dp, vertical = 32.dp)
    ) {

        // 🔝 HEADER ROW
        Column {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "CINEMATCH",
                    color = LightGray,
                    fontSize = 14.sp
                )

                Text(
                    text = "← Back to sign up",
                    color = LightGray,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable {
                        navController.navigate("register")
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(Modifier, thickness = 1.dp, color = LightGray)
        }

        // 🎯 CENTER CONTENT
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 📧 ICON
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Orange.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    tint = Orange,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 🔸 ALMOST THERE
            Text(
                text = "ALMOST THERE",
                color = Orange,
                fontSize = 12.sp,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 🎬 HEADLINE (MIXED STYLE)
            Text(
                text = buildAnnotatedString {
                    append("Check your\n")

                    withStyle(
                        SpanStyle(
                            color = Orange,
                            fontStyle = FontStyle.Italic
                        )
                    ) {
                        append("inbox.")
                    }
                },
                color = Color.White,
                fontSize = 30.sp,
                fontFamily = FontFamily.Serif,
                textAlign = TextAlign.Center,
                lineHeight = 34.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 📩 DESCRIPTION
            Text(
                text = "We sent a verification link to",
                color = LightGray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = email,
                color = Orange,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Click the link to activate your account.",
                color = LightGray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ➖ CENTERED HALF DIVIDER
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(0.5f),
                thickness = 1.dp,
                color = LightGray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 🔁 RESEND
            var canResend by remember { mutableStateOf(false) }
            var timer by remember { mutableIntStateOf(10) }

            LaunchedEffect(canResend) {
                if (!canResend) {
                    timer = 60
                    while (timer > 0) {
                        kotlinx.coroutines.delay(1000)
                        timer--
                    }
                    canResend = true
                }
            }
            Row {
                Text(
                    text = "Didn't get it? ",
                    color = LightGray,
                    fontSize = 14.sp
                )

                if (canResend) {
                    Text(
                        text = "Resend Email",
                        color = Orange,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.clickable {
                            canResend = false
                            // TODO: trigger resend API later
                        }
                    )
                } else {
                    Text(
                        text = "Resend in ${timer}s",
                        color = Orange,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}