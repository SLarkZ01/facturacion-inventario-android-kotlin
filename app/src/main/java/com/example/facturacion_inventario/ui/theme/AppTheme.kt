package com.example.facturacion_inventario.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.material.Typography
import androidx.compose.material.Shapes
import androidx.compose.foundation.shape.RoundedCornerShape

private val LightColorPalette = lightColors(
    primary = Accent,
    primaryVariant = PrimaryVariant,
    secondary = SecondaryColor,
    background = BackgroundTop,
    surface = CardColor,
    onPrimary = OnPrimary,
    onSecondary = OnSecondary,
    onBackground = OnBackground,
    onSurface = OnSurface
)

private val DarkColorPalette = darkColors(
    primary = Accent,
    primaryVariant = PrimaryVariant,
    secondary = SecondaryColor
)

// Shapes usando tokens centralizados
private val AppShapes = Shapes(
    small = RoundedCornerShape(Dimens.cornerSmall),
    medium = RoundedCornerShape(Dimens.cornerMedium),
    large = RoundedCornerShape(Dimens.cornerLarge)
)

@Composable
fun AppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette
    MaterialTheme(
        colors = colors,
        typography = Typography(),
        shapes = AppShapes,
        content = content
    )
}
