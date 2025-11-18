package com.example.data.auth

import android.content.Context

object TokenStorage {
    private const val PREF = "auth"
    private const val KEY_ACCESS = "accessToken"
    private const val KEY_REFRESH = "refreshToken"
    private const val KEY_USER_ID = "userId"
    private const val KEY_USERNAME = "username"
    private const val KEY_EMAIL = "email"
    private const val KEY_NOMBRE = "nombre"
    private const val KEY_APELLIDO = "apellido"
    private const val KEY_FECHA_CREACION = "fechaCreacion"

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

    // Almacenar información completa del usuario para snapshot de facturas
    fun setUserInfo(
        context: Context,
        userId: String?,
        username: String?,
        email: String?,
        nombre: String?,
        apellido: String?,
        fechaCreacion: String?
    ) {
        val prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE).edit()
        prefs.putString(KEY_USER_ID, userId)
        prefs.putString(KEY_USERNAME, username)
        prefs.putString(KEY_EMAIL, email)
        prefs.putString(KEY_NOMBRE, nombre)
        prefs.putString(KEY_APELLIDO, apellido)
        prefs.putString(KEY_FECHA_CREACION, fechaCreacion)
        prefs.apply()
    }

    fun setUserFromMap(context: Context, user: Map<String, Any?>?) {
        if (user == null) return

        val userId = (user["id"] ?: user["_id"])?.toString()
        val username = (user["username"] ?: user["user"])?.toString()
        val email = user["email"]?.toString()
        val nombre = (user["nombre"] ?: user["name"] ?: user["firstName"])?.toString()
        val apellido = (user["apellido"] ?: user["lastName"] ?: user["surname"])?.toString()
        val fechaCreacion = user["fechaCreacion"]?.toString()

        setUserInfo(context, userId, username, email, nombre, apellido, fechaCreacion)
    }

    fun getUserId(context: Context): String? {
        return context.getSharedPreferences(PREF, Context.MODE_PRIVATE).getString(KEY_USER_ID, null)
    }

    fun getUsername(context: Context): String? {
        return context.getSharedPreferences(PREF, Context.MODE_PRIVATE).getString(KEY_USERNAME, null)
    }

    fun getEmail(context: Context): String? {
        return context.getSharedPreferences(PREF, Context.MODE_PRIVATE).getString(KEY_EMAIL, null)
    }

    fun getNombre(context: Context): String? {
        return context.getSharedPreferences(PREF, Context.MODE_PRIVATE).getString(KEY_NOMBRE, null)
    }

    fun getApellido(context: Context): String? {
        return context.getSharedPreferences(PREF, Context.MODE_PRIVATE).getString(KEY_APELLIDO, null)
    }

    fun getFechaCreacion(context: Context): String? {
        return context.getSharedPreferences(PREF, Context.MODE_PRIVATE).getString(KEY_FECHA_CREACION, null)
    }
}

