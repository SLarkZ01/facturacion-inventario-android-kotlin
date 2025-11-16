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
import com.example.facturacion_inventario.ui.store.RemoteCartViewModel
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.ui.graphics.graphicsLayer
import com.example.facturacion_inventario.ui.components.badge.PriceTag
import androidx.compose.ui.platform.LocalContext
import com.example.facturacion_inventario.ui.components.stock.StockBadge
import com.example.facturacion_inventario.ui.components.stock.StockDetailCard
import com.example.facturacion_inventario.ui.components.stock.StockLoadingSkeleton
import com.example.facturacion_inventario.ui.store.StockState
import com.example.facturacion_inventario.ui.store.StockViewModel

/**
 * Contenido de detalle de producto integrado con Stock en tiempo real
 * Reemplaza el stock estático del producto con el stock del backend
 */
@Composable
fun ProductDetailContentWithStock(
    product: Product,
    stockState: StockState,
    @Suppress("UNUSED_PARAMETER") onAddToCart: () -> Unit,
    cartViewModel: RemoteCartViewModel,
    stockViewModel: StockViewModel
) {
    var selectedQuantity by remember { mutableStateOf(1) }
    var isAnimating by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isAnimating) 0.92f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "buttonScale"
    )

    // Observar estados del carrito remoto
    val carritoId by cartViewModel.carritoId.collectAsState()
    val successMessage by cartViewModel.successMessage.collectAsState()
    val errorMessage by cartViewModel.errorMessage.collectAsState()

    // Inicializar carrito al cargar la pantalla
    LaunchedEffect(Unit) {
        if (carritoId == null) {
            cartViewModel.obtenerOCrearCarrito(usuarioId = null)
        }
    }

    // TO DO: Mostrar mensajes de éxito/error en un Snackbar cuando se implemente
    // Por ahora los mensajes se manejan internamente en el ViewModel

    // Obtener stock total actual
    // PRIORIDAD: Usar product.stock (que ya viene con totalStock del endpoint público)
    // FALLBACK: Si stockState tiene datos más recientes, usarlos
    val totalStock = when {
        // Si product.stock > 0, usarlo directamente (viene del endpoint público con totalStock)
        product.stock > 0 -> product.stock
        // Si no, intentar usar stockState como fallback
        stockState is StockState.Success -> stockState.total
        else -> 0
    }

    // Validar que hay stock disponible
    val hasStock = totalStock > 0

    Box {
        StoreScreenScaffold {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {

    Scaffold(scaffoldState = scaffoldState) { padding ->
        StoreScreenScaffold {
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(padding)) {
                item {
                    Spacer(modifier = Modifier.height(96.dp))
                    Text(
                        text = "Detalle del producto",
                        color = MaterialTheme.colors.onBackground,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 6.dp,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        ProductMediaCarousel(
                            product = product,
                            modifier = Modifier.padding(Dimens.lg)
                        )
                    }
                    Spacer(modifier = Modifier.height(Dimens.xl))
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 6.dp,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Column(modifier = Modifier.padding(Dimens.lg)) {
                            Text(
                                text = "Precio",
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(Dimens.xs))
                            PriceTag(
                                price = product.price,
                                currency = "S/",
                                priceColor = AccentOrange,
                                fontSize = 24
                            )

                            Spacer(modifier = Modifier.height(Dimens.lg))

                            // 🔥 NUEVO: Stock desde el endpoint público (ya incluye totalStock)
                            Text(
                                text = "Disponibilidad",
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(Dimens.xs))

                            // Mostrar stock desde product.stock (que ya tiene totalStock)
                            StockBadge(total = totalStock)

                            // Mostrar desglose por almacén si está disponible (StockState)
                            if (stockState is StockState.Success && stockState.stockByAlmacen.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(Dimens.md))
                                StockDetailCard(
                                    stockState = stockState,
                                    totalOverride = totalStock,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            Spacer(modifier = Modifier.height(Dimens.xl))
                            Divider()
                            Spacer(modifier = Modifier.height(Dimens.xl))

                            // 🔥 Validación de stock para habilitar/deshabilitar botones
                            if (hasStock) {
                                QuantitySelector(
                                    quantity = selectedQuantity,
                                    maxQuantity = totalStock,
                                    onQuantityChange = { selectedQuantity = it },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(Dimens.xl))

                                // Botón: Agregar al carrito
                                Button(
                                    onClick = {
                                        isAnimating = true
                                        cartViewModel.agregarProducto(
                                            productoId = product.id,
                                            cantidad = selectedQuantity
                                        )
                                        onAddToCart()
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(Dimens.buttonHeight)
                                        .graphicsLayer { scaleX = scale; scaleY = scale },
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = AmazonYellow
                                    ),
                                    shape = MaterialTheme.shapes.medium,
                                    enabled = hasStock // Solo habilitado si hay stock
                                ) {
                                    Text(
                                        text = "Agregar al carrito",
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(Dimens.md))

                                // Botón: Comprar ahora (placeholder)
                                Button(
                                    onClick = {
                                        // TO DO: Implementar compra directa
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(Dimens.buttonHeight),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = AccentOrange
                                    ),
                                    shape = MaterialTheme.shapes.medium,
                                    enabled = hasStock // Solo habilitado si hay stock
                                ) {
                                    Text(
                                        text = "Comprar ahora",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                }
                            } else {
                                // Sin stock disponible
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    backgroundColor = MaterialTheme.colors.error.copy(alpha = 0.1f),
                                    elevation = 0.dp
                                ) {
                                    Column(
                                        modifier = Modifier.padding(Dimens.lg),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "🔴 Producto sin stock",
                                            color = MaterialTheme.colors.error,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        )
                                        Spacer(modifier = Modifier.height(Dimens.xs))
                                        Text(
                                            text = "Este producto no está disponible actualmente. Te notificaremos cuando vuelva a estar en stock.",
                                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 🔥 NUEVO: Card con desglose de stock por almacén
                if (stockState is StockState.Success) {
                    item {
                        Spacer(modifier = Modifier.height(Dimens.lg))
                        StockDetailCard(stockState = stockState, totalOverride = totalStock)
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(Dimens.xl))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 6.dp,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        ProductDetailsSection(
                            product = product,
                            modifier = Modifier.padding(Dimens.lg)
                        )
                    }
                    Spacer(modifier = Modifier.height(Dimens.xl))
                }

                item { Spacer(modifier = Modifier.height(Dimens.xl)) }
            }
        }
    }
}
