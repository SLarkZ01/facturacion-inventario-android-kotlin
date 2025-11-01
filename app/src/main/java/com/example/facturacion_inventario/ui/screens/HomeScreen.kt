package com.example.facturacion_inventario.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.facturacion_inventario.domain.repository.ProductRepository
import com.example.facturacion_inventario.data.repository.FakeProductRepository
import com.example.facturacion_inventario.ui.store.CategorySection
import com.example.facturacion_inventario.ui.store.StoreScreenScaffold
import com.example.facturacion_inventario.ui.theme.Dimens
import com.example.facturacion_inventario.ui.components.banner.CategoryBanner

/**
 * HomeContent optimizado con keys en LazyColumn y uso de remember/derivedStateOf.
 */
@Composable
fun HomeContent(
    repository: ProductRepository,
    onProductClick: (String) -> Unit,
    onSeeAllCategoryClick: (String) -> Unit,
    selectedCategoryId: String? = null
) {
    val threshold = 120.dp
    val density = LocalDensity.current
    val thresholdPx = with(density) { threshold.toPx() }

    val listState = rememberLazyListState()

    // Usar derivedStateOf para evitar recomposiciones innecesarias al calcular progress
    val progress by remember {
        derivedStateOf {
            if (listState.firstVisibleItemIndex > 0) 1f
            else (listState.firstVisibleItemScrollOffset / thresholdPx).coerceIn(0f, 1f)
        }
    }

    // Cachear las categorías y productos para evitar recalcular en cada recomposición
    val repo = remember { repository as? FakeProductRepository }
    val categories = remember { repo?.getCategories() ?: emptyList() }

    // Filtrar categorías si hay una seleccionada - usar remember para cachear el resultado
    val displayCategories = remember(selectedCategoryId, categories) {
        if (selectedCategoryId != null) {
            categories.filter { it.id == selectedCategoryId }
        } else {
            categories
        }
    }

    // Cachear la categoría seleccionada
    val selectedCategory = remember(selectedCategoryId, categories) {
        categories.find { it.id == selectedCategoryId }
    }

    StoreScreenScaffold(headerProgress = progress) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Dimens.lg)
            ) {
                // Mostrar banner si hay categoría seleccionada
                if (selectedCategoryId != null && selectedCategory != null) {
                    item(key = "category_banner_$selectedCategoryId") {
                        CategoryBanner(category = selectedCategory)
                    }
                }

                item(key = "top_spacer") {
                    Spacer(modifier = Modifier.height(Dimens.s))
                }

                // KEY CRÍTICO: usar category.id como key para identificar cada sección
                items(displayCategories, key = { it.id }) { category ->
                    // Cachear productos por categoría
                    val productsInCategory = remember(category.id) {
                        repo?.getProductsByCategory(category.id) ?: emptyList()
                    }

                    if (productsInCategory.isNotEmpty()) {
                        CategorySection(
                            category = category,
                            products = productsInCategory,
                            onProductClick = onProductClick,
                            onSeeAllClick = onSeeAllCategoryClick,
                            showAll = selectedCategoryId != null
                        )
                    }
                }

                item(key = "bottom_spacer") {
                    Spacer(modifier = Modifier.height(Dimens.xxl))
                }
            }

            Spacer(modifier = Modifier.height(Dimens.lg))
        }
    }
}
