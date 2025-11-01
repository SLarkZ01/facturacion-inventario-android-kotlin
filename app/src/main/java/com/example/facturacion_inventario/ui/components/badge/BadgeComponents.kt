package com.example.facturacion_inventario.ui.components.badge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.facturacion_inventario.ui.theme.Dimens
import com.example.facturacion_inventario.ui.theme.SuccessGreen

/**
 * StockBadge - Badge para mostrar el estado de stock de un producto
 * Muestra diferentes estilos según disponibilidad
 */
@Composable
fun StockBadge(
    stock: Int,
    modifier: Modifier = Modifier,
    inStockText: String = "En stock",
    outOfStockText: String = "Sin stock",
    lowStockThreshold: Int = 10,
    lowStockText: String = "¡Solo quedan %d!"
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = if (stock > 0) inStockText else outOfStockText,
            color = if (stock > 0) SuccessGreen else MaterialTheme.colors.error,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        if (stock in 1..lowStockThreshold) {
            Spacer(modifier = Modifier.width(Dimens.s))
            Text(
                text = lowStockText.format(stock),
                color = MaterialTheme.colors.error,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/**
 * StatusBadge - Badge genérico para mostrar estados
 * Útil para pedidos, pagos, envíos, etc.
 */
@Composable
fun StatusBadge(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.primary.copy(alpha = 0.12f),
    textColor: Color = MaterialTheme.colors.primary,
    fontSize: Int = 12
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = fontSize.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

/**
 * DiscountBadge - Badge para mostrar descuentos o promociones
 */
@Composable
fun DiscountBadge(
    discountPercentage: Int,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.secondary,
    textColor: Color = Color.White
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(4.dp),
        color = backgroundColor
    ) {
        Text(
            text = "-$discountPercentage%",
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

/**
 * OutOfStockOverlay - Overlay semi-transparente para productos agotados
 * Se puede superponer sobre tarjetas de productos
 */
@Suppress("unused") // Componente útil para estados de productos
@Composable
fun OutOfStockOverlay(
    modifier: Modifier = Modifier,
    message: String = "Producto agotado"
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            backgroundColor = MaterialTheme.colors.error.copy(alpha = 0.9f),
            elevation = 4.dp,
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = message,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * NewBadge - Badge para productos nuevos
 */
@Composable
fun NewBadge(
    modifier: Modifier = Modifier,
    text: String = "NUEVO"
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(4.dp),
        color = SuccessGreen
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
        )
    }
}

/**
 * OutOfStockCard - Tarjeta informativa para productos sin stock
 * Útil en pantallas de detalle
 */
@Composable
fun OutOfStockCard(
    modifier: Modifier = Modifier,
    title: String = "Producto agotado",
    message: String = "Este producto no está disponible actualmente"
) {
    Card(
        backgroundColor = MaterialTheme.colors.error.copy(alpha = 0.1f),
        elevation = 0.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(Dimens.lg),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color = MaterialTheme.colors.error,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(Dimens.s))
            Text(
                text = message,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * PriceTag - Componente para mostrar precios con formato consistente
 */
@Composable
fun PriceTag(
    price: Double,
    currency: String = "S/",
    modifier: Modifier = Modifier,
    oldPrice: Double? = null,
    priceColor: Color = MaterialTheme.colors.primary,
    fontSize: Int = 18
) {
    Column(modifier = modifier) {
        Text(
            text = "$currency ${"%.2f".format(price)}",
            color = priceColor,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize.sp
        )
        oldPrice?.let {
            Spacer(modifier = Modifier.height(Dimens.xs))
            Text(
                text = "$currency ${"%.2f".format(it)}",
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                fontSize = (fontSize * 0.75).toInt().sp,
                style = MaterialTheme.typography.body2.copy(
                    textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                )
            )
        }
    }
}

// Previews
@Preview(showBackground = true)
@Composable
fun StockBadgePreview() {
    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            StockBadge(stock = 50)
            StockBadge(stock = 5)
            StockBadge(stock = 0)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StatusBadgesPreview() {
    MaterialTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatusBadge("Pendiente", backgroundColor = Color(0xFFFFF3CD), textColor = Color(0xFF856404))
            StatusBadge("Enviado", backgroundColor = Color(0xFFD1ECF1), textColor = Color(0xFF0C5460))
            DiscountBadge(25)
            NewBadge()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PriceTagPreview() {
    MaterialTheme {
        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            PriceTag(price = 125.99)
            PriceTag(price = 125.99, oldPrice = 175.00)
        }
    }
}
