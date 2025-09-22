package com.example.auth

import com.google.gson.annotations.SerializedName

// Requests
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val inviteCode: String? = null,
    val nombre: String?,
    val apellido: String?,
    val rol: String?
)

data class LoginRequest(
    val usernameOrEmail: String,
    val password: String,
    val device: String? = null
)

// Responses
data class LoginResponse(
    @SerializedName("accessToken") val accessToken: String?,
    @SerializedName("access_token") val access_token: String?,
    @SerializedName("refreshToken") val refreshToken: String?,
    @SerializedName("refresh_token") val refresh_token: String?,
    val user: Map<String, Any?>?
) {
    val accessTokenNormalized: String?
        get() = accessToken ?: access_token

    val refreshTokenNormalized: String?
        get() = refreshToken ?: refresh_token
}

data class RefreshResponse(
    @SerializedName("accessToken") val accessToken: String?,
    @SerializedName("access_token") val access_token: String?
) {
    val accessTokenNormalized: String?
        get() = accessToken ?: access_token
}
