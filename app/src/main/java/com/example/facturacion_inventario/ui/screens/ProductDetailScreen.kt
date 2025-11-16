package com.example.facturacion_inventario.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.facturacion_inventario.domain.model.Product
import com.example.facturacion_inventario.ui.components.product.ProductMediaCarousel
import com.example.facturacion_inventario.ui.components.product.ProductDetailsSection
import com.example.facturacion_inventario.ui.components.product.QuantitySelector
import com.example.facturacion_inventario.ui.theme.Dimens
import com.example.facturacion_inventario.ui.theme.AccentOrange
import com.example.facturacion_inventario.ui.theme.AmazonYellow
import com.example.facturacion_inventario.ui.store.StoreScreenScaffold
import com.example.facturacion_inventario.ui.store.CartViewModel
import com.example.facturacion_inventario.ui.store.RemoteCartViewModel
import com.example.facturacion_inventario.ui.store.RemoteCartViewModelFactory
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.ui.graphics.graphicsLayer
import com.example.facturacion_inventario.ui.components.badge.StockBadge
import com.example.facturacion_inventario.ui.components.badge.OutOfStockCard
import com.example.facturacion_inventario.ui.components.badge.PriceTag
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext

@Composable
fun ProductDetailContent(product: Product?, onAddToCart: () -> Unit, cartViewModel: RemoteCartViewModel? = null) {
    var selectedQuantity by remember { mutableStateOf(1) }
    var isAnimating by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isAnimating) 0.92f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        finishedListener = { isAnimating = false }
    )

    // 🔥 OPTIMIZACIÓN: Usar el RemoteCartViewModel pasado como parámetro o crear uno nuevo
    val context = LocalContext.current
    val remoteCartViewModel: RemoteCartViewModel = cartViewModel ?: viewModel(
        factory = RemoteCartViewModelFactory(
            context.applicationContext as android.app.Application
        )
    )

    // Observar estados del carrito remoto
    val carritoId by remoteCartViewModel.carritoId.collectAsState()
    val successMessage by remoteCartViewModel.successMessage.collectAsState()
    val errorMessage by remoteCartViewModel.errorMessage.collectAsState()

    // Snackbar para mostrar mensajes
    val scaffoldState = rememberScaffoldState()

    // Inicializar carrito al cargar la pantalla (solo si no hay carrito)
    LaunchedEffect(Unit) {
        if (carritoId == null) {
            remoteCartViewModel.obtenerOCrearCarrito(usuarioId = null)
        }
    }

    // Mostrar mensajes de éxito
    LaunchedEffect(successMessage) {
        successMessage?.let {
            scaffoldState.snackbarHostState.showSnackbar(it)
            remoteCartViewModel.clearSuccessMessage()
        }
    }

    // Mostrar mensajes de error
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            scaffoldState.snackbarHostState.showSnackbar("Error: $it")
            remoteCartViewModel.clearError()
        }
    }

    Scaffold(scaffoldState = scaffoldState) { padding ->
        StoreScreenScaffold {
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(padding)) {
                item {
                    Spacer(modifier = Modifier.height(96.dp))
                    Text(text = "Detalle del producto", color = MaterialTheme.colors.onBackground, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                }

                product?.let { prod ->
                    item {
                        Card(modifier = Modifier.fillMaxWidth(), elevation = 6.dp, shape = MaterialTheme.shapes.medium) {
                            ProductMediaCarousel(product = prod, modifier = Modifier.padding(Dimens.lg))
                        }
                        Spacer(modifier = Modifier.height(Dimens.xl))
                    }

                    item {
                        Card(modifier = Modifier.fillMaxWidth(), elevation = 6.dp, shape = MaterialTheme.shapes.medium) {
                            Column(modifier = Modifier.padding(Dimens.lg)) {
                                Text(text = "Precio", color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f), fontSize = 12.sp)
                                Spacer(modifier = Modifier.height(Dimens.xs))
                                // Usar componente reutilizable PriceTag
                                PriceTag(
                                    price = prod.price,
                                    currency = "S/",
                                    priceColor = AccentOrange,
                                    fontSize = 24
                                )

                                Spacer(modifier = Modifier.height(Dimens.lg))

                                // Usar componente reutilizable StockBadge
                                StockBadge(
                                    stock = prod.stock,
                                    lowStockThreshold = 10
                                )

                                Spacer(modifier = Modifier.height(Dimens.xl))
                                Divider()
                                Spacer(modifier = Modifier.height(Dimens.xl))

                                if (prod.stock > 0) {
                                    QuantitySelector(quantity = selectedQuantity, maxQuantity = prod.stock, onQuantityChange = { selectedQuantity = it }, modifier = Modifier.fillMaxWidth())

                                    Spacer(modifier = Modifier.height(Dimens.xl))

                                    // 🔥 MODIFICADO: Usar RemoteCartViewModel para agregar al backend
                                    Button(
                                        onClick = {
                                            isAnimating = true
                                            // Agregar al carrito del backend
                                            remoteCartViewModel.agregarProducto(
                                                productoId = prod.id,
                                                cantidad = selectedQuantity,
                                                precioUnitario = prod.price
                                            )
                                            onAddToCart()
                                        },
                                        modifier = Modifier.fillMaxWidth().height(Dimens.buttonHeight).graphicsLayer { scaleX = scale; scaleY = scale },
                                        colors = ButtonDefaults.buttonColors(backgroundColor = AmazonYellow),
                                        shape = MaterialTheme.shapes.medium
                                    ) {
                                        Text(text = "Agregar al carrito", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    }

                                    Spacer(modifier = Modifier.height(Dimens.md))

                                    /**
                                     * 🚧 BOTÓN PLACEHOLDER - IMPLEMENTACIÓN PENDIENTE
                                     *
                                     * PROPÓSITO ORIGINAL:
                                     * Permitir la compra directa de un producto sin agregarlo al carrito,
                                     * navegando directamente al checkout con ese único producto para
                                     * una experiencia de compra rápida (quick buy / buy now).
                                     *
                                     * ESTADO ACTUAL:
                                     * Placeholder sin implementación. El botón es visible y clickeable
                                     * pero no ejecuta ninguna acción (lambda vacía).
                                     *
                                     * RAZÓN DE MANTENERLO:
                                     * - Diseño de UX ya definido (botón visible en ProductDetailScreen)
                                     * - Feature planificada para implementación futura
                                     * - Evita tener que modificar la UI más adelante
                                     * - Patrón común en e-commerce (Amazon, MercadoLibre, etc.)
                                     *
                                     * CONDICIONES PARA IMPLEMENTACIÓN:
                                     * - Cuando se complete la pantalla de Checkout
                                     * - Cuando se implemente el flujo de pago/facturación inmediato
                                     * - Cuando se defina la lógica de "carrito temporal" para compra rápida
                                     * - Según priorización del backlog del proyecto
                                     *
                                     * IMPLEMENTACIÓN FUTURA:
                                     * Debe:
                                     * 1. Crear un carrito temporal o usar un flag "buyNow" en el carrito
                                     * 2. Navegar a la pantalla de checkout/facturación
                                     * 3. Pasar contexto de compra rápida (producto + cantidad)
                                     * 4. Saltar el paso de revisar carrito (ir directo a pago)
                                     *
                                     * @see CartViewModel para gestión del carrito
                                     */
                                    Button(
                                        onClick = {
                                            //  Implementar compra directa (buy now)
                                            // Ejemplo: navController.navigate("checkout?buyNow=true&productId=${prod.id}&quantity=$selectedQuantity")
                                        },
                                        modifier = Modifier.fillMaxWidth().height(Dimens.buttonHeight),
                                        colors = ButtonDefaults.buttonColors(backgroundColor = AccentOrange),
                                        shape = MaterialTheme.shapes.medium
                                    ) {
                                        Text(text = "Comprar ahora", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    }
                                } else {
                                    // Usar componente reutilizable OutOfStockCard
                                    OutOfStockCard()
                                }
                            }
                        }
                    }

                    item {
                        Card(modifier = Modifier.fillMaxWidth(), elevation = 6.dp, shape = MaterialTheme.shapes.medium) {
                            ProductDetailsSection(product = prod, modifier = Modifier.padding(Dimens.lg))
                        }
                        Spacer(modifier = Modifier.height(Dimens.xl))
                    }
                }

                item { Spacer(modifier = Modifier.height(Dimens.xl)) }
            }
        }
    }
}