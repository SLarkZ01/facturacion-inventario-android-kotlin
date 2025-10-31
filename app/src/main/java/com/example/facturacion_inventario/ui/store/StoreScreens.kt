package com.example.facturacion_inventario.ui.store

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.facturacion_inventario.data.repository.FakeProductRepository
import com.example.facturacion_inventario.domain.model.Category
import com.example.facturacion_inventario.ui.screens.CartContent
import com.example.facturacion_inventario.ui.screens.HomeContent
import com.example.facturacion_inventario.ui.screens.ProductDetailContent
import androidx.compose.ui.Modifier
import com.example.facturacion_inventario.ui.components.CategoryCard
import com.example.facturacion_inventario.ui.theme.Dimens

@Composable
fun HomeScreen(navController: NavController, selectedCategoryId: String? = null) {
    // Usar remember para cachear el repositorio
    val repository = remember { FakeProductRepository() }
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
    // Usar remember para cachear el repositorio
    val repository = remember { FakeProductRepository() }
    val product = remember(productId) { productId?.let { repository.getProductById(it) } }
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

/**
 * CategoriesContent optimizado con keys para mejor rendimiento.
 */
@Composable
fun CategoriesContent(categories: List<Category>, onCategoryClick: (String) -> Unit) {
    StoreScreenScaffold {
        // Movemos el título dentro del LazyColumn para que scrollee con la lista
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = Dimens.lg, vertical = Dimens.md),
            verticalArrangement = Arrangement.spacedBy(Dimens.md)
        ) {
            item(key = "header") {
                Spacer(modifier = Modifier.height(Dimens.lg))
                Text(text = "Todas las Categorías", style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(Dimens.md))
            }

            // KEY CRÍTICO: permite a Compose identificar items únicos
            items(categories, key = { it.id }) { category ->
                // Usamos CategoryCard que ya muestra el icono (category.iconRes) y la descripción
                CategoryCard(
                    category = category,
                    onClick = { onCategoryClick(category.id) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun CategoriesScreen(navController: NavController) {
    // Usar remember para cachear el repositorio y las categorías
    val repository = remember { FakeProductRepository() }
    val categories = remember { repository.getCategories() }

    CategoriesContent(
        categories = categories,
        onCategoryClick = { categoryId: String ->
            navController.navigate(Routes.homeWithCategory(categoryId)) {
                popUpTo(Routes.HOME) { inclusive = false }
            }
        }
    )
}
