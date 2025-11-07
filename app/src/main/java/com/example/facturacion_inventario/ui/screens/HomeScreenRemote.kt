package com.example.facturacion_inventario.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.facturacion_inventario.data.repository.FakeProductRepository
import com.example.facturacion_inventario.domain.model.Product
import com.example.facturacion_inventario.ui.components.product.ProductCard
import com.example.facturacion_inventario.ui.components.product.ProductUi
import com.example.facturacion_inventario.ui.store.*
import com.example.facturacion_inventario.ui.theme.Dimens

/**
 * Convierte Product del dominio a ProductUi para la UI
 */
private fun Product.toUi(): ProductUi {
    return ProductUi(
        id = this.id,
        name = this.name,
        price = this.price,
        currency = "S/",
        oldPrice = null,
        rating = null,
        inStock = this.stock > 0,
        imageRes = this.imageRes,
        imageUrl = null
    )
}

/**
 * Pantalla Home que consume datos reales de la API
 * Usa ProductListViewModel para manejar estados de carga
 */
@Composable
fun HomeScreenRemote(
    viewModel: ProductListViewModel = viewModel(),
    onProductClick: (String) -> Unit,
    categoryId: String? = null,
    query: String? = null
) {
    val uiState by viewModel.uiState.collectAsState()

    // Cargar productos cuando cambia el filtro
    LaunchedEffect(categoryId, query) {
        viewModel.loadProducts(categoryId, query)
    }

    StoreScreenScaffold {
        when (val state = uiState) {
            is ProductListState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(Dimens.md))
                        Text(
                            text = "Cargando productos...",
                            style = MaterialTheme.typography.body2
                        )
                    }
                }
            }

            is ProductListState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(Dimens.md),
                    verticalArrangement = Arrangement.spacedBy(Dimens.md)
                ) {
                    item(key = "header") {
                        Text(
                            text = when {
                                query != null -> "Resultados para \"$query\""
                                categoryId != null -> "Productos en categoría"
                                else -> "Todos los productos"
                            },
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(vertical = Dimens.md)
                        )
                    }

                    items(state.products, key = { it.id }) { product ->
                        ProductCard(
                            product = product.toUi(),
                            onClick = { onProductClick(product.id) }
                        )
                    }

                    item(key = "footer") {
                        Spacer(modifier = Modifier.height(Dimens.xxl))
                    }
                }
            }

            is ProductListState.Empty -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(Dimens.xl)
                    ) {
                        Text(
                            text = "No se encontraron productos",
                            style = MaterialTheme.typography.h6,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(Dimens.md))
                        Text(
                            text = when {
                                query != null -> "Intenta con otros términos de búsqueda"
                                categoryId != null -> "No hay productos en esta categoría"
                                else -> "Aún no hay productos disponibles"
                            },
                            style = MaterialTheme.typography.body2,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            is ProductListState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(Dimens.xl)
                    ) {
                        Text(
                            text = "Error al cargar productos",
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.colors.error
                        )
                        Spacer(modifier = Modifier.height(Dimens.md))
                        Text(
                            text = state.message,
                            style = MaterialTheme.typography.body2,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(Dimens.lg))
                        Button(onClick = { viewModel.retry() }) {
                            Text("Reintentar")
                        }
                        Spacer(modifier = Modifier.height(Dimens.md))
                        Text(
                            text = "Verifica tu conexión a internet y que el servidor esté activo",
                            style = MaterialTheme.typography.caption,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Versión híbrida: usa datos fake por defecto pero puede cambiar a API real
 */
@Composable
fun HomeScreenHybrid(
    useRemoteData: Boolean = false,
    onProductClick: (String) -> Unit,
    selectedCategoryId: String? = null
) {
    if (useRemoteData) {
        HomeScreenRemote(
            onProductClick = onProductClick,
            categoryId = selectedCategoryId
        )
    } else {
        // Mantener la versión original con datos fake
        val repository = remember { FakeProductRepository() }
        HomeContent(
            repository = repository,
            onProductClick = onProductClick,
            onSeeAllCategoryClick = { /* navegar a categoría */ },
            selectedCategoryId = selectedCategoryId
        )
    }
}
