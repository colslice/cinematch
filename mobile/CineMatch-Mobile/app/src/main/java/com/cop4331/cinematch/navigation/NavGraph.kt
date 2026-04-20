package com.cop4331.cinematch.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.cop4331.cinematch.ui.screens.*
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("services") { SelectServicesScreen(navController) }
        composable("genres") { SelectGenresScreen(navController) }
        composable("verify/{email}") { backStackEntry ->
            val email = URLDecoder.decode(
                backStackEntry.arguments?.getString("email") ?: "",
                StandardCharsets.UTF_8.toString()
            )
            EmailVerificationScreen(navController, email)
        }    }

}