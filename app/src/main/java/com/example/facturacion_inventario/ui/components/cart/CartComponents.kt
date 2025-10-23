package com.example.facturacion_inventario.ui.components.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import com.example.facturacion_inventario.domain.model.Product

// Tokens
import com.example.facturacion_inventario.ui.theme.Dimens

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
 * CartSummary: muestra el total y botones de acción (pagar, limpiar)
 */
@Composable
fun CartSummary(total: Double, onCheckout: () -> Unit, onClear: () -> Unit, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth().padding(Dimens.s), elevation = 4.dp, shape = MaterialTheme.shapes.medium) {
        Column(modifier = Modifier.padding(Dimens.md)) {
            Text(text = "Total: S/ ${"%.2f".format(total)}", style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(Dimens.s))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = onCheckout) { Text(text = "Pagar") }
                OutlinedButton(onClick = onClear) { Text(text = "Vaciar carrito") }
            }
        }
    }
}

/**
 * EmptyCartView: vista mostrada cuando el carrito está vacío.
 */
@Composable
fun EmptyCartView(modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth().padding(Dimens.lg), elevation = 2.dp, shape = MaterialTheme.shapes.medium) {
        Column(modifier = Modifier.padding(Dimens.xl)) {
            Text(text = "Tu carrito está vacío", style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(Dimens.s))
            Text(text = "Agrega productos para verlos aquí.")
        }
    }
}
