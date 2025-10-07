package com.example.facturacion_inventario.ui.store

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.facturacion_inventario.data.repository.FakeProductRepository

@Composable
fun HomeScreen(navController: NavController) {
    val repository = FakeProductRepository()
    HomeContent(
        repository = repository,
        onProductClick = { id -> navController.navigate("product/$id") },
        onCartClick = { navController.navigate("cart") },
        onCategoriesClick = { navController.navigate("categories") }
    )
}

@Composable
fun ProductDetailScreen(productId: String?, navController: NavController) {
    val repository = FakeProductRepository()
    val product = productId?.let { repository.getProductById(it) }
    ProductDetailContent(product = product, onAddToCart = { navController.navigate("cart") })
}

@Composable
fun CartScreen(navController: NavController) {
    CartContent(
        onContinueShopping = { navController.navigate("home") },
        onCheckout = { /* implementar checkout */ }
    )
}

@Composable
fun SearchScreen(navController: NavController) {
    SearchContent(onSearch = { _ -> navController.navigate("home") }, onBack = { navController.popBackStack() })
}

@Composable
fun CategoriesScreen(navController: NavController) {
    val categories = listOf("Motor", "Transmisión", "Frenos", "Ruedas")
    CategoriesContent(categories = categories, onCategoryClick = { _ -> navController.navigate("home") })
}
