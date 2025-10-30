package com.example.facturacion_inventario.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import com.example.facturacion_inventario.domain.repository.ProductRepository
import com.example.facturacion_inventario.data.repository.FakeProductRepository
import com.example.facturacion_inventario.ui.store.CategorySection
import com.example.facturacion_inventario.ui.store.StoreScreenScaffold
import com.example.facturacion_inventario.ui.theme.Dimens

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

    val progress by remember {
        derivedStateOf {
            if (listState.firstVisibleItemIndex > 0) 1f
            else (listState.firstVisibleItemScrollOffset / thresholdPx).coerceIn(0f, 1f)
        }
    }

    StoreScreenScaffold(headerProgress = progress) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Obtenemos las categorías y productos desde el repositorio
            val repo = repository as? FakeProductRepository
            val categories = repo?.getCategories() ?: emptyList()

            // Filtrar categorías si hay una seleccionada
            val displayCategories = if (selectedCategoryId != null) {
                categories.filter { it.id == selectedCategoryId }
            } else {
                categories
            }

            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Dimens.lg)
            ) {
                // Mostrar banner si hay categoría seleccionada
                if (selectedCategoryId != null) {
                    item {
                        val selectedCategory = categories.find { it.id == selectedCategoryId }
                        selectedCategory?.let { category ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = Dimens.s),
                                backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.08f),
                                elevation = 2.dp,
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Row(
                                    modifier = Modifier.padding(Dimens.lg),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Imagen del icono de la categoría a la izquierda
                                    Image(
                                        painter = painterResource(id = category.iconRes),
                                        contentDescription = category.name,
                                        modifier = Modifier.size(Dimens.imageLarge)
                                    )

                                    Spacer(modifier = Modifier.width(Dimens.md))

                                    // Nombre y descripción a la derecha
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = category.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = MaterialTheme.colors.onSurface
                                        )
                                        Spacer(modifier = Modifier.height(Dimens.s))
                                        Text(
                                            text = category.description,
                                            fontSize = 14.sp,
                                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.75f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(Dimens.s)) }

                items(displayCategories) { category ->
                    val productsInCategory = repo?.getProductsByCategory(category.id) ?: emptyList()
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

                item { Spacer(modifier = Modifier.height(Dimens.xxl)) }
            }

            Spacer(modifier = Modifier.height(Dimens.lg))
        }
    }
}
