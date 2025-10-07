package com.example.facturacion_inventario.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.material.Typography
import androidx.compose.material.Shapes

private val LightColorPalette = lightColors(
    primary = com.example.facturacion_inventario.ui.theme.Accent,
    primaryVariant = com.example.facturacion_inventario.ui.theme.PrimaryVariant,
    secondary = com.example.facturacion_inventario.ui.theme.SecondaryColor,
    background = com.example.facturacion_inventario.ui.theme.BackgroundTop,
    surface = com.example.facturacion_inventario.ui.theme.CardColor,
    onPrimary = com.example.facturacion_inventario.ui.theme.OnPrimary,
    onSecondary = com.example.facturacion_inventario.ui.theme.OnSecondary,
    onBackground = com.example.facturacion_inventario.ui.theme.OnBackground,
    onSurface = com.example.facturacion_inventario.ui.theme.OnSurface
)

private val DarkColorPalette = darkColors(
    primary = com.example.facturacion_inventario.ui.theme.Accent,
    primaryVariant = com.example.facturacion_inventario.ui.theme.PrimaryVariant,
    secondary = com.example.facturacion_inventario.ui.theme.SecondaryColor
)

@Composable
fun AppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette
    MaterialTheme(
        colors = colors,
        typography = Typography(),
        shapes = Shapes(),
        content = content
    )
}
