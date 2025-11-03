package com.example.facturacion_inventario.ui.nav

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.auth.AuthViewModel
import com.example.facturacion_inventario.ui.login.LoginScreen
import com.example.facturacion_inventario.ui.register.RegisterScreen
import com.example.facturacion_inventario.ui.dashboard.DashboardScreen
import com.example.facturacion_inventario.ui.store.StoreHost
import com.example.facturacion_inventario.ui.splash.SplashScreen
import com.example.facturacion_inventario.ui.animations.NavigationTransitions

@Composable
fun AppNavHost(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()

    // Control del splash screen
    var showSplash by remember { mutableStateOf(true) }

    if (showSplash) {
        SplashScreen(
            onSplashComplete = { showSplash = false }
        )
    } else {
        val startDestination = if (isAuthenticated) "store" else "login"

        NavHost(
            navController = navController,
            startDestination = startDestination,
            enterTransition = { NavigationTransitions.slideInFromRight() },
            exitTransition = { NavigationTransitions.slideOutToLeft() },
            popEnterTransition = { NavigationTransitions.slideInFromLeft() },
            popExitTransition = { NavigationTransitions.slideOutToRight() }
        ) {
            composable(
                "login",
                enterTransition = { NavigationTransitions.fadeInOnly() },
                exitTransition = { NavigationTransitions.fadeOutOnly() }
            ) {
                LoginScreen(authViewModel = authViewModel, navController = navController)
            }

            composable(
                "register",
                enterTransition = { NavigationTransitions.slideInFromRight() },
                exitTransition = { NavigationTransitions.slideOutToRight() }
            ) {
                RegisterScreen(authViewModel = authViewModel, navController = navController)
            }

            composable(
                "dashboard",
                enterTransition = { NavigationTransitions.fadeInOnly() },
                exitTransition = { NavigationTransitions.fadeOutOnly() }
            ) {
                DashboardScreen(authViewModel = authViewModel, navController = navController, onLogout = {
                    navController.navigate("login") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                })
            }

            composable(
                "store",
                enterTransition = { NavigationTransitions.fadeInOnly() },
                exitTransition = { NavigationTransitions.fadeOutOnly() }
            ) {
                StoreHost(authViewModel = authViewModel, rootNavController = navController)
            }

            composable("home") {
                com.example.facturacion_inventario.ui.store.HomeScreen(navController = navController)
            }

            composable("product/{productId}") { backStackEntry ->
                val pid = backStackEntry.arguments?.getString("productId")
                com.example.facturacion_inventario.ui.store.ProductDetailScreen(productId = pid)
            }

            composable("cart") {
                com.example.facturacion_inventario.ui.store.CartScreen(navController = navController)
            }

            composable("categories") {
                com.example.facturacion_inventario.ui.store.CategoriesScreen(navController = navController)
            }
        }
    }
}
