package com.example.facturacion_inventario.ui.store

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.facturacion_inventario.R
import com.example.facturacion_inventario.domain.model.Category
import com.example.facturacion_inventario.ui.components.cart.CartItemCard
import com.example.facturacion_inventario.ui.components.cart.EmptyCartCard
import com.example.facturacion_inventario.ui.components.cart.PriceSummaryCard
import com.example.facturacion_inventario.ui.components.factura.CheckoutDialog
import com.example.facturacion_inventario.ui.screens.HomeScreenRemote
import com.example.facturacion_inventario.ui.screens.ProductDetailScreenRemote
import com.example.facturacion_inventario.ui.screens.CategoriesScreenRemote
import com.example.facturacion_inventario.ui.components.category.CategoryCard
import com.example.facturacion_inventario.ui.theme.Dimens
import com.example.facturacion_inventario.ui.theme.AccentOrange
import kotlinx.coroutines.launch

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
fun ProductDetailScreen(productId: String?, cartViewModel: RemoteCartViewModel) {
    if (productId != null) {
        ProductDetailScreenRemote(
            productId = productId,
            cartViewModel = cartViewModel
        )
    }
}

@Composable
fun CartScreen(navController: NavController, cartViewModel: RemoteCartViewModel = viewModel()) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val totalPrice by cartViewModel.totalPrice.collectAsState()
    val totalItemCount by cartViewModel.totalItemCount.collectAsState()
    val isLoading by cartViewModel.isLoading.collectAsState()
    val errorMessage by cartViewModel.errorMessage.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    // Estado para controlar el diálogo de checkout
    var showCheckoutDialog by remember { mutableStateOf(false) }
    var carritoIdParaCheckout by remember { mutableStateOf<String?>(null) }

    // Cargar el carrito actual al montar la pantalla
    LaunchedEffect(Unit) {
        cartViewModel.cargarCarritoActual()
    }

    // Mostrar el diálogo de checkout cuando se presiona el botón
    if (showCheckoutDialog && carritoIdParaCheckout != null) {
        CheckoutDialog(
            carritoId = carritoIdParaCheckout!!,
            onDismiss = {
                showCheckoutDialog = false
                carritoIdParaCheckout = null
            },
            onSuccess = { _ ->
                showCheckoutDialog = false
                carritoIdParaCheckout = null

                // 🔥 SOLUCIÓN: Limpiar carrito inmediatamente y crear uno nuevo
                // Esto actualiza la UI al instante sin necesidad de cambiar de vista
                cartViewModel.limpiarYCrearNuevoCarrito()

                // Mostrar mensaje de éxito
                coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = "✓ Factura creada exitosamente",
                        duration = SnackbarDuration.Long
                    )
                }
            }
        )
    }

    Scaffold(scaffoldState = scaffoldState) { paddingValues ->
        StoreScreenScaffold {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Indicador de carga
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // Contenido del carrito
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(Dimens.md),
                    contentPadding = PaddingValues(Dimens.lg)
                ) {
                    item(key = "header") {
                        Spacer(modifier = Modifier.height(32.dp))
                        Text(
                            text = "Carrito / Documentos",
                            color = MaterialTheme.colors.onBackground,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(Dimens.md))
                    }

                    // Mostrar error si existe
                    errorMessage?.let { error ->
                        item(key = "error") {
                            Card(
                                backgroundColor = MaterialTheme.colors.error.copy(alpha = 0.1f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = error,
                                    color = MaterialTheme.colors.error,
                                    modifier = Modifier.padding(Dimens.md)
                                )
                            }
                        }
                    }

                    if (cartItems.isEmpty() && !isLoading) {
                        item(key = "empty_cart") {
                            EmptyCartCard()
                        }
                        item(key = "empty_spacer") {
                            Spacer(modifier = Modifier.height(Dimens.xxl))
                        }
                    } else {
                        items(cartItems, key = { it.product.id }) { cartItem ->
                            CartItemCard(
                                cartItem = cartItem,
                                onRemoveClick = {
                                    // Eliminar directamente del backend
                                    coroutineScope.launch {
                                        cartViewModel.removerProducto(cartItem.product.id)
                                    }
                                },
                                removeIconRes = R.drawable.ic_arrow_back
                            )
                        }

                        item(key = "summary") {
                            Spacer(modifier = Modifier.height(Dimens.lg))
                            PriceSummaryCard(
                                totalPrice = totalPrice,
                                itemCount = totalItemCount,
                                backgroundColor = AccentOrange.copy(alpha = 0.08f)
                            )
                        }

                        item(key = "bottom_spacer") {
                            Spacer(modifier = Modifier.height(Dimens.xxl))
                        }
                    }
                }

                // Barra fija en la parte inferior con botones
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    elevation = 8.dp,
                    color = MaterialTheme.colors.background
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Dimens.md),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Dimens.lg)
                    ) {
                        OutlinedButton(
                            onClick = { navController.navigate(Routes.HOME) },
                            modifier = Modifier.weight(1f).height(Dimens.buttonHeight)
                        ) {
                            Text(text = "Seguir comprando", color = MaterialTheme.colors.primary)
                        }
                        Button(
                            onClick = {
                                // Obtener el ID del carrito actual y abrir diálogo
                                val carritoId = cartViewModel.carritoId.value
                                if (carritoId != null && cartItems.isNotEmpty()) {
                                    carritoIdParaCheckout = carritoId
                                    showCheckoutDialog = true
                                } else if (cartItems.isEmpty()) {
                                    coroutineScope.launch {
                                        scaffoldState.snackbarHostState.showSnackbar(
                                            "El carrito está vacío"
                                        )
                                    }
                                } else {
                                    coroutineScope.launch {
                                        scaffoldState.snackbarHostState.showSnackbar(
                                            "Error: No se pudo obtener el ID del carrito"
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f).height(Dimens.buttonHeight),
                            enabled = cartItems.isNotEmpty() && !isLoading,
                            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                        ) {
                            Text(text = "Generar factura", color = MaterialTheme.colors.onPrimary)
                        }
                    }
                }
            }
        }
    }
}

/**
 * CategoriesContent optimizado con keys para mejor rendimiento.
 */
@Suppress("unused", "UNUSED_PARAMETER")
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
