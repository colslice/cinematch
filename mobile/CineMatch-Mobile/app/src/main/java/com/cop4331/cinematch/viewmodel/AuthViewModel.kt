package com.cop4331.cinematch.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cop4331.cinematch.network.*
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    fun signup(
        firstName: String,
        lastName: String,
        login: String,
        password: String,
        onSuccess: (UserResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.signup(
                    SignupRequest(firstName, lastName, login, password)
                )

                if (response.isSuccessful) {
                    response.body()?.let { onSuccess(it) }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("SIGNUP_ERROR", errorBody ?: "Unknown error")
                    onError(errorBody ?: "Signup failed")
                }

            } catch (e: Exception) {
                onError("Network error: ${e.message}")
            }
        }
    }

    fun login(
        login: String,
        password: String,
        onSuccess: (UserResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.login(
                    LoginRequest(login, password)
                )

                if (response.isSuccessful) {
                    response.body()?.let { onSuccess(it) }
                } else {
                    onError("Invalid login or password")
                }

            } catch (e: Exception) {
                onError("Network error: ${e.message}")
            }
        }
    }
    fun saveServices(
        services: List<String>,
        onComplete: () -> Unit,
        onError: (String) -> Unit
    ) {
        val userId = UserSession.userId ?: return

        viewModelScope.launch {
            try {
                for (service in services) {
                    RetrofitInstance.api.addService(
                        userId,
                        mapOf("service" to service)
                    )
                }
                onComplete()
            } catch (e: Exception) {
                onError(e.message ?: "Error saving services")
            }
        }
    }

    fun saveGenres(
        genres: List<String>,
        onComplete: () -> Unit,
        onError: (String) -> Unit
    ) {
        val userId = UserSession.userId ?: return

        viewModelScope.launch {
            try {
                for (genre in genres) {
                    RetrofitInstance.api.addGenre(
                        userId,
                        mapOf("genre" to genre)
                    )
                }
                onComplete()
            } catch (e: Exception) {
                onError(e.message ?: "Error saving genres")
            }
        }
    }
}