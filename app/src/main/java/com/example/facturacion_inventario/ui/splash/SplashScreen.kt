package com.example.facturacion_inventario.ui.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.facturacion_inventario.R
import kotlinx.coroutines.delay

/**
 * Pantalla de carga con el logo de Ermotos animado.
 * Optimizado para mantener 60 FPS estables.
 */
@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit
) {
    // Duraciones de las animaciones
    val logoScaleDelay = 100L
    val logoScaleDuration = 600
    val logoFadeDuration = 400
    val textDelay = 400L
    val textDuration = 500
    val splashDuration = 2000L // Duración total antes de navegar

    // Estados de animación
    var startLogoAnimation by remember { mutableStateOf(false) }
    var startTextAnimation by remember { mutableStateOf(false) }

    // Animación de escala del logo
    val logoScale by animateFloatAsState(
        targetValue = if (startLogoAnimation) 1f else 0.3f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "logo_scale"
    )

    // Animación de fade del logo
    val logoAlpha by animateFloatAsState(
        targetValue = if (startLogoAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = logoFadeDuration,
            easing = FastOutSlowInEasing
        ),
        label = "logo_alpha"
    )

    // Animación del texto
    val textAlpha by animateFloatAsState(
        targetValue = if (startTextAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = textDuration,
            easing = LinearOutSlowInEasing
        ),
        label = "text_alpha"
    )

    val textOffset by animateFloatAsState(
        targetValue = if (startTextAnimation) 0f else 30f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "text_offset"
    )

    // Iniciar animaciones y transición
    LaunchedEffect(Unit) {
        delay(logoScaleDelay)
        startLogoAnimation = true

        delay(textDelay)
        startTextAnimation = true

        delay(splashDuration)
        onSplashComplete()
    }

    // Fondo naranja oscuro de Ermotos
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE65100)), // Naranja oscuro de la marca
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo de Ermotos animado
            Image(
                painter = painterResource(id = R.drawable.ermotoshd),
                contentDescription = "Logo Ermotos",
                modifier = Modifier
                    .size(180.dp)
                    .scale(logoScale)
                    .alpha(logoAlpha)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Nombre de la marca
            Text(
                text = "ERMOTOS",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 4.sp,
                modifier = Modifier
                    .alpha(textAlpha)
                    .offset(y = textOffset.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Venta de Repuestos, Lubricantes y Accesorios",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White.copy(alpha = 0.9f),
                modifier = Modifier
                    .alpha(textAlpha)
                    .offset(y = textOffset.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Loading indicator minimalista
            LoadingDots(
                modifier = Modifier.alpha(textAlpha)
            )
        }
    }
}

/**
 * Loading dots animados
 */
@Composable
private fun LoadingDots(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(3) { index ->
            val infiniteTransition = rememberInfiniteTransition(label = "dot_$index")
            val scale by infiniteTransition.animateFloat(
                initialValue = 0.5f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 600,
                        delayMillis = index * 100,
                        easing = FastOutSlowInEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "dot_scale_$index"
            )

            Box(
                modifier = Modifier
                    .size(8.dp)
                    .scale(scale)
                    .background(
                        color = Color.White.copy(alpha = 0.8f),
                        shape = MaterialTheme.shapes.small
                    )
            )
        }
    }
}
