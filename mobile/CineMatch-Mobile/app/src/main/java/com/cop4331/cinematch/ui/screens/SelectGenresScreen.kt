package com.cop4331.cinematch.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.cop4331.cinematch.ui.components.GenreButton
import com.cop4331.cinematch.ui.theme.*
import com.cop4331.cinematch.viewmodel.AuthViewModel

@Composable
fun SelectGenresScreen(navController: NavController) {

    val genres = listOf(
        "Sci-Fi", "Horror", "Action", "Romance",
        "Comedy", "Drama", "Documentary",
        "Mystery", "Anime", "Fantasy"
    )
    val viewModel: AuthViewModel = viewModel()
    val selectedGenres = remember { mutableStateListOf<String>() }

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
                text = "Step 2 of 3",
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
                    append("favorites.")
                }
            },
            color = Color.White,
            fontSize = 30.sp,
            fontFamily = FontFamily.Serif,
            lineHeight = 34.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Pick your genres. We'll tailor your picks to match your taste",
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
            items(genres) { genre ->

                val isSelected = selectedGenres.contains(genre)

                GenreButton(
                    text = genre,
                    isSelected = isSelected,
                    onClick = {
                        if (isSelected) {
                            selectedGenres.remove(genre)
                        } else {
                            selectedGenres.add(genre)
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
                        append("${selectedGenres.size}")
                    }
                    append(" selected")
                },
                color = LightGray,
                fontSize = 14.sp
            )

            Row(verticalAlignment = Alignment.CenterVertically) {

                Text(
                    text = "← Back",
                    color = LightGray,
                    modifier = Modifier.clickable {
                        navController.navigate("services")
                    }
                )

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = {
                        // 🔥 This is your array ready for backend
                        val selectedArray = selectedGenres.toList()

                        // TODO: pass to next step / backend
                        viewModel.saveGenres(
                            selectedArray,
                            onComplete = {
                                //navController.navigate("home") // or next screen
                            },
                            onError = {
                                Log.e("GENRES", it)
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