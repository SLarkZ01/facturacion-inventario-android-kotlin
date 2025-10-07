package com.example.auth

import android.content.Context

// Wrapper ligero para mantener compatibilidad con referencias existentes a ApiClient.create(...)
object ApiClient {
    fun create(context: Context, baseUrl: String) = com.example.data.auth.ApiClient.create(context, baseUrl)
}