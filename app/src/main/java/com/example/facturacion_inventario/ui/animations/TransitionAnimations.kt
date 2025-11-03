package com.example.facturacion_inventario.ui.animations

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.ui.unit.IntOffset

/**
 * Transiciones de navegación profesionales para la app.
 * Optimizadas para 60 FPS consistentes.
 */
object NavigationTransitions {

    // Duración estándar para transiciones suaves
    private const val DURATION_MS = 300
    private const val FADE_DURATION_MS = 200

    // Easing más profesional
    private val easing = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)

    /**
     * Transición de slide horizontal (ideal para navegación forward)
     */
    fun slideInFromRight(): EnterTransition {
        return slideInHorizontally(
            initialOffsetX = { fullWidth -> fullWidth },
            animationSpec = tween(
                durationMillis = DURATION_MS,
                easing = easing
            )
        ) + fadeIn(
            animationSpec = tween(
                durationMillis = FADE_DURATION_MS,
                easing = LinearEasing
            )
        )
    }

    fun slideOutToLeft(): ExitTransition {
        return slideOutHorizontally(
            targetOffsetX = { fullWidth -> -fullWidth / 3 },
            animationSpec = tween(
                durationMillis = DURATION_MS,
                easing = easing
            )
        ) + fadeOut(
            animationSpec = tween(
                durationMillis = FADE_DURATION_MS,
                easing = LinearEasing
            )
        )
    }

    /**
     * Transición de slide horizontal invertida (ideal para back)
     */
    fun slideInFromLeft(): EnterTransition {
        return slideInHorizontally(
            initialOffsetX = { fullWidth -> -fullWidth / 3 },
            animationSpec = tween(
                durationMillis = DURATION_MS,
                easing = easing
            )
        ) + fadeIn(
            animationSpec = tween(
                durationMillis = FADE_DURATION_MS,
                easing = LinearEasing
            )
        )
    }

    fun slideOutToRight(): ExitTransition {
        return slideOutHorizontally(
            targetOffsetX = { fullWidth -> fullWidth },
            animationSpec = tween(
                durationMillis = DURATION_MS,
                easing = easing
            )
        ) + fadeOut(
            animationSpec = tween(
                durationMillis = FADE_DURATION_MS,
                easing = LinearEasing
            )
        )
    }

    /**
     * Transición vertical (ideal para bottom sheets o modales)
     */
    fun slideInFromBottom(): EnterTransition {
        return slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ) + fadeIn(animationSpec = tween(FADE_DURATION_MS))
    }

    fun slideOutToBottom(): ExitTransition {
        return slideOutVertically(
            targetOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(durationMillis = DURATION_MS, easing = easing)
        ) + fadeOut(animationSpec = tween(FADE_DURATION_MS))
    }

    /**
     * Expand vertical suave (para listas, menús)
     */
    fun expandVerticallySmooth(): EnterTransition {
        return expandVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ) + fadeIn(animationSpec = tween(FADE_DURATION_MS))
    }

    fun shrinkVerticallySmooth(): ExitTransition {
        return shrinkVertically(
            animationSpec = tween(durationMillis = DURATION_MS, easing = easing)
        ) + fadeOut(animationSpec = tween(FADE_DURATION_MS))
    }

    /**
     * Fade simple para cambios sutiles
     */
    fun fadeInOnly(): EnterTransition {
        return fadeIn(
            animationSpec = tween(
                durationMillis = DURATION_MS,
                easing = LinearEasing
            )
        )
    }

    fun fadeOutOnly(): ExitTransition {
        return fadeOut(
            animationSpec = tween(
                durationMillis = DURATION_MS,
                easing = LinearEasing
            )
        )
    }

    /**
     * Scale + Fade para efectos más dramáticos
     */
    fun scaleInWithFade(): EnterTransition {
        return scaleIn(
            initialScale = 0.8f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ) + fadeIn(animationSpec = tween(FADE_DURATION_MS))
    }

    fun scaleOutWithFade(): ExitTransition {
        return scaleOut(
            targetScale = 0.8f,
            animationSpec = tween(durationMillis = DURATION_MS, easing = easing)
        ) + fadeOut(animationSpec = tween(FADE_DURATION_MS))
    }
}

/**
 * Specs de animación reutilizables
 */
object AnimationSpecs {
    val fastSpring = spring<Float>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMedium
    )

    val mediumSpring = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )

    val smoothTween = tween<Float>(
        durationMillis = 300,
        easing = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)
    )

    val quickTween = tween<Float>(
        durationMillis = 150,
        easing = FastOutSlowInEasing
    )
}

