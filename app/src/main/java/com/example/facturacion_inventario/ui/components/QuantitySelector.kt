package com.example.facturacion_inventario.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.facturacion_inventario.ui.theme.Dimens

@Composable
fun QuantitySelector(
    quantity: Int,
    maxQuantity: Int,
    onQuantityChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = "Cantidad:",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colors.onSurface
        )
        Spacer(modifier = Modifier.width(Dimens.md))

        // Botón decrementar
        OutlinedButton(
            onClick = { if (quantity > 1) onQuantityChange(quantity - 1) },
            modifier = Modifier.size(Dimens.iconButtonSize),
            contentPadding = PaddingValues(0.dp),
            enabled = quantity > 1,
            shape = CircleShape
        ) {
            Text(text = "−", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.width(Dimens.md))

        // Display cantidad
        Card(
            backgroundColor = MaterialTheme.colors.surface,
            elevation = 0.dp,
            border = BorderStroke(1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.2f)),
            shape = MaterialTheme.shapes.small
        ) {
            Box(
                modifier = Modifier
                    .width(Dimens.quantityBoxWidth)
                    .padding(vertical = Dimens.s),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = quantity.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.width(Dimens.md))

        // Botón incrementar
        OutlinedButton(
            onClick = { if (quantity < maxQuantity) onQuantityChange(quantity + 1) },
            modifier = Modifier.size(Dimens.iconButtonSize),
            contentPadding = PaddingValues(0.dp),
            enabled = quantity < maxQuantity,
            shape = CircleShape
        ) {
            Text(text = "+", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.width(Dimens.s))

        // Mostrar stock máximo disponible
        Text(
            text = "($maxQuantity disponibles)",
            fontSize = 11.sp,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
        )
    }
}

