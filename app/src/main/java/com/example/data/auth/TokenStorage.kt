package com.example.data.auth

import android.content.Context

object TokenStorage {
    private const val PREF = "auth"
    private const val KEY_ACCESS = "accessToken"
    private const val KEY_REFRESH = "refreshToken"
    private const val KEY_USERNAME = "username"
    private const val KEY_NOMBRE = "nombre"
    private const val KEY_APELLIDO = "apellido"

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

    // Nuevo: almacenar información básica del usuario
    fun setUserInfo(context: Context, username: String?, nombre: String?, apellido: String?) {
        val prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE).edit()
        prefs.putString(KEY_USERNAME, username)
        prefs.putString(KEY_NOMBRE, nombre)
        prefs.putString(KEY_APELLIDO, apellido)
        prefs.apply()
    }

    fun setUserFromMap(context: Context, user: Map<String, Any?>?) {
        if (user == null) return
        val username = (user["username"] ?: user["user"] ?: user["email"])?.toString()
        val nombre = (user["nombre"] ?: user["name"] ?: user["firstName"])?.toString()
        val apellido = (user["apellido"] ?: user["lastName"] ?: user["surname"])?.toString()
        setUserInfo(context, username, nombre, apellido)
    }

    fun getUsername(context: Context): String? {
        return context.getSharedPreferences(PREF, Context.MODE_PRIVATE).getString(KEY_USERNAME, null)
    }

    fun getNombre(context: Context): String? {
        return context.getSharedPreferences(PREF, Context.MODE_PRIVATE).getString(KEY_NOMBRE, null)
    }

    fun getApellido(context: Context): String? {
        return context.getSharedPreferences(PREF, Context.MODE_PRIVATE).getString(KEY_APELLIDO, null)
    }
}

