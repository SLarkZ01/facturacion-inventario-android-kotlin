package com.example.facturacion_inventario.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.facturacion_inventario.ui.store.StoreScreenScaffold
import com.example.facturacion_inventario.ui.theme.Dimens
import com.example.facturacion_inventario.ui.theme.AccentOrange
import com.example.facturacion_inventario.R
import androidx.compose.foundation.layout.Arrangement
import com.example.facturacion_inventario.ui.store.CartViewModel
import com.example.facturacion_inventario.ui.store.RemoteCartViewModel
import com.example.facturacion_inventario.ui.store.RemoteCartViewModelFactory
import com.example.facturacion_inventario.ui.components.cart.CartItemCard
import com.example.facturacion_inventario.ui.components.cart.PriceSummaryCard
import com.example.facturacion_inventario.ui.components.cart.EmptyCartCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * CartContent con soporte para carrito del backend.
 * Puede usar CartViewModel (local) o RemoteCartViewModel (backend).
 *
 * Para usar carrito del backend, pasa carritoId.
 */
@Composable
fun CartContent(
    onContinueShopping: () -> Unit,
    onCheckout: () -> Unit,
    cartViewModel: CartViewModel? = null,
    carritoId: String? = null // Nuevo parámetro para cargar del backend
) {
    // Si hay carritoId, usar RemoteCartViewModel
    if (carritoId != null) {
        RemoteCartContent(
            carritoId = carritoId,
            onContinueShopping = onContinueShopping,
            onCheckout = onCheckout
        )
        return
    }

    // Modo local original
    if (cartViewModel == null) return

    // OPTIMIZACIÓN: Observar StateFlow con collectAsState
    val cartItems by cartViewModel.cartItems.collectAsState()
    val totalItemCount by cartViewModel.totalItemCount.collectAsState()
    val totalPrice by cartViewModel.totalPrice.collectAsState()

    // Estado para controlar items que están siendo eliminados
    var itemsBeingRemoved by remember { mutableStateOf<Set<String>>(emptySet()) }
    val coroutineScope = rememberCoroutineScope()

    StoreScreenScaffold {
        // Pongo el encabezado dentro del LazyColumn para que se desplace con el contenido
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Dimens.md),
            contentPadding = PaddingValues(Dimens.lg)
        ) {
            item(key = "header") {
                Spacer(modifier = Modifier.height(32.dp))
                Text(text = "Carrito / Documentos", color = MaterialTheme.colors.onBackground, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(Dimens.md))
            }

            if (cartItems.isEmpty()) {
                item(key = "empty_cart") {
                    // Usar componente reutilizable EmptyCartCard
                    EmptyCartCard()
                }

                // espacio para que la barra inferior no tape contenido
                item(key = "empty_spacer") {
                    Spacer(modifier = Modifier.height(Dimens.xxl))
                }
            } else {
                // KEY CRÍTICO: usar cartItem.product.id para identificar items únicos
                items(cartItems, key = { it.product.id }) { cartItem ->
                    // AnimatedVisibility para animación de salida suave
                    AnimatedVisibility(
                        visible = !itemsBeingRemoved.contains(cartItem.product.id),
                        enter = expandVertically(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        ) + fadeIn(animationSpec = tween(300)),
                        exit = shrinkVertically(
                            animationSpec = tween(
                                durationMillis = 400,
                                easing = FastOutSlowInEasing
                            )
                        ) + fadeOut(
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = LinearEasing
                            )
                        ) + slideOutHorizontally(
                            targetOffsetX = { fullWidth -> -fullWidth },
                            animationSpec = tween(
                                durationMillis = 400,
                                easing = FastOutSlowInEasing
                            )
                        )
                    ) {
                        // Usar componente reutilizable CartItemCard
                        CartItemCard(
                            cartItem = cartItem,
                            onRemoveClick = {
                                // Marcar como "siendo eliminado" para iniciar animación
                                itemsBeingRemoved = itemsBeingRemoved + cartItem.product.id

                                // Esperar a que termine la animación antes de eliminar del ViewModel
                                coroutineScope.launch {
                                    delay(400) // Duración de la animación de salida
                                    cartViewModel.removeFromCart(cartItem.product.id)
                                    itemsBeingRemoved = itemsBeingRemoved - cartItem.product.id
                                }
                            },
                            removeIconRes = R.drawable.ic_arrow_back
                        )
                    }
                }

                item(key = "summary") {
                    Spacer(modifier = Modifier.height(Dimens.lg))
                    // Usar componente reutilizable PriceSummaryCard
                    PriceSummaryCard(
                        totalPrice = totalPrice,
                        itemCount = totalItemCount,
                        backgroundColor = AccentOrange.copy(alpha = 0.08f)
                    )
                }

                // espacio para que la barra inferior no tape contenido
                item(key = "bottom_spacer") {
                    Spacer(modifier = Modifier.height(Dimens.xxl))
                }
            }
        }

        // Barra fija en la parte inferior con botones (diseño similar al de la pantalla principal)
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            elevation = 8.dp,
            color = MaterialTheme.colors.background
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens.md),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.lg)
            ) {
                OutlinedButton(onClick = onContinueShopping, modifier = Modifier.weight(1f).height(Dimens.buttonHeight)) {
                    Text(text = "Seguir comprando", color = MaterialTheme.colors.primary)
                }
                Button(
                    onClick = onCheckout,
                    modifier = Modifier.weight(1f).height(Dimens.buttonHeight),
                    enabled = cartItems.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                ) {
                    Text(text = "Generar factura", color = MaterialTheme.colors.onPrimary)
                }
            }
        }
    }
}

/**
 * Contenido del carrito usando RemoteCartViewModel (carrito del backend)
 * MODO DINÁMICO: Carga o crea automáticamente el carrito del usuario
 */
@Composable
private fun RemoteCartContent(
    carritoId: String,
    onContinueShopping: () -> Unit,
    onCheckout: () -> Unit
) {
    // ARREGLADO: Crear ViewModel con Factory personalizado
    val context = androidx.compose.ui.platform.LocalContext.current
    val viewModel: RemoteCartViewModel = viewModel(
        factory = RemoteCartViewModelFactory(
            context.applicationContext as android.app.Application
        )
    )

    // Observar estados
    val cartItems by viewModel.cartItems.collectAsState()
    val totalPrice by viewModel.totalPrice.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()

    // NUEVO: Modo dinámico - obtiene o crea carrito automáticamente
    LaunchedEffect(Unit) {
        if (carritoId == "DYNAMIC") {
            // Modo dinámico: obtener o crear carrito del usuario
            viewModel.obtenerOCrearCarrito(usuarioId = null) // TO DO: obtener del usuario autenticado
        } else {
            // Modo específico: cargar un carrito por ID
            viewModel.loadCarrito(carritoId)
        }
    }

    // Snackbar state
    val snackbarHostState = remember { SnackbarHostState() }

    // Mostrar mensajes
    LaunchedEffect(successMessage) {
        successMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSuccessMessage()
        }
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar("Error: $it")
            viewModel.clearError()
        }
    }

    StoreScreenScaffold {
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                // Estado: Cargando
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // Estado: Carrito cargado
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(Dimens.md),
                        contentPadding = PaddingValues(Dimens.lg)
                    ) {
                        item(key = "header") {
                            Spacer(modifier = Modifier.height(32.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Carrito / Documentos",
                                    color = MaterialTheme.colors.onBackground,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "🌐 Backend",
                                    color = MaterialTheme.colors.primary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Spacer(modifier = Modifier.height(Dimens.md))
                        }

                        if (cartItems.isEmpty()) {
                            item(key = "empty_cart") {
                                EmptyCartCard(
                                    title = "Carrito vacío",
                                    message = "Este carrito no tiene productos aún.\nAgrega productos desde la tienda."
                                )
                            }
                            item(key = "empty_spacer") {
                                Spacer(modifier = Modifier.height(Dimens.xxl))
                            }
                        } else {
                            items(cartItems, key = { it.product.id }) { cartItem ->
                                CartItemCard(
                                    cartItem = cartItem,
                                    onRemoveClick = {
                                        viewModel.removerProducto(cartItem.product.id)
                                    },
                                    removeIconRes = R.drawable.ic_arrow_back
                                )
                            }

                            item(key = "summary") {
                                Spacer(modifier = Modifier.height(Dimens.lg))
                                PriceSummaryCard(
                                    totalPrice = totalPrice,
                                    itemCount = cartItems.sumOf { it.quantity },
                                    backgroundColor = AccentOrange.copy(alpha = 0.08f)
                                )
                            }

                            item(key = "bottom_spacer") {
                                Spacer(modifier = Modifier.height(Dimens.xxl))
                            }
                        }
                    }

                    // Botones inferiores
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
                                onClick = onContinueShopping,
                                modifier = Modifier.weight(1f).height(Dimens.buttonHeight)
                            ) {
                                Text(text = "Seguir comprando", color = MaterialTheme.colors.primary)
                            }
                            Button(
                                onClick = onCheckout,
                                modifier = Modifier.weight(1f).height(Dimens.buttonHeight),
                                enabled = cartItems.isNotEmpty(),
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
}
