package com.example.facturacion_inventario.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.facturacion_inventario.ui.components.CartItemCard
import com.example.facturacion_inventario.ui.components.PriceSummaryCard
import com.example.facturacion_inventario.ui.components.EmptyCartCard

/**
 * CartContent optimizado con collectAsState para observar StateFlow.
 */
@Composable
fun CartContent(onContinueShopping: () -> Unit, onCheckout: () -> Unit, cartViewModel: CartViewModel? = null) {
    // Retornar temprano si cartViewModel es null
    if (cartViewModel == null) return

    // OPTIMIZACIÓN: Observar StateFlow con collectAsState
    val cartItems by cartViewModel.cartItems.collectAsState()
    val totalItemCount by cartViewModel.totalItemCount.collectAsState()
    val totalPrice by cartViewModel.totalPrice.collectAsState()

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
                    // Usar componente reutilizable CartItemCard
                    CartItemCard(
                        cartItem = cartItem,
                        onRemoveClick = { cartViewModel.removeFromCart(cartItem.product.id) },
                        removeIconRes = R.drawable.ic_arrow_back
                    )
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
