package com.cop4331.cinematch.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

// 🔐 REQUESTS
data class SignupRequest(
    val FirstName: String,
    val LastName: String,
    val Login: String,
    val Password: String
)

data class LoginRequest(
    val Login: String,
    val Password: String
)

// 📦 RESPONSE
data class UserResponse(
    val _id: String,
    val FirstName: String,
    val LastName: String,
    val Login: String,
    val Services: List<String>,
    val FavGenre: List<String>,
    val NewUser: Int
)

// ❌ ERROR RESPONSE
data class ErrorResponse(
    val error: String
)

interface ApiService {

    @POST("api/auth/signup")
    suspend fun signup(
        @Body request: SignupRequest
    ): Response<UserResponse>

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<UserResponse>
    @POST("api/users/{id}/services")

    suspend fun addService(
        @Path("id") userId: String,
        @Body body: Map<String, String>
    ): Response<List<String>>

    @POST("api/users/{id}/genres")
    suspend fun addGenre(
        @Path("id") userId: String,
        @Body body: Map<String, String>
    ): Response<List<String>>
}