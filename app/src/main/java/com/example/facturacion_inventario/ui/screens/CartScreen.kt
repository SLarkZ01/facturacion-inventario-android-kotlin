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
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.text.style.TextOverflow
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.foundation.layout.Arrangement
import com.example.facturacion_inventario.ui.store.CartViewModel

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
                    Card(modifier = Modifier.fillMaxWidth(), elevation = 4.dp) {
                        Column(modifier = Modifier.padding(Dimens.lg)) {
                            Text(text = "Tu carrito está vacío", color = MaterialTheme.colors.onSurface.copy(alpha = 0.9f))
                            Spacer(modifier = Modifier.height(Dimens.s))
                            Text(text = "Aquí verás los items seleccionados para facturar o mover en inventario.", color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f), fontSize = 13.sp)
                        }
                    }
                }

                // espacio para que la barra inferior no tape contenido
                item {
                    Spacer(modifier = Modifier.height(Dimens.xxl))
                }
            } else {
                items(cartViewModel.cartItems) { cartItem ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 4.dp,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Row(
                            modifier = Modifier.padding(Dimens.lg),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = cartItem.product.imageRes),
                                contentDescription = cartItem.product.name,
                                modifier = Modifier.size(Dimens.imageLarge)
                            )

                            Spacer(modifier = Modifier.width(Dimens.md))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = cartItem.product.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(Dimens.s))
                                Text(
                                    text = "Cantidad: ${cartItem.quantity}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                                )
                                Spacer(modifier = Modifier.height(Dimens.s))
                                Text(
                                    text = "S/ ${"%.2f".format(cartItem.product.price)}",
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colors.primary
                                )
                            }

                            IconButton(onClick = { cartViewModel.removeFromCart(cartItem.product.id) }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_arrow_back),
                                    contentDescription = "Eliminar",
                                    tint = MaterialTheme.colors.error,
                                    modifier = Modifier.size(Dimens.iconSize)
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(Dimens.lg))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 4.dp,
                        backgroundColor = AccentOrange.copy(alpha = 0.08f)
                    ) {
                        Column(modifier = Modifier.padding(Dimens.lg)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Total:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Text(
                                    text = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CO")).format(cartViewModel.getTotalPrice()),
                                    color = AccentOrange,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(Dimens.s))
                            Text(
                                text = "${cartViewModel.totalItemCount} producto(s) en total",
                                fontSize = 12.sp,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
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
