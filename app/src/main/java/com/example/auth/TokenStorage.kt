package com.example.auth

import android.content.Context

object TokenStorage {
    private const val PREF = "auth"
    private const val KEY_ACCESS = "accessToken"
    private const val KEY_REFRESH = "refreshToken"

    fun getAccessToken(context: Context): String? {
        return context.getSharedPreferences(PREF, Context.MODE_PRIVATE).getString(KEY_ACCESS, null)
    }

    fun setAccessToken(context: Context, token: String?) {
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE).edit().putString(KEY_ACCESS, token).apply()
    }

    fun getRefreshToken(context: Context): String? {
        return context.getSharedPreferences(PREF, Context.MODE_PRIVATE).getString(KEY_REFRESH, null)
    }

    fun setRefreshToken(context: Context, token: String?) {
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE).edit().putString(KEY_REFRESH, token).apply()
    }

    fun clear(context: Context) {
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE).edit().clear().apply()
    }
}

