package com.cop4331.cinematch.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.cop4331.cinematch.ui.components.ServiceButton
import com.cop4331.cinematch.ui.theme.*
import com.cop4331.cinematch.viewmodel.AuthViewModel

@Composable
fun SelectServicesScreen(navController: NavController) {

    val services = listOf(
        "Netflix", "Hulu", "Apple TV", "Disney+",
        "HBO Max", "Amazon Prime", "Paramount+",
        "Peacock", "Crunchyroll"
    )
    val viewModel: AuthViewModel = viewModel()
    val selectedServices = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Dark)
            .padding(24.dp)
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 🔝 HEADER
            Text(
                text = "CINEMATCH",
                color = LightGray,
                fontSize = 14.sp
            )

            Text(
                text = "Step 1 of 3",
                color = LightGray,
                fontSize = 12.sp
            )

        }
        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(Modifier, DividerDefaults.Thickness, color = LightGray)

        Spacer(modifier = Modifier.height(24.dp))

        // 🎯 TITLE
        Text(
            text = "SELECT ALL THAT APPLY",
            color = Orange,
            fontSize = 12.sp,
            fontFamily = FontFamily.SansSerif
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = buildAnnotatedString {
                append("Pick your\n")
                withStyle(
                    SpanStyle(color = Orange)
                ) {
                    append("services.")
                }
            },
            color = Color.White,
            fontSize = 30.sp,
            fontFamily = FontFamily.Serif,
            lineHeight = 34.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "we'll only show you movies you can actually watch",
            color = LightGray,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 🔲 GRID
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(services) { service ->

                val isSelected = selectedServices.contains(service)

                ServiceButton(
                    text = service,
                    isSelected = isSelected,
                    onClick = {
                        if (isSelected) {
                            selectedServices.remove(service)
                        } else {
                            selectedServices.add(service)
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔻 FOOTER
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Selected Count
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = Orange)) {
                        append("${selectedServices.size}")
                    }
                    append(" selected")
                },
                color = LightGray,
                fontSize = 14.sp
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = {
                        // 🔥 This is your array ready for backend
                        val selectedArray = selectedServices.toList()

                        // TODO: pass to next step / backend
                        viewModel.saveServices(
                            selectedArray,
                            onComplete = {
                                navController.navigate("genres")
                            },
                            onError = {
                                Log.e("SERVICES", it)
                            }
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Orange),
                    shape = RoundedCornerShape(30.dp)
                ) {
                    Text("Continue →", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}