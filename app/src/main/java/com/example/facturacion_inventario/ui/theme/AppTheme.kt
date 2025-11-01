package com.example.facturacion_inventario.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.Shapes

// Shapes usando tokens centralizados
private val AppShapes = Shapes(
    small = RoundedCornerShape(Dimens.cornerSmall),
    medium = RoundedCornerShape(Dimens.cornerMedium),
    large = RoundedCornerShape(Dimens.cornerLarge)
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    // Siempre usar LightColors, sin importar el tema del sistema
    MaterialTheme(
        colors = LightColors,
        typography = Typography(),
        shapes = AppShapes,
        content = content
    )
}
