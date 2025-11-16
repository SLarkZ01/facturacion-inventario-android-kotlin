package com.example.facturacion_inventario.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.facturacion_inventario.R
import com.example.facturacion_inventario.domain.model.Product
import com.example.facturacion_inventario.ui.components.product.ProductCard
import com.example.facturacion_inventario.ui.components.product.ProductUi
import com.example.facturacion_inventario.ui.store.SearchViewModel
import com.example.facturacion_inventario.ui.theme.Dimens
import kotlinx.coroutines.delay

/**
 * Convierte Product del dominio a ProductUi para la UI
 */
private fun Product.toUi(): ProductUi {
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
 * Pantalla de búsqueda estilo Amazon
 *
 * Esta pantalla se muestra cuando el usuario toca la barra de búsqueda en el inicio.
 * Contiene:
 * - Barra de búsqueda funcional (mismo diseño que la del inicio)
 * - Lista de resultados en grid
 * - Indicador de carga
 * - Mensajes de estado (sin resultados, error, etc)
 *
 * @param navController Controlador de navegación para volver atrás o navegar a detalles
 * @param searchViewModel ViewModel que maneja la lógica de búsqueda y estado
 */
@Composable
fun SearchScreen(
    navController: NavController,
    searchViewModel: SearchViewModel = viewModel()
) {
    // Estado de la búsqueda actual
    var searchQuery by remember { mutableStateOf("") }
    var showSuggestions by remember { mutableStateOf(false) }

    // Observar resultados del ViewModel
    val searchResults by searchViewModel.searchResults.collectAsState()
    val isSearching by searchViewModel.isSearching.collectAsState()

    // Nuevo: Observar sugerencias
    val suggestions by searchViewModel.suggestions.collectAsState()
    val isLoadingSuggestions by searchViewModel.isLoadingSuggestions.collectAsState()

    // 🔥 FocusRequester y KeyboardController para enfocar automáticamente
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    // 🔥 Enfocar el campo de búsqueda automáticamente al abrir la pantalla
    LaunchedEffect(Unit) {
        delay(100) // Pequeño delay para asegurar que to do está renderizado
        try {
            focusRequester.requestFocus()
            keyboardController?.show()
        } catch (e: Exception) {
            // Ignorar errores de focus
        }
    }

    // Scaffold sin topBar para tener control total del diseño
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colors.background)
        ) {
            // Header naranja con barra de búsqueda (similar al inicio)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colors.primary, // Color naranja
                elevation = 4.dp
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding() // Respeta la barra de estado
                            .padding(horizontal = 12.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Botón de regreso
                        IconButton(
                            onClick = { navController.navigateUp() },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_back),
                                contentDescription = "Volver",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Barra de búsqueda funcional con sugerencias
                        Box(modifier = Modifier.weight(1f)) {
                            AmazonStyleSearchBar(
                                query = searchQuery,
                                onQueryChange = { newQuery ->
                                    searchQuery = newQuery
                                    showSuggestions = newQuery.isNotBlank()

                                    // Obtener sugerencias en tiempo real
                                    if (newQuery.isNotBlank()) {
                                        searchViewModel.getSuggestions(newQuery)
                                    } else {
                                        searchViewModel.clearSuggestions()
                                    }
                                },
                                onClear = {
                                    searchQuery = ""
                                    showSuggestions = false
                                    searchViewModel.clearAll()
                                },
                                onSearch = {
                                    // Al presionar enter o buscar, ocultar sugerencias y buscar
                                    showSuggestions = false
                                    if (searchQuery.isNotBlank()) {
                                        searchViewModel.searchProducts(searchQuery)
                                    }
                                },
                                focusRequester = focusRequester,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }

            // Contenido principal con sugerencias o resultados
            Box(modifier = Modifier.fillMaxSize()) {
                // Dropdown de sugerencias (se muestra encima del contenido)
                if (showSuggestions && (suggestions.isNotEmpty() || isLoadingSuggestions)) {
                    SuggestionsDropdown(
                        suggestions = suggestions,
                        isLoading = isLoadingSuggestions,
                        query = searchQuery,
                        onSuggestionClick = { product ->
                            // Al hacer clic en una sugerencia, ir al producto
                            showSuggestions = false
                            searchViewModel.clearSuggestions()
                            navController.navigate("product/${product.id}")
                        },
                        onDismiss = {
                            showSuggestions = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                            .zIndex(10f)
                    )
                }

                // Contenido según el estado
                when {
                    // Mostrando indicador de carga
                    isSearching -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colors.primary,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Buscando productos...",
                                    style = MaterialTheme.typography.body1,
                                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }

                    // Sin resultados y búsqueda vacía (estado inicial)
                    searchQuery.isBlank() -> {
                        EmptySearchState()
                    }

                    // Sin resultados pero con búsqueda activa
                    searchResults.isEmpty() && !isSearching -> {
                        NoResultsState(query = searchQuery)
                    }

                    // Mostrando resultados
                    else -> {
                        SearchResultsGrid(
                            results = searchResults.map { it.toUi() },
                            onProductClick = { productId ->
                                // Navegar a la pantalla de detalle del producto
                                navController.navigate("product/$productId")
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Dropdown de sugerencias estilo Amazon
 * Muestra una lista de productos sugeridos mientras el usuario escribe
 */
@Composable
private fun SuggestionsDropdown(
    suggestions: List<Product>,
    isLoading: Boolean,
    query: String,
    onSuggestionClick: (Product) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color.White,
        shape = RoundedCornerShape(8.dp),
        elevation = 8.dp
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 400.dp)
        ) {
            // Mostrar indicador de carga si está buscando
            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colors.primary,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Buscando...",
                                style = MaterialTheme.typography.body2,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }

            // Mostrar sugerencias
            items(suggestions) { product ->
                SuggestionItem(
                    product = product,
                    query = query,
                    onClick = { onSuggestionClick(product) }
                )
                Divider(color = Color.LightGray.copy(alpha = 0.3f), thickness = 0.5.dp)
            }

            // Mensaje si no hay sugerencias
            if (!isLoading && suggestions.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No se encontraron sugerencias",
                            style = MaterialTheme.typography.body2,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

/**
 * Item individual de sugerencia
 */
@Composable
private fun SuggestionItem(
    product: Product,
    query: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icono de búsqueda
        Icon(
            painter = painterResource(id = R.drawable.ic_search),
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Nombre del producto
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.body1,
                color = Color.Black,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Precio
            Text(
                text = "S/ ${String.format("%.2f", product.price)}",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Bold
            )
        }

        // Icono de flecha
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_forward),
            contentDescription = "Ver producto",
            tint = Color.Gray.copy(alpha = 0.5f),
            modifier = Modifier.size(16.dp)
        )
    }
}

/**
 * Barra de búsqueda estilo Amazon
 * Diseño: fondo blanco, bordes redondeados, icono de búsqueda, icono de limpiar
 *
 * @param query Texto actual de búsqueda
 * @param onQueryChange Callback cuando cambia el texto
 * @param onClear Callback para limpiar el texto
 * @param onSearch Callback cuando se presiona buscar (enter)
 * @param focusRequester FocusRequester para controlar el enfoque del campo
 * @param modifier Modificador para personalizar el tamaño/posición
 */
@Composable
private fun AmazonStyleSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    onSearch: () -> Unit = {},
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color.White,
        shape = RoundedCornerShape(24.dp),
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono de búsqueda
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "Buscar",
                tint = Color(0xFF757575),
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            // Campo de texto
            Box(modifier = Modifier.weight(1f)) {
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .focusRequester(focusRequester),
                    decorationBox = { innerTextField ->
                        Box(modifier = Modifier.fillMaxWidth()) {
                            // Placeholder cuando está vacío
                            if (query.isEmpty()) {
                                Text(
                                    text = "Buscar productos, marcas y más",
                                    color = Color(0xFF9E9E9E),
                                    style = TextStyle(fontSize = 14.sp)
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }

            // Botón de limpiar (solo visible cuando hay texto)
            AnimatedVisibility(
                visible = query.isNotBlank(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                IconButton(
                    onClick = onClear,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = "Limpiar",
                        tint = Color(0xFF757575),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

/**
 * Grid de resultados de búsqueda
 * Muestra los productos encontrados en una cuadrícula adaptativa
 */
@Composable
private fun SearchResultsGrid(
    results: List<ProductUi>,
    onProductClick: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(Dimens.md),
        horizontalArrangement = Arrangement.spacedBy(Dimens.md),
        verticalArrangement = Arrangement.spacedBy(Dimens.md)
    ) {
        // Header con contador de resultados
        item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
            Text(
                text = "${results.size} resultados encontrados",
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.padding(vertical = Dimens.s)
            )
        }

        // Productos
        items(results, key = { it.id }) { product ->
            ProductCard(
                product = product,
                onClick = { onProductClick(product.id) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Footer con espacio adicional
        item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
            Spacer(modifier = Modifier.height(Dimens.xxl))
        }
    }
}

/**
 * Estado inicial de búsqueda (sin query)
 * Muestra un mensaje invitando al usuario a buscar
 */
@Composable
private fun EmptySearchState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(Dimens.xl)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
                tint = MaterialTheme.colors.onBackground.copy(alpha = 0.3f),
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(Dimens.lg))
            Text(
                text = "Busca tus productos favoritos",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onBackground,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(Dimens.s))
            Text(
                text = "Escribe en la barra de búsqueda para encontrar productos, marcas y más",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Estado sin resultados
 * Se muestra cuando la búsqueda no encuentra productos
 */
@Composable
private fun NoResultsState(query: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(Dimens.xl)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
                tint = MaterialTheme.colors.onBackground.copy(alpha = 0.3f),
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(Dimens.lg))
            Text(
                text = "No se encontraron resultados",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onBackground,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(Dimens.s))
            Text(
                text = "No hay productos que coincidan con \"$query\"",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(Dimens.md))
            Text(
                text = "Intenta con otras palabras clave",
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f),
                textAlign = TextAlign.Center
            )
        }
    }
}
