package com.example.data.auth

interface AuthDataSource {
    suspend fun login(usernameOrEmail: String, password: String, device: String? = null): LoginResponse?
    suspend fun register(req: RegisterRequest): LoginResponse?
    suspend fun refresh(refreshToken: String): String?
    suspend fun logout(refreshToken: String)
    suspend fun revokeAll()
    suspend fun me(): Map<String, Any?>?
    suspend fun oauthGoogle(idToken: String, inviteCode: String? = null, device: String? = null): LoginResponse?
    suspend fun oauthFacebook(accessToken: String, inviteCode: String? = null, device: String? = null): LoginResponse?
}

