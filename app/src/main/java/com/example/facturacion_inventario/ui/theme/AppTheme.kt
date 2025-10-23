package com.example.facturacion_inventario.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
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
fun AppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colors = colors,
        typography = Typography(),
        shapes = AppShapes,
        content = content
    )
}
