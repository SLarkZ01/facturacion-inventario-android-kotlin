package com.example.facturacion_inventario.ui.components.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.facturacion_inventario.domain.model.CartItem
import com.example.facturacion_inventario.domain.model.Product
import com.example.facturacion_inventario.ui.theme.Dimens
import java.text.NumberFormat
import java.util.Locale

/**
 * CartItemCard - Tarjeta para mostrar un item del carrito
 * Reutilizable en carrito, facturación, y resúmenes de pedido
 */
@Composable
fun CartItemCard(
    productName: String,
    productImageRes: Int,
    quantity: Int,
    price: Double,
    currency: String = "S/",
    modifier: Modifier = Modifier,
    onRemoveClick: (() -> Unit)? = null,
    showRemoveButton: Boolean = true,
    removeIconRes: Int? = null
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = 4.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(Dimens.lg),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = productImageRes),
                contentDescription = productName,
                modifier = Modifier.size(Dimens.imageLarge)
            )

            Spacer(modifier = Modifier.width(Dimens.md))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = productName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(Dimens.s))
                Text(
                    text = "Cantidad: $quantity",
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(Dimens.s))
                Text(
                    text = "$currency ${"%.2f".format(price)}",
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colors.primary
                )
            }

            if (showRemoveButton && onRemoveClick != null) {
                IconButton(onClick = onRemoveClick) {
                    Icon(
                        painter = painterResource(
                            id = removeIconRes ?: com.example.facturacion_inventario.R.drawable.ic_arrow_back
                        ),
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colors.error,
                        modifier = Modifier.size(Dimens.iconSize)
                    )
                }
            }
        }
    }
}

/**
 * CartItemCard (sobrecarga) - Acepta directamente el objeto CartItem del dominio
 */
@Composable
fun CartItemCard(
    cartItem: CartItem,
    modifier: Modifier = Modifier,
    onRemoveClick: (() -> Unit)? = null,
    showRemoveButton: Boolean = true,
    removeIconRes: Int? = null
) {
    CartItemCard(
        productName = cartItem.product.name,
        productImageRes = cartItem.product.imageRes,
        quantity = cartItem.quantity,
        price = cartItem.product.price,
        currency = "S/",
        modifier = modifier,
        onRemoveClick = onRemoveClick,
        showRemoveButton = showRemoveButton,
        removeIconRes = removeIconRes
    )
}

/**
 * CartItemRow: fila que muestra un producto en el carrito con controles de cantidad y eliminar.
 * Parámetros:
 *  - product: Product del dominio
 *  - quantity: cantidad actual
 *  - onQuantityChange: callback para actualizar cantidad
 *  - onRemove: callback para eliminar
 */
@Composable
fun CartItemRow(
    product: Product,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth().padding(Dimens.s), elevation = 2.dp, shape = MaterialTheme.shapes.medium) {
        Row(modifier = Modifier.padding(Dimens.md), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = product.name, style = MaterialTheme.typography.subtitle1)
                Spacer(modifier = Modifier.height(Dimens.xs))
                Text(text = "S/ ${"%.2f".format(product.price)}", style = MaterialTheme.typography.body2)
            }

            // Controles simples de cantidad
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { if (quantity > 1) onQuantityChange(quantity - 1) else onRemove() }) {
                    Text(text = "-")
                }
                Text(text = quantity.toString(), modifier = Modifier.padding(horizontal = Dimens.md))
                IconButton(onClick = { onQuantityChange(quantity + 1) }) {
                    Text(text = "+")
                }
            }
        }
    }
}

/**
 * PriceSummaryCard - Tarjeta de resumen de precios
 * Útil para carrito, checkout, y confirmación de pedidos
 */
@Composable
fun PriceSummaryCard(
    totalPrice: Double,
    itemCount: Int,
    currency: String = "S/",
    modifier: Modifier = Modifier,
    subtotal: Double? = null,
    tax: Double? = null,
    discount: Double? = null,
    backgroundColor: androidx.compose.ui.graphics.Color = MaterialTheme.colors.primary.copy(alpha = 0.08f)
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = 4.dp,
        backgroundColor = backgroundColor
    ) {
        Column(modifier = Modifier.padding(Dimens.lg)) {
            // Subtotal (si se proporciona)
            subtotal?.let {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Subtotal:", fontSize = 14.sp)
                    Text(
                        text = "$currency ${"%.2f".format(it)}",
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(Dimens.s))
            }

            // Descuento (si se proporciona)
            discount?.let {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Descuento:",
                        fontSize = 14.sp,
                        color = MaterialTheme.colors.secondary
                    )
                    Text(
                        text = "-$currency ${"%.2f".format(it)}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colors.secondary
                    )
                }
                Spacer(modifier = Modifier.height(Dimens.s))
            }

            // Impuesto (si se proporciona)
            tax?.let {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "IVA:", fontSize = 14.sp)
                    Text(
                        text = "$currency ${"%.2f".format(it)}",
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(Dimens.s))
            }

            if (subtotal != null || tax != null || discount != null) {
                Divider(modifier = Modifier.padding(vertical = Dimens.s))
            }

            // Total
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
                    text = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CO"))
                        .format(totalPrice),
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.height(Dimens.s))
            Text(
                text = "$itemCount producto(s) en total",
                fontSize = 12.sp,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

/**
 * EmptyCartCard - Tarjeta para mostrar cuando el carrito está vacío
 */
@Composable
fun EmptyCartCard(
    title: String = "Tu carrito está vacío",
    message: String = "Aquí verás los items seleccionados para facturar o mover en inventario.",
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth(), elevation = 4.dp) {
        Column(modifier = Modifier.padding(Dimens.lg)) {
            Text(
                text = title,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.9f),
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(Dimens.s))
            Text(
                text = message,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                fontSize = 13.sp
            )
        }
    }
}

// Previews
@Preview(showBackground = true)
@Composable
fun CartItemCardPreview() {
    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            CartItemCard(
                productName = "Filtro de aceite Honda",
                productImageRes = com.example.facturacion_inventario.R.drawable.ic_motorcycle_animated,
                quantity = 2,
                price = 45.50,
                onRemoveClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PriceSummaryCardPreview() {
    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            PriceSummaryCard(
                totalPrice = 150.75,
                itemCount = 3,
                subtotal = 140.0,
                tax = 12.75,
                discount = 2.0
            )
        }
    }
}
