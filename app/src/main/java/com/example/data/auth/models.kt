package com.example.data.auth

import com.google.gson.annotations.SerializedName

// Requests
data class RegisterRequest(
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("inviteCode") val inviteCode: String? = null,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("apellido") val apellido: String
)

data class LoginRequest(
    @SerializedName("usernameOrEmail") val usernameOrEmail: String,
    @SerializedName("password") val password: String,
    @SerializedName("device") val device: String? = null
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
