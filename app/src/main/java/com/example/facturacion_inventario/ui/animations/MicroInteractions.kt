package com.example.facturacion_inventario.ui.animations

import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput

/**
 * Microinteracciones profesionales para una UX pulida.
 * Optimizadas para rendimiento sin afectar los 60 FPS.
 */

/**
 * Efecto de bounce al presionar (como iOS)
 */
fun Modifier.bounceClick(
    minScale: Float = 0.92f,
    onTap: () -> Unit = {}
): Modifier = composed {
    val scale = remember { Animatable(1f) }

    this
        .graphicsLayer {
            scaleX = scale.value
            scaleY = scale.value
        }
        .pointerInput(Unit) {
            detectTapGestures(
                onPress = {
                    scale.animateTo(
                        minScale,
                        animationSpec = tween(100, easing = FastOutSlowInEasing)
                    )
                    tryAwaitRelease()
                    scale.animateTo(
                        1f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    )
                },
                onTap = { onTap() }
            )
        }
}

/**
 * Efecto de presionado sutil (recomendado para cards y elementos grandes)
 */
fun Modifier.pressEffect(interactionSource: MutableInteractionSource): Modifier = composed {
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "press_scale"
    )

    this.scale(scale)
}

/**
 * Shimmer effect para estados de carga
 */
@Composable
fun rememberShimmerAnimation(): Float {
    val transition = rememberInfiniteTransition(label = "shimmer")
    return transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_offset"
    ).value
}

/**
 * Pulse animation para elementos que necesitan atención
 */
@Composable
fun rememberPulseAnimation(enabled: Boolean = true): Float {
    val transition = rememberInfiniteTransition(label = "pulse")

    return if (enabled) {
        transition.animateFloat(
            initialValue = 1f,
            targetValue = 1.1f,
            animationSpec = infiniteRepeatable(
                animation = tween(800, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse_scale"
        ).value
    } else {
        1f
    }
}

/**
 * Fade in/out suave al aparecer contenido
 */
@Composable
fun rememberFadeInAnimation(): State<Float> {
    return animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing
        ),
        label = "fade_in"
    )
}

/**
 * Rotation animation continua (para loaders)
 */
@Composable
fun rememberRotationAnimation(): State<Float> {
    val transition = rememberInfiniteTransition(label = "rotation")
    return transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation_degrees"
    )
}

/**
 * Slide up animation suave para elementos que aparecen
 */
@Composable
fun rememberSlideUpAnimation(): State<Float> {
    return animateFloatAsState(
        targetValue = 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "slide_up"
    )
}

/**
 * Loading dots animation (tres puntos)
 */
@Composable
fun rememberLoadingDotsAnimation(index: Int): State<Float> {
    val infiniteTransition = rememberInfiniteTransition(label = "loading_dots")
    return infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 600,
                delayMillis = index * 100,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot_$index"
    )
}
