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
import com.example.facturacion_inventario.ui.components.badge.PriceTag
import android.util.Log
import com.example.facturacion_inventario.ui.components.stock.StockBadge
import com.example.facturacion_inventario.ui.components.stock.StockDetailCard
import kotlin.math.max
import com.example.facturacion_inventario.ui.store.StockState
import com.example.facturacion_inventario.ui.store.StockViewModel
import androidx.compose.ui.graphics.graphicsLayer

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
    val TAG = "ProductDetailContentWithStock"
    var selectedQuantity by remember { mutableStateOf(1) }
    // Simplificamos la animación por ahora para evitar warnings; escala fija
    val scale = 1f

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
    // PRIORIDAD: Usar stockState (datos en tiempo real) si está disponible
    // FALLBACK: Usar product.stock (mapeado desde el endpoint público)
    val totalStock = when (stockState) {
        is StockState.Success -> max(stockState.total, product.stock)
        else -> product.stock
    }

    // Loggear para diagnóstico en runtime
    LaunchedEffect(totalStock, stockState) {
        Log.d(TAG, "Computed totalStock=$totalStock from product.stock=${product.stock} and stockState=$stockState")
    }

    // Asegurar uso de stockViewModel para evitar warning y solicitar carga si está en Loading
    LaunchedEffect(product.id) {
        if (stockState is StockState.Loading) {
            Log.d(TAG, "Triggering stockViewModel.loadStock for product=${product.id}")
            stockViewModel.loadStock(product.id)
        }
    }

    // Validar que hay stock disponible
    val hasStock = totalStock > 0

    // START: Reemplazo de layout para corregir llaves desbalanceadas
    val scaffoldState = rememberScaffoldState()

    // Mostrar mensajes de éxito/error del RemoteCartViewModel
    LaunchedEffect(successMessage) {
        successMessage?.let {
            scaffoldState.snackbarHostState.showSnackbar(it)
            cartViewModel.clearSuccessMessage()
        }
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            scaffoldState.snackbarHostState.showSnackbar("Error: $it")
            cartViewModel.clearError()
        }
    }

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

                // Media carousel
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

                // Precio y stock
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

                            Text(
                                text = "Disponibilidad",
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(Dimens.xs))

                            StockBadge(total = totalStock)

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

                            if (hasStock) {
                                QuantitySelector(
                                    quantity = selectedQuantity,
                                    maxQuantity = totalStock,
                                    onQuantityChange = { selectedQuantity = it },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(Dimens.xl))

                                Button(
                                    onClick = {
                                        // Animación y agregar al carrito remoto
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
                                    enabled = hasStock
                                ) {
                                    Text(
                                        text = "Agregar al carrito",
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(Dimens.md))

                                Button(
                                    onClick = { /* Compra directa - placeholder */ },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(Dimens.buttonHeight),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = AccentOrange
                                    ),
                                    shape = MaterialTheme.shapes.medium,
                                    enabled = hasStock
                                ) {
                                    Text(
                                        text = "Comprar ahora",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                }
                            } else {
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

                // Desglose de stock
                if (stockState is StockState.Success) {
                    item {
                        Spacer(modifier = Modifier.height(Dimens.lg))
                        StockDetailCard(stockState = stockState, totalOverride = totalStock)
                    }
                }

                // Detalles del producto
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
    // END: Reemplazo de layout
}
