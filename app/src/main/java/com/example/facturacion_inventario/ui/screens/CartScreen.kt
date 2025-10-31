package com.example.facturacion_inventario.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
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

@Composable
fun CartContent(onContinueShopping: () -> Unit, onCheckout: () -> Unit, cartViewModel: CartViewModel? = null) {
    StoreScreenScaffold {
        // Pongo el encabezado dentro del LazyColumn para que se desplace con el contenido
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Dimens.md),
            contentPadding = PaddingValues(Dimens.lg)
        ) {
            item {
                Spacer(modifier = Modifier.height(32.dp))
                Text(text = "Carrito / Documentos", color = MaterialTheme.colors.onBackground, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(Dimens.md))
            }

            if (cartViewModel == null || cartViewModel.cartItems.isEmpty()) {
                item {
                    // Usar componente reutilizable EmptyCartCard
                    EmptyCartCard()
                }

                // espacio para que la barra inferior no tape contenido
                item {
                    Spacer(modifier = Modifier.height(Dimens.xxl))
                }
            } else {
                items(cartViewModel.cartItems) { cartItem ->
                    // Usar componente reutilizable CartItemCard
                    CartItemCard(
                        cartItem = cartItem,
                        onRemoveClick = { cartViewModel.removeFromCart(cartItem.product.id) },
                        removeIconRes = R.drawable.ic_arrow_back
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(Dimens.lg))
                    // Usar componente reutilizable PriceSummaryCard
                    PriceSummaryCard(
                        totalPrice = cartViewModel.getTotalPrice(),
                        itemCount = cartViewModel.totalItemCount,
                        backgroundColor = AccentOrange.copy(alpha = 0.08f)
                    )
                }

                // espacio para que la barra inferior no tape contenido
                item {
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
                    enabled = cartViewModel?.cartItems?.isNotEmpty() == true,
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                ) {
                    Text(text = "Generar factura", color = MaterialTheme.colors.onPrimary)
                }
            }
        }
    }
}
