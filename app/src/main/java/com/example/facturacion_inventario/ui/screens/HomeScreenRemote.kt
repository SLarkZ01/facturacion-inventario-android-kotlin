package com.example.facturacion_inventario.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.facturacion_inventario.domain.model.Product
import com.example.facturacion_inventario.domain.model.Category
import com.example.facturacion_inventario.ui.components.product.ProductCard
import com.example.facturacion_inventario.ui.components.product.ProductUi
import com.example.facturacion_inventario.ui.store.*
import com.example.facturacion_inventario.ui.theme.Dimens
import com.example.facturacion_inventario.R

/**
 * Convierte Product del dominio a ProductUi para la UI
 */
private fun Product.toUi(): ProductUi {
    // Obtener la URL de la primera imagen si existe
    val firstImageUrl = this.mediaList.firstOrNull()?.url

    return ProductUi(
        id = this.id,
        name = this.name,
        price = this.price,
        currency = "S/",
        oldPrice = null,
        rating = null,
        inStock = this.stock > 0,
        imageRes = this.imageRes,
        imageUrl = firstImageUrl
    )
}

/**
 * Composable que muestra el icono de una categoría de manera segura
 * Prioridad: 1) Imagen desde URL del backend, 2) Icono local, 3) Icono de Ermotos por defecto
 */
@Composable
private fun CategoryIcon(
    category: Category,
    modifier: Modifier = Modifier
) {
    val context = androidx.compose.ui.platform.LocalContext.current

    // Si hay una URL de imagen, intentar cargarla
    if (!category.imageUrl.isNullOrBlank()) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(context)
                .data(category.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = category.name,
            modifier = modifier,
            contentScale = ContentScale.Fit,
            loading = {
                // Mostrar un indicador de carga mientras se descarga la imagen
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                }
            },
            error = {
                // Si falla la carga de la URL, mostrar el ícono de Ermotos por defecto
                Icon(
                    painter = painterResource(id = R.drawable.ermotoshd),
                    contentDescription = category.name,
                    modifier = Modifier.fillMaxSize(),
                    tint = Color.Unspecified
                )
            }
        )
    } else {
        // No hay URL, intentar usar el iconRes local
        val hasValidIcon = remember(category.iconRes) {
            try {
                if (category.iconRes != 0 && category.iconRes != -1 && category.iconRes != R.drawable.ermotoshd) {
                    val resourceTypeName = try {
                        context.resources.getResourceTypeName(category.iconRes)
                    } catch (e: Exception) {
                        null
                    }
                    // Solo es válido si es un drawable o mipmap
                    resourceTypeName == "drawable" || resourceTypeName == "mipmap"
                } else {
                    false
                }
            } catch (e: Exception) {
                false
            }
        }

        if (hasValidIcon) {
            // Mostrar el icono local
            Icon(
                painter = painterResource(id = category.iconRes),
                contentDescription = category.name,
                modifier = modifier,
                tint = Color.Unspecified
            )
        } else {
            // Mostrar el ícono de Ermotos por defecto
            Icon(
                painter = painterResource(id = R.drawable.ermotoshd),
                contentDescription = category.name,
                modifier = modifier,
                tint = Color.Unspecified
            )
        }
    }
}

/**
 * Pantalla Home que muestra categorías con productos asociados
 * Consume datos reales de la API
 */
@Composable
fun HomeScreenRemote(
    viewModel: ProductListViewModel = viewModel(),
    categoryViewModel: CategoryViewModel = viewModel(),
    onProductClick: (String) -> Unit,
    onCategoryClick: ((String) -> Unit)? = null,
    categoryId: String? = null,
    query: String? = null
) {
    val categoriesState by categoryViewModel.uiState.collectAsState()

    // Cargar categorías al iniciar
    LaunchedEffect(Unit) {
        categoryViewModel.loadPublicCategories() // << Usar endpoint público para la app pública
    }

    StoreScreenScaffold {
        when (val catState = categoriesState) {
            is CategoryListState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(Dimens.md))
                        Text(
                            text = "Cargando categorías...",
                            style = MaterialTheme.typography.body2
                        )
                    }
                }
            }

            is CategoryListState.Success -> {
                // Si hay un categoryId, mostrar solo esa categoría con todos sus productos
                if (categoryId != null) {
                    val selectedCategory = catState.categories.find { it.id == categoryId }
                    if (selectedCategory != null) {
                        SingleCategoryProductsView(
                            category = selectedCategory,
                            onProductClick = onProductClick,
                            viewModel = viewModel
                        )
                    } else {
                        // Categoría no encontrada
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Categoría no encontrada")
                                Spacer(modifier = Modifier.height(Dimens.md))
                                Button(onClick = { onCategoryClick?.invoke("") }) {
                                    Text("Volver")
                                }
                            }
                        }
                    }
                } else {
                    // Mostrar todas las categorías con productos
                    HomeCategoriesWithProducts(
                        categories = catState.categories,
                        onProductClick = onProductClick,
                        onCategoryClick = onCategoryClick,
                        viewModel = viewModel
                    )
                }
            }

            is CategoryListState.Empty -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay categorías disponibles",
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center
                    )
                }
            }

            is CategoryListState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(Dimens.xl)
                    ) {
                        Text(
                            text = "Error al cargar categorías",
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.colors.error
                        )
                        Spacer(modifier = Modifier.height(Dimens.md))
                        Text(
                            text = catState.message,
                            style = MaterialTheme.typography.body2,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(Dimens.lg))
                        Button(onClick = { categoryViewModel.loadPublicCategories() }) {
                            Text("Reintentar")
                        }
                    }
                }
            }
        }
    }
}

/**
 * Muestra categorías con sus productos asociados en formato de secciones
 */
@Composable
private fun HomeCategoriesWithProducts(
    categories: List<Category>,
    onProductClick: (String) -> Unit,
    onCategoryClick: ((String) -> Unit)?,
    viewModel: ProductListViewModel
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = Dimens.md)
    ) {
        item(key = "header") {
            Text(
                text = "Todas las Categorías",
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = Dimens.md, vertical = Dimens.md)
            )
        }

        items(categories, key = { it.id }) { category ->
            CategorySection(
                category = category,
                onProductClick = onProductClick,
                onCategoryClick = onCategoryClick,
                viewModel = viewModel
            )
        }

        item(key = "footer") {
            Spacer(modifier = Modifier.height(Dimens.xxl))
        }
    }
}

/**
 * Sección de una categoría con sus productos en scroll horizontal
 */
@Composable
private fun CategorySection(
    category: Category,
    onProductClick: (String) -> Unit,
    onCategoryClick: ((String) -> Unit)?,
    viewModel: ProductListViewModel
) {
    var products by remember(category.id) { mutableStateOf<List<Product>>(emptyList()) }
    var isLoading by remember(category.id) { mutableStateOf(true) }
    var hasError by remember(category.id) { mutableStateOf(false) }
    var errorMessage by remember(category.id) { mutableStateOf("") }

    // Cargar productos de esta categoría directamente desde el repositorio
    LaunchedEffect(category.id) {
        isLoading = true
        hasError = false
        errorMessage = ""

        try {
            val repository = com.example.facturacion_inventario.data.repository.RemoteProductRepository()
            repository.getPublicProductosAsync(categoriaId = category.id).fold(
                onSuccess = { productList ->
                    products = productList
                    isLoading = false
                    hasError = false
                },
                onFailure = { error ->
                    isLoading = false
                    hasError = true
                    errorMessage = error.message ?: "Error al cargar productos"
                    products = emptyList()
                }
            )
        } catch (e: Exception) {
            isLoading = false
            hasError = true
            errorMessage = e.message ?: "Error de conexión"
            products = emptyList()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.md)
    ) {
        // Header de la categoría con botón "Ver todos"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = category.name,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )


            // Botón "Ver todos"
            TextButton(
                onClick = {
                    onCategoryClick?.invoke(category.id)
                }
            ) {
                Text(
                    text = "Ver todos",
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(Dimens.md))

        // Lista horizontal de productos
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.height(Dimens.s))
                        Text(
                            text = "Cargando productos...",
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }
            hasError -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(horizontal = Dimens.md),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Error al cargar productos",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.error,
                            fontWeight = FontWeight.Bold
                        )
                        if (errorMessage.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = errorMessage,
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.error.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
            products.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(horizontal = Dimens.md),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "No hay productos en esta categoría",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Agrega productos desde el backend",
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.4f)
                        )
                    }
                }
            }
            else -> {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = Dimens.md),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.md)
                ) {
                    items(products, key = { it.id }) { product ->
                        ProductCard(
                            product = product.toUi(),
                            onClick = { onProductClick(product.id) },
                            modifier = Modifier.width(200.dp)
                        )
                    }
                }
            }
        }

        Divider(
            modifier = Modifier.padding(top = Dimens.lg),
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
        )
    }
}

/**
 * Pantalla que muestra todos los productos de una categoría específica
 */
@Composable
fun SingleCategoryProductsView(
    category: Category,
    onProductClick: (String) -> Unit,
    viewModel: ProductListViewModel
) {
    var products by remember(category.id) { mutableStateOf<List<Product>>(emptyList()) }
    var isLoading by remember(category.id) { mutableStateOf(true) }
    var hasError by remember(category.id) { mutableStateOf(false) }

    // Cargar productos de esta categoría directamente desde el repositorio
    LaunchedEffect(category.id) {
        isLoading = true
        hasError = false

        try {
            val repository = com.example.facturacion_inventario.data.repository.RemoteProductRepository()
            repository.getPublicProductosAsync(categoriaId = category.id).fold(
                onSuccess = { productList ->
                    products = productList
                    isLoading = false
                    hasError = false
                },
                onFailure = { _ ->
                    isLoading = false
                    hasError = true
                    products = emptyList()
                }
            )
        } catch (_: Exception) {
            isLoading = false
            hasError = true
            products = emptyList()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Header con recuadro de la categoría (icono + nombre + descripción)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.md, vertical = Dimens.md),
            color = Color.White,
            shape = RoundedCornerShape(Dimens.cornerMedium),
            elevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.lg),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icono de la categoría
                Surface(
                    modifier = Modifier
                        .size(60.dp),
                    color = Color(0xFFF5F5F5),
                    shape = RoundedCornerShape(Dimens.cornerSmall)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        // Mostrar icono de categoría de manera segura
                        CategoryIcon(
                            category = category,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(Dimens.lg))

                // Nombre y descripción
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = category.description,
                        style = MaterialTheme.typography.body2,
                        color = Color.Gray,
                        fontSize = 13.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        // Grid de productos
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(32.dp))
                }
            }
            hasError -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(horizontal = Dimens.md),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error al cargar productos",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.error
                    )
                }
            }
            products.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(horizontal = Dimens.md),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay productos en esta categoría",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(horizontal = Dimens.s, vertical = Dimens.s),
                    verticalArrangement = Arrangement.spacedBy(Dimens.s),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.s)
                ) {
                    items(products, key = { it.id }) { product ->
                        ProductCard(
                            product = product.toUi(),
                            onClick = { onProductClick(product.id) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
