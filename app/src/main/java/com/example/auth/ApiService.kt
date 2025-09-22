package com.example.auth

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response


interface ApiService {
    @POST("/api/auth/register")
    suspend fun register(@Body req: RegisterRequest): Response<LoginResponse>

    @POST("/api/auth/login")
    suspend fun login(@Body req: LoginRequest): Response<LoginResponse>

    @POST("/api/auth/login")
    suspend fun loginMap(@Body body: Map<String, String>): Response<LoginResponse>

    @POST("/api/auth/refresh")
    suspend fun refresh(@Body body: Map<String, String>): Response<RefreshResponse>

    @POST("/api/auth/logout")
    suspend fun logout(@Body body: Map<String, String>): Response<Unit>

    // OAuth endpoints (según docs/openapi.yaml)
    @POST("/api/auth/oauth/google")
    suspend fun oauthGoogle(@Body body: Map<String, String>): Response<LoginResponse>

    @POST("/api/auth/oauth/facebook")
    suspend fun oauthFacebook(@Body body: Map<String, String>): Response<LoginResponse>
}
