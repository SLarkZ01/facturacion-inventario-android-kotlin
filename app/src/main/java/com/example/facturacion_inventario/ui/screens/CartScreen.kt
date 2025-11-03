package com.example.facturacion_inventario.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.facturacion_inventario.ui.store.StoreScreenScaffold
import com.example.facturacion_inventario.ui.theme.Dimens
import com.example.facturacion_inventario.ui.theme.AccentOrange
import com.example.facturacion_inventario.R
import androidx.compose.foundation.layout.Arrangement
import com.example.facturacion_inventario.ui.store.CartViewModel
import com.example.facturacion_inventario.ui.components.cart.CartItemCard
import com.example.facturacion_inventario.ui.components.cart.PriceSummaryCard
import com.example.facturacion_inventario.ui.components.cart.EmptyCartCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * CartContent optimizado con collectAsState para observar StateFlow.
 * Ahora incluye animaciones suaves al eliminar items.
 */
@Composable
fun CartContent(onContinueShopping: () -> Unit, onCheckout: () -> Unit, cartViewModel: CartViewModel? = null) {
    // Retornar temprano si cartViewModel es null
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
