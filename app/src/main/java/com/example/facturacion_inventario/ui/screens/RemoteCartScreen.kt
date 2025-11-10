package com.example.facturacion_inventario.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.facturacion_inventario.ui.components.cart.CartItemCard
import com.example.facturacion_inventario.ui.components.cart.EmptyCartCard
import com.example.facturacion_inventario.ui.components.cart.PriceSummaryCard
import com.example.facturacion_inventario.ui.store.RemoteCartViewModel
import com.example.facturacion_inventario.ui.theme.Dimens

/**
 * EJEMPLO DE USO: Pantalla que carga y muestra un carrito desde la API
 *
 * Esta pantalla demuestra:
 * - Cómo cargar un carrito por ID desde la API
 * - Manejo de estados (loading, error, success, empty)
 * - Uso de los componentes reutilizables de CartComponents
 * - Integración con RemoteCartViewModel
 *
 * CÓMO USAR EN NAVEGACIÓN:
 * ```kotlin
 * // En tu NavHost:
 * composable(
 *     route = "remote-cart/{carritoId}",
 *     arguments = listOf(navArgument("carritoId") { type = NavType.StringType })
 * ) { backStackEntry ->
 *     val carritoId = backStackEntry.arguments?.getString("carritoId") ?: ""
 *     RemoteCartScreen(carritoId = carritoId)
 * }
 *
 * // Para navegar:
 * navController.navigate("remote-cart/$carritoId")
 * ```
 */
@Composable
fun RemoteCartScreen(
    carritoId: String,
    viewModel: RemoteCartViewModel = viewModel()
) {
    // Observar estados del ViewModel
    val cartItems by viewModel.cartItems.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val totalPrice by viewModel.totalPrice.collectAsState()

    // Cargar el carrito cuando se monta la pantalla
    LaunchedEffect(carritoId) {
        viewModel.loadCarrito(carritoId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrito desde API") },
                backgroundColor = MaterialTheme.colors.primary
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                // Estado: Cargando
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // Estado: Error
                errorMessage != null -> {
                    ErrorContent(
                        message = errorMessage!!,
                        onRetry = { viewModel.loadCarrito(carritoId) },
                        onDismiss = { viewModel.clearError() },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // Estado: Carrito vacío
                cartItems.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(Dimens.lg),
                        verticalArrangement = Arrangement.Center
                    ) {
                        EmptyCartCard(
                            title = "Carrito vacío",
                            message = "Este carrito no tiene productos aún."
                        )
                    }
                }

                // Estado: Success - Mostrar items
                else -> {
                    CartContent(
                        cartItems = cartItems,
                        totalPrice = totalPrice,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

/**
 * Contenido principal del carrito con items y resumen
 */
@Composable
private fun CartContent(
    cartItems: List<com.example.facturacion_inventario.domain.model.CartItem>,
    totalPrice: Double,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Lista de items del carrito
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(Dimens.md),
            verticalArrangement = Arrangement.spacedBy(Dimens.md)
        ) {
            item {
                Text(
                    text = "Productos en el carrito",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = Dimens.s)
                )
            }

            items(cartItems) { cartItem ->
                CartItemCard(
                    cartItem = cartItem,
                    showRemoveButton = false // Solo lectura desde API
                )
            }
        }

        // Resumen de precios
        PriceSummaryCard(
            totalPrice = totalPrice,
            itemCount = cartItems.sumOf { it.quantity },
            modifier = Modifier.padding(Dimens.md)
        )
    }
}

/**
 * Componente para mostrar errores con opción de reintentar
 */
@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimens.lg),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(Dimens.lg),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "⚠️ Error",
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.error,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(Dimens.md))

            Text(
                text = message,
                style = MaterialTheme.typography.body1
            )

            Spacer(modifier = Modifier.height(Dimens.lg))

            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens.md)
            ) {
                OutlinedButton(onClick = onDismiss) {
                    Text("Cerrar")
                }
                Button(onClick = onRetry) {
                    Text("Reintentar")
                }
            }
        }
    }
}

// ════════════════════════════════════════════════════════════════════════
// EJEMPLO 2: Pantalla que lista todos los carritos de un usuario
// ════════════════════════════════════════════════════════════════════════

/**
 * Pantalla que lista todos los carritos de un usuario
 * Útil para que un usuario vea todos sus carritos activos
 */
@Composable
fun UserCarritosScreen(
    usuarioId: String,
    onCarritoClick: (String) -> Unit = {},
    viewModel: RemoteCartViewModel = viewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()

    // Cargar o crear carrito del usuario
    LaunchedEffect(usuarioId) {
        viewModel.obtenerOCrearCarrito(usuarioId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Carrito") },
                backgroundColor = MaterialTheme.colors.primary
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                errorMessage != null -> {
                    ErrorContent(
                        message = errorMessage!!,
                        onRetry = { viewModel.obtenerOCrearCarrito(usuarioId) },
                        onDismiss = { viewModel.clearError() },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                cartItems.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(Dimens.lg),
                        verticalArrangement = Arrangement.Center
                    ) {
                        EmptyCartCard(
                            title = "Carrito vacío",
                            message = "Este carrito no tiene productos aún.\nAgrega productos desde la tienda."
                        )
                    }
                }

                else -> {
                    // Mostrar el carrito cargado
                    CartContent(
                        cartItems = cartItems,
                        totalPrice = viewModel.totalPrice.collectAsState().value,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
