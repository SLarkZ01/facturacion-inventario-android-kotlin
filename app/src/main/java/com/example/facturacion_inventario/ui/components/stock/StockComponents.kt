package com.example.facturacion_inventario.ui.components.stock

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.facturacion_inventario.data.remote.model.StockByAlmacenDto
import com.example.facturacion_inventario.ui.store.StockLevel
import com.example.facturacion_inventario.ui.store.StockState

/**
 * Badge visual de stock según el nivel disponible
 * 🟢 Verde: Stock > 10 → "En stock"
 * 🟡 Amarillo: Stock 1-10 → "Pocas unidades"
 * 🔴 Rojo: Stock = 0 → "Sin stock"
 */
@Composable
fun StockBadge(
    total: Int,
    modifier: Modifier = Modifier,
    showIcon: Boolean = true
) {
    val stockLevel = when {
        total == 0 -> StockLevel.OUT_OF_STOCK
        total <= 10 -> StockLevel.LOW_STOCK
        else -> StockLevel.IN_STOCK
    }

    val (backgroundColor, textColor, iconText, text) = when (stockLevel) {
        StockLevel.IN_STOCK -> {
            StockInfo(
                Color(0xFF4CAF50).copy(alpha = 0.15f),
                Color(0xFF2E7D32),
                "✓",
                "En stock ($total)"
            )
        }
        StockLevel.LOW_STOCK -> {
            StockInfo(
                Color(0xFFFF9800).copy(alpha = 0.15f),
                Color(0xFFE65100),
                "⚠",
                "Pocas unidades ($total)"
            )
        }
        StockLevel.OUT_OF_STOCK -> {
            StockInfo(
                Color(0xFFF44336).copy(alpha = 0.15f),
                Color(0xFFC62828),
                "✕",
                "Sin stock"
            )
        }
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (showIcon) {
                Text(
                    text = iconText,
                    color = textColor,
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.caption,
                color = textColor,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/**
 * Componente compacto de stock (solo color e icono)
 */
@Composable
fun StockIndicator(
    total: Int,
    modifier: Modifier = Modifier
) {
    val (color, iconText) = when {
        total == 0 -> Color(0xFFC62828) to "✕"
        total <= 10 -> Color(0xFFE65100) to "⚠"
        else -> Color(0xFF2E7D32) to "✓"
    }

    Text(
        text = iconText,
        color = color,
        style = MaterialTheme.typography.body1,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}

/**
 * Card expandible con desglose de stock por almacén
 */
@Composable
fun StockDetailCard(
    stockState: StockState,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = stockState is StockState.Success,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        if (stockState is StockState.Success) {
            Card(
                modifier = modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.8f),
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Disponibilidad",
                            style = MaterialTheme.typography.subtitle1,
                            fontWeight = FontWeight.Bold
                        )
                        StockBadge(total = stockState.total, showIcon = false)
                    }

                    Divider(modifier = Modifier.padding(vertical = 4.dp))

                    if (stockState.stockByAlmacen.isNotEmpty()) {
                        Text(
                            text = "Stock por almacén:",
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )

                        stockState.stockByAlmacen.forEach { almacen ->
                            AlmacenStockRow(almacen)
                        }
                    } else {
                        Text(
                            text = "No hay información de almacenes",
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Fila con información de stock de un almacén específico
 */
@Composable
private fun AlmacenStockRow(almacen: StockByAlmacenDto) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = almacen.almacenNombre ?: "Almacén desconocido",
            style = MaterialTheme.typography.body2
        )
        Text(
            text = "${almacen.cantidad} unidades",
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.SemiBold,
            color = when {
                almacen.cantidad == 0 -> Color(0xFFC62828)
                almacen.cantidad <= 10 -> Color(0xFFE65100)
                else -> Color(0xFF2E7D32)
            }
        )
    }
}

/**
 * Loading skeleton para el stock
 */
@Composable
fun StockLoadingSkeleton(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(150.dp)
            .height(30.dp)
            .background(
                color = MaterialTheme.colors.surface.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp)
            )
    )
}

/**
 * Clase auxiliar para información de stock
 */
@Suppress("unused")
private data class StockInfo(
    val backgroundColor: Color,
    val textColor: Color,
    val icon: String,
    val text: String
)
