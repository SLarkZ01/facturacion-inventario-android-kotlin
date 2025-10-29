package com.example.facturacion_inventario.ui.store

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.facturacion_inventario.data.repository.FakeProductRepository

@Composable
fun HomeScreen(navController: NavController, selectedCategoryId: String? = null) {
    val repository = FakeProductRepository()
    HomeContent(
        repository = repository,
        onProductClick = { id -> navController.navigate(Routes.productRoute(id)) },
        onSeeAllCategoryClick = { categoryId ->
            navController.navigate(Routes.homeWithCategory(categoryId))
        },
        selectedCategoryId = selectedCategoryId
    )
}

@Composable
fun ProductDetailScreen(productId: String?, cartViewModel: CartViewModel = viewModel()) {
    val repository = FakeProductRepository()
    val product = productId?.let { repository.getProductById(it) }
    ProductDetailContent(
        product = product,
        onAddToCart = { /* Ya no navegamos automáticamente, mostramos mensaje */ },
        cartViewModel = cartViewModel
    )
}

@Composable
fun CartScreen(navController: NavController, cartViewModel: CartViewModel = viewModel()) {
    CartContent(
        onContinueShopping = { navController.navigate(Routes.HOME) },
        onCheckout = { /* implementar checkout */ },
        cartViewModel = cartViewModel
    )
}

@Composable
fun CategoriesScreen(navController: NavController) {
    val repository = FakeProductRepository()
    val categories = repository.getCategories()
    CategoriesContent(
        categories = categories,
        onCategoryClick = { categoryId: String ->
            navController.navigate(Routes.homeWithCategory(categoryId)) {
                popUpTo(Routes.HOME) { inclusive = false }
            }
        }
    )
}
