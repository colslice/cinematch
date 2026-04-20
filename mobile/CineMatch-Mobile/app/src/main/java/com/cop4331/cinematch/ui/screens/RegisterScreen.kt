package com.cop4331.cinematch.ui.screens

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
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cop4331.cinematch.network.UserSession


@Composable
fun RegisterScreen(navController: NavController) {

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
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
                text = "Let's get started.",
                color = Color.White,
                fontFamily = FontFamily.Serif,
                fontSize = 28.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            CustomTextField(
                label = "First Name",
                value = firstName,
                onChange = { firstName = it },
                placeholder = "e.g. John"
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                label = "Last Name",
                value = lastName,
                onChange = { lastName = it },
                placeholder = "e.g. Doe"
            )

            Spacer(modifier = Modifier.height(16.dp))

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
                placeholder = "Must be at least 8 characters",
                isPassword = true
            )

            Spacer(modifier = Modifier.height(24.dp))


            PrimaryButton("Create account →") {
                // TODO: API
                viewModel.signup(
                    firstName,
                    lastName,
                    login,
                    password,
                    onSuccess = { user ->
                        // 🔥 Navigate to verification
                        UserSession.userId = user._id
                        val encodedEmail = URLEncoder.encode(login, StandardCharsets.UTF_8.toString())
                        navController.navigate("verify/${user.Login}")
                    },
                    onError = { error ->
                        println(error)
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Already have an account? Log in",
                color = LightGray,
                modifier = Modifier.clickable {
                    navController.navigate("login")
                }
            )
        }

        // 🔁 MARQUEE
        MarqueeBar()
    }
}