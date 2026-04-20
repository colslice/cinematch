package com.cop4331.cinematch.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cop4331.cinematch.ui.components.*
import com.cop4331.cinematch.ui.theme.*
import com.cop4331.cinematch.viewmodel.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cop4331.cinematch.network.UserSession

@Composable
fun LoginScreen(navController: NavController) {

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val viewModel: AuthViewModel = viewModel()

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
            fontSize = 14.sp
        )

        // 🎯 FORM
        Column {
            Text(
                text = "YOUR DETAILS",
                color = Orange,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Good to see you again.",
                fontFamily = FontFamily.Serif,
                color = Color.White,
                fontSize = 28.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            CustomTextField(
                label = "Email",
                value = login,
                onChange = { login = it },
                placeholder = "e.g. johndoe@gmail.com"
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                label = "Password",
                value = password,
                onChange = { password = it },
                placeholder = "Enter your password",
                isPassword = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton("Sign in →") {
                viewModel.login(
                    login,
                    password,
                    onSuccess = { user ->
                        UserSession.userId = user._id
                        if (user.NewUser == 1) {
                            navController.navigate("services") // onboarding
                        } else {
                            navController.navigate("home") // later
                        }
                    },
                    onError = { error ->
                        println(error)
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Don't have an account? Sign up",
                color = LightGray,
                modifier = Modifier.clickable {
                    navController.navigate("register")
                }
            )
        }

        // 🔁 MARQUEE
        MarqueeBar()
    }
}