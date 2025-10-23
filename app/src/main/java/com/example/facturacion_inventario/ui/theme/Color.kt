package com.example.facturacion_inventario.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors

// Paleta básica (colores reutilizables)
val PrimaryColor = Color(0xFFFF9900) // Naranja tipo Amazon
val SecondaryColor = Color(0xFF232F3E)
val BackgroundLight = Color(0xFFF7F7F7)
val BackgroundDark = Color(0xFF0F1111)
val Accent = Color(0xFF00A896)
val PrimaryVariant = Color(0xFFF57C00)
val CardColor = Color(0xFFFFFFFF)
val OnPrimary = Color(0xFFFFFFFF)
val OnSecondary = Color(0xFFFFFFFF)
val OnBackground = Color(0xFF111111)
val OnSurface = Color(0xFF111111)

// Light / Dark Colors para Material (androidx.compose.material)
val LightColors = lightColors(
    primary = PrimaryColor,
    primaryVariant = PrimaryVariant,
    secondary = SecondaryColor,
    background = BackgroundLight,
    surface = CardColor,
    onPrimary = OnPrimary,
    onSecondary = OnSecondary,
    onBackground = OnBackground,
    onSurface = OnSurface
)

val DarkColors = darkColors(
    primary = PrimaryColor,
    primaryVariant = PrimaryVariant,
    secondary = SecondaryColor,
    background = BackgroundDark,
    surface = Color(0xFF121212),
    onPrimary = OnPrimary,
    onSecondary = OnSecondary,
    onBackground = Color(0xFFECECEC),
    onSurface = Color(0xFFECECEC)
)
