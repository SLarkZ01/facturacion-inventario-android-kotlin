package com.example.facturacion_inventario.ui.store

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Versión no-op de StatusBarSync.
 * Se dejó vacía a petición del usuario para eliminar cualquier manipulación de la barra de estado.
 */
@Suppress("UNUSED_PARAMETER")
@Composable
fun StatusBarSync(headerProgress: Float, initialColor: Color = Color(0xFFFFA500)) {
    // No-op: la lógica que modificaba la barra de estado ha sido eliminada.
}

