package com.example.facturacion_inventario.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

/**
 * Gestor de carritos que maneja el carrito actual del usuario
 * Almacena el ID del carrito activo en SharedPreferences
 */
class CarritoManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val TAG = "CarritoManager"

    companion object {
        private const val PREFS_NAME = "carrito_prefs"
        private const val KEY_CARRITO_ACTUAL = "carrito_actual_id"
        private const val KEY_USUARIO_ID = "usuario_id"
    }

    /**
     * Guarda el ID del carrito actual del usuario
     */
    fun setCarritoActual(carritoId: String, usuarioId: String) {
        Log.d(TAG, "Setting carrito actual: $carritoId for user: $usuarioId")
        prefs.edit().apply {
            putString(KEY_CARRITO_ACTUAL, carritoId)
            putString(KEY_USUARIO_ID, usuarioId)
            apply()
        }
    }

    /**
     * Obtiene el ID del carrito actual del usuario
     */
    fun getCarritoActual(): String? {
        val carritoId = prefs.getString(KEY_CARRITO_ACTUAL, null)
        Log.d(TAG, "Getting carrito actual: $carritoId")
        return carritoId
    }

    /**
     * Obtiene el ID del usuario del carrito actual
     */
    fun getUsuarioId(): String? {
        return prefs.getString(KEY_USUARIO_ID, null)
    }

    /**
     * Limpia el carrito actual (útil al cerrar sesión)
     */
    fun clearCarrito() {
        Log.d(TAG, "Clearing carrito actual")
        prefs.edit().clear().apply()
    }

    /**
     * Verifica si hay un carrito activo
     */
    fun hasCarritoActivo(): Boolean {
        return getCarritoActual() != null
    }
}

