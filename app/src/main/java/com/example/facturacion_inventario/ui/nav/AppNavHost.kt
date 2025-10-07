package com.example.facturacion_inventario.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.auth.AuthViewModel
import com.example.facturacion_inventario.ui.login.LoginScreen
import com.example.facturacion_inventario.ui.register.RegisterScreen
import com.example.facturacion_inventario.ui.dashboard.DashboardScreen
import com.example.facturacion_inventario.ui.store.StoreHost

@Composable
fun AppNavHost(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
    val startDestination = if (isAuthenticated) "store" else "login"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            LoginScreen(authViewModel = authViewModel, navController = navController)
        }
        composable("register") {
            RegisterScreen(authViewModel = authViewModel, navController = navController)
        }
        composable("dashboard") {
            DashboardScreen(authViewModel = authViewModel, navController = navController, onLogout = {
                navController.navigate("login") {
                    popUpTo("dashboard") { inclusive = true }
                }
            })
        }
        composable("store") {
            StoreHost(authViewModel = authViewModel, rootNavController = navController)
        }
        composable("home") {
            com.example.facturacion_inventario.ui.store.HomeScreen(navController = navController)
        }
        composable("product/{productId}") { backStackEntry ->
            val pid = backStackEntry.arguments?.getString("productId")
            com.example.facturacion_inventario.ui.store.ProductDetailScreen(productId = pid, navController = navController)
        }
        composable("cart") {
            com.example.facturacion_inventario.ui.store.CartScreen(navController = navController)
        }
        composable("categories") {
            com.example.facturacion_inventario.ui.store.CategoriesScreen(navController = navController)
        }
    }
}
