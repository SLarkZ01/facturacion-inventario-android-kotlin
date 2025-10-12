package com.example.facturacion_inventario.ui.store

// Este archivo se mantiene por compatibilidad pero no se usa actualmente
// El badge del carrito está implementado directamente en StoreHost.kt

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlin.math.roundToInt

// Estado para manejar la animación "fly to cart"
data class FlyingItem(
    val startX: Float,
    val startY: Float,
    val targetX: Float,
    val targetY: Float,
    val iconRes: Int? = null
)

// Composable para la animación "fly to cart"
// Esta es una implementación de ejemplo que puede ser extendida en el futuro
@Composable
fun FlyToCartAnimation(
    flyingItem: FlyingItem?,
    onAnimationEnd: () -> Unit,
    modifier: Modifier = Modifier
) {
    flyingItem?.let { item ->
        var isAnimating by remember { mutableStateOf(true) }

        // Animación de posición
        val animationProgress by animateFloatAsState(
            targetValue = if (isAnimating) 1f else 0f,
            animationSpec = tween(
                durationMillis = 800,
                easing = FastOutSlowInEasing
            ),
            finishedListener = {
                isAnimating = false
                onAnimationEnd()
            }, label = "fly_to_cart"
        )

        LaunchedEffect(flyingItem) {
            isAnimating = true
        }

        // Calcular la posición interpolada
        val currentX = item.startX + (item.targetX - item.startX) * animationProgress
        val currentY = item.startY + (item.targetY - item.startY) * animationProgress

        // Animación de escala (se hace más pequeño mientras vuela)
        val scale = 1f - (animationProgress * 0.7f)

        // Animación de opacidad
        val alpha = 1f - (animationProgress * 0.3f)

        Box(
            modifier = modifier
                .fillMaxSize()
                .zIndex(1000f)
        ) {
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            currentX.roundToInt(),
                            currentY.roundToInt()
                        )
                    }
                    .size(60.dp)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        this.alpha = alpha
                    }
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.primary.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                // Aquí se mostraría el icono del producto volando hacia el carrito
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
