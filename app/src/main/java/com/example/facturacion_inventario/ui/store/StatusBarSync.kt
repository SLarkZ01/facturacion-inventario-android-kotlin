package com.example.facturacion_inventario.ui.store

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * ⚠️ COMPOSABLE NO-OP
 *
 * PROPÓSITO ORIGINAL:
 * Sincronizar el color de la barra de estado del sistema Android con el progreso
 * del header colapsable de StoreHost, creando una transición visual fluida donde
 * la barra de estado cambia de color naranja a blanco según el scroll.
 *
 * ESTADO ACTUAL:
 * Deshabilitada completamente a petición del usuario. No ejecuta ninguna lógica
 * ni modifica la barra de estado del sistema.
 *
 * RAZÓN DE MANTENERLA:
 * - Compatibilidad con código existente que la invoca desde StoreHost.kt
 * - Evitar breaking changes y errores de compilación
 * - Permite reactivar la funcionalidad fácilmente si se requiere en el futuro
 *
 * CONDICIONES PARA ELIMINACIÓN:
 * - Cuando se refactorice StoreHost.kt y se eliminen todas las invocaciones
 * - Después de confirmar que no se necesita en ninguna otra pantalla de la app
 * - En la próxima versión mayor donde se permitan breaking changes (v2.0+)
 * - Si se decide permanentemente no sincronizar la barra de estado
 *
 * IMPLEMENTACIÓN FUTURA (si se reactiva):
 * Debería usar SideEffect con WindowInsetsController para cambiar el color
 * de la barra de estado interpolando entre initialColor y Color.White según
 * el valor de headerProgress (0f = expandido, 1f = colapsado).
 *
 * @param headerProgress Progreso de colapso del header (0f = expandido, 1f = colapsado) - NO USADO
 * @param initialColor Color inicial de la barra de estado (naranja por defecto) - NO USADO
 * @see StoreHost donde se invoca esta función
 * @since v1.0
 */
@Suppress("UNUSED_PARAMETER")
@Composable
fun StatusBarSync(headerProgress: Float, initialColor: Color = Color(0xFFFFA500)) {
    // No-op: la lógica de modificación de barra de estado ha sido eliminada intencionalmente
}
