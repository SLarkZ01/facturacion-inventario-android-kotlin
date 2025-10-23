package com.example.facturacion_inventario.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Colores y constantes de UI compartidas
val BackgroundTop = Color(0xFFFFFFFF)
val DarkText = Color(0xFF0F1724)
val SubtleText = Color(0xFF6B7280)

// Nota: Los colores del esquema de Material3 (PrimaryColor, SecondaryColor, OnPrimary, OnSecondary,
// OnBackground, OnSurface, CardColor, PrimaryVariant, Accent) se definen en `Color.kt`.
// Evitamos duplicarlos aquí para que no haya conflictos de declaración.

// Colores de acento usados en los composables (centralizados para evitar literales)
val AccentOrange = Color(0xFFFF6F00)
val AmazonYellow = Color(0xFFFFC107)
val SuccessGreen = Color(0xFF388E3C)

// Dimensiones y espaciados reutilizables
object Dimens {
    val xs = 4.dp
    val s = 8.dp
    val md = 12.dp
    val lg = 16.dp
    val xl = 24.dp
    val xxl = 32.dp

    val buttonHeight = 48.dp
    val iconButtonSize = 40.dp
    val iconSize = 24.dp
    val imageSmall = 40.dp
    val imageMedium = 56.dp
    val imageLarge = 80.dp
    val quantityBoxWidth = 50.dp
    val pagerHeight = 280.dp

    val cornerSmall = 8.dp
    val cornerMedium = 12.dp
    val cornerLarge = 16.dp

    // Tokens específicos para tarjetas compactas
    val cardCompactWidth = 160.dp
    val cardImageHeight = 110.dp
    val favoriteBadgeSize = 36.dp
}
