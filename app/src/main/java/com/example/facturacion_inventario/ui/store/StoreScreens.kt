package com.example.facturacion_inventario.ui.store

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.facturacion_inventario.domain.model.Category
import com.example.facturacion_inventario.ui.screens.CartContent
import com.example.facturacion_inventario.ui.screens.HomeScreenRemote
import com.example.facturacion_inventario.ui.screens.ProductDetailScreenRemote
import com.example.facturacion_inventario.ui.screens.CategoriesScreenRemote
import androidx.compose.ui.Modifier
import com.example.facturacion_inventario.ui.components.category.CategoryCard
import com.example.facturacion_inventario.ui.theme.Dimens

/**
 * HomeScreen ahora consume datos REALES de la API de Spring Boot
 * Cambiado para usar HomeScreenRemote en lugar de FakeProductRepository
 */
@Composable
fun HomeScreen(navController: NavController, selectedCategoryId: String? = null) {
    HomeScreenRemote(
        onProductClick = { id -> navController.navigate(Routes.productRoute(id)) },
        onCategoryClick = { categoryId -> navController.navigate(Routes.homeWithCategory(categoryId)) },
        categoryId = selectedCategoryId
    )
}

/**
 * ProductDetailScreen ahora consume datos REALES de la API
 * Cambiado para usar ProductDetailScreenRemote
 */
@Composable
fun ProductDetailScreen(productId: String?, cartViewModel: CartViewModel = viewModel()) {
    if (productId != null) {
        ProductDetailScreenRemote(
            productId = productId,
            cartViewModel = cartViewModel
        )
    }
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
    // Usar CategoriesScreenRemote que consume la API real
    CategoriesScreenRemote(
        onCategoryClick = { categoryId: String ->
            navController.navigate(Routes.homeWithCategory(categoryId)) {
                popUpTo(Routes.HOME) { inclusive = false }
            }
        }
    )
}
