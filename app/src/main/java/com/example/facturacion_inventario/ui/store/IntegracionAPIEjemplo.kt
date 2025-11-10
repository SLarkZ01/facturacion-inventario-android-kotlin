package com.example.facturacion_inventario.ui.store

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.facturacion_inventario.ui.screens.HomeScreenRemote
import com.example.facturacion_inventario.ui.screens.ProductDetailScreenRemote

/**
 * EJEMPLO DE INTEGRACIÓN - Código de referencia
 *
 * ESTE ARCHIVO ES SOLO PARA REFERENCIA - NO SE USA DIRECTAMENTE
 * Las funciones reales están en StoreScreens.kt
 *
 * PASO 1: Copia estas funciones
 * PASO 2: Reemplaza las funciones HomeScreen y ProductDetailScreen en StoreScreens.kt
 * PASO 3: Asegúrate de que tu backend esté corriendo en http://localhost:8080
 * PASO 4: Ejecuta la app y verás los productos reales de tu API
 */

/**
 * Ejemplo de HomeScreen que consume la API real de productos
 * COPIA ESTA FUNCIÓN para reemplazar HomeScreen en StoreScreens.kt
 */
@Composable
fun HomeScreenExample(navController: NavController, selectedCategoryId: String? = null) {
    HomeScreenRemote(
        onProductClick = { id -> navController.navigate(Routes.productRoute(id)) },
        categoryId = selectedCategoryId
    )
}

/**
 * Ejemplo de ProductDetailScreen que consume la API real
 * COPIA ESTA FUNCIÓN para reemplazar ProductDetailScreen en StoreScreens.kt
 */
@Composable
fun ProductDetailScreenExample(productId: String?, remoteCartViewModel: RemoteCartViewModel) {
    if (productId != null) {
        ProductDetailScreenRemote(
            productId = productId,
            cartViewModel = remoteCartViewModel
        )
    }
}

/**
 * ALTERNATIVA: Versión con toggle para cambiar entre datos fake y reales
 * Útil para desarrollo y testing
 */
@Composable
fun HomeScreenConToggleExample(navController: NavController) {
    var useRemoteData by remember { mutableStateOf(false) }

    // TO DO: Agregar un Switch en la UI para cambiar useRemoteData
    // Por ahora usa true para datos reales, false para datos fake

    if (useRemoteData) {
        HomeScreenRemote(
            onProductClick = { id -> navController.navigate(Routes.productRoute(id)) }
        )
    } else {
        // Versión original con datos fake
        val repository = remember { com.example.facturacion_inventario.data.repository.FakeProductRepository() }
        com.example.facturacion_inventario.ui.screens.HomeContent(
            repository = repository,
            onProductClick = { id -> navController.navigate(Routes.productRoute(id)) },
            onSeeAllCategoryClick = { categoryId ->
                navController.navigate(Routes.homeWithCategory(categoryId))
            }
        )
    }
}
