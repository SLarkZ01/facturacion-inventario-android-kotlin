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
import com.example.facturacion_inventario.ui.components.ProductMediaCarousel
import com.example.facturacion_inventario.ui.components.ProductDetailsSection
import com.example.facturacion_inventario.ui.components.QuantitySelector
import com.example.facturacion_inventario.ui.theme.Dimens
import com.example.facturacion_inventario.ui.theme.AccentOrange
import com.example.facturacion_inventario.ui.theme.AmazonYellow
import com.example.facturacion_inventario.ui.theme.SuccessGreen
import com.example.facturacion_inventario.ui.store.StoreScreenScaffold
import com.example.facturacion_inventario.ui.store.CartViewModel
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun ProductDetailContent(product: Product?, onAddToCart: () -> Unit, cartViewModel: CartViewModel? = null) {
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

    StoreScreenScaffold {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
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
                            Text(text = prod.name, color = MaterialTheme.colors.onBackground, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Spacer(modifier = Modifier.height(Dimens.s))
                            Text(text = prod.description, color = MaterialTheme.colors.onSurface.copy(alpha = 0.9f), fontSize = 14.sp)

                            Spacer(modifier = Modifier.height(Dimens.lg))
                            Divider()
                            Spacer(modifier = Modifier.height(Dimens.lg))

                            Text(text = "Precio", color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f), fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(Dimens.xs))
                            Text(text = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CO")).format(prod.price), color = AccentOrange, fontWeight = FontWeight.Bold, fontSize = 24.sp)

                            Spacer(modifier = Modifier.height(Dimens.lg))

                            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                                Text(text = if (prod.stock > 0) "En stock" else "Sin stock", color = if (prod.stock > 0) SuccessGreen else MaterialTheme.colors.error, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                if (prod.stock > 0 && prod.stock <= 10) {
                                    Spacer(modifier = Modifier.width(Dimens.s))
                                    Text(text = "¡Solo quedan ${prod.stock}!", color = MaterialTheme.colors.error, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }

                            Spacer(modifier = Modifier.height(Dimens.xl))
                            Divider()
                            Spacer(modifier = Modifier.height(Dimens.xl))

                            if (prod.stock > 0) {
                                QuantitySelector(quantity = selectedQuantity, maxQuantity = prod.stock, onQuantityChange = { selectedQuantity = it }, modifier = Modifier.fillMaxWidth())

                                Spacer(modifier = Modifier.height(Dimens.xl))

                                Button(onClick = { isAnimating = true; cartViewModel?.addToCart(prod, selectedQuantity); onAddToCart() }, modifier = Modifier.fillMaxWidth().height(Dimens.buttonHeight).graphicsLayer { scaleX = scale; scaleY = scale }, colors = ButtonDefaults.buttonColors(backgroundColor = AmazonYellow), shape = MaterialTheme.shapes.medium) {
                                    Text(text = "Agregar al carrito", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                }

                                Spacer(modifier = Modifier.height(Dimens.md))

                                Button(onClick = { /* comprar ahora */ }, modifier = Modifier.fillMaxWidth().height(Dimens.buttonHeight), colors = ButtonDefaults.buttonColors(backgroundColor = AccentOrange), shape = MaterialTheme.shapes.medium) {
                                    Text(text = "Comprar ahora", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                }
                            } else {
                                Card(backgroundColor = MaterialTheme.colors.error.copy(alpha = 0.1f), elevation = 0.dp, shape = MaterialTheme.shapes.medium) {
                                    Column(modifier = Modifier.padding(Dimens.lg), horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                                        Text(text = "Producto agotado", color = MaterialTheme.colors.error, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                        Spacer(modifier = Modifier.height(Dimens.s))
                                        Text(text = "Este producto no está disponible actualmente", color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f), fontSize = 13.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                                    }
                                }
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
