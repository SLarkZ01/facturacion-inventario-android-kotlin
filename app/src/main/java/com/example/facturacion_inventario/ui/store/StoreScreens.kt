package com.example.facturacion_inventario.ui.store

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.facturacion_inventario.data.repository.FakeProductRepository

@Composable
fun HomeScreen(navController: NavController, selectedCategoryId: String? = null) {
    val repository = FakeProductRepository()
    HomeContent(
        repository = repository,
        onProductClick = { id -> navController.navigate(Routes.productRoute(id)) },
        onCartClick = { navController.navigate(Routes.CART) },
        onCategoriesClick = { navController.navigate(Routes.CATEGORIES) },
        onSeeAllCategoryClick = { categoryId ->
            navController.navigate(Routes.homeWithCategory(categoryId))
        },
        selectedCategoryId = selectedCategoryId
    )
}

@Composable
fun ProductDetailScreen(productId: String?, navController: NavController) {
    val repository = FakeProductRepository()
    val product = productId?.let { repository.getProductById(it) }
    ProductDetailContent(product = product, onAddToCart = { navController.navigate(Routes.CART) })
}

@Composable
fun CartScreen(navController: NavController) {
    CartContent(
        onContinueShopping = { navController.navigate(Routes.HOME) },
        onCheckout = { /* implementar checkout */ }
    )
}

@Composable
fun CategoriesScreen(navController: NavController) {
    val repository = FakeProductRepository()
    val categories = repository.getCategories()
    CategoriesContent(
        categories = categories,
        onCategoryClick = { categoryId ->
            navController.navigate(Routes.homeWithCategory(categoryId)) {
                popUpTo(Routes.HOME) { inclusive = false }
            }
        }
    )
}
