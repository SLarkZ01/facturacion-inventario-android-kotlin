package com.example.facturacion_inventario.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * Gestión profesional de las barras de sistema (status bar y navigation bar).
 * Proporciona una experiencia visual cohesiva y pulida.
 */
@Composable
fun SystemBarsColor(
    statusBarColor: Color = MaterialTheme.colors.primary,
    navigationBarColor: Color = MaterialTheme.colors.background,
    statusBarDarkIcons: Boolean = false,
    navigationBarDarkIcons: Boolean = true
) {
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = statusBarDarkIcons
        )

        systemUiController.setNavigationBarColor(
            color = navigationBarColor,
            darkIcons = navigationBarDarkIcons
        )
    }
}

/**
 * Configuración para pantalla con fondo claro
 */
@Composable
fun LightSystemBars() {
    SystemBarsColor(
        statusBarColor = Color.White,
        navigationBarColor = Color.White,
        statusBarDarkIcons = true,
        navigationBarDarkIcons = true
    )
}

/**
 * Configuración para pantalla con fondo oscuro
 */
@Composable
fun DarkSystemBars() {
    SystemBarsColor(
        statusBarColor = Color.Black,
        navigationBarColor = Color.Black,
        statusBarDarkIcons = false,
        navigationBarDarkIcons = false
    )
}

/**
 * Configuración para pantallas con barra de estado transparente
 */
@Composable
fun TransparentStatusBar(darkIcons: Boolean = false) {
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = darkIcons
        )
    }
}

/**
 * Configuración para SplashScreen con barras de color primario
 */
@Composable
fun SplashSystemBars() {
    val primaryColor = MaterialTheme.colors.primary

    SystemBarsColor(
        statusBarColor = primaryColor,
        navigationBarColor = primaryColor,
        statusBarDarkIcons = false,
        navigationBarDarkIcons = false
    )
}

/**
 * Configuración para pantallas inmersivas (fullscreen)
 */
@Composable
fun ImmersiveSystemBars() {
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.isStatusBarVisible = false
        systemUiController.isNavigationBarVisible = false
    }
}

