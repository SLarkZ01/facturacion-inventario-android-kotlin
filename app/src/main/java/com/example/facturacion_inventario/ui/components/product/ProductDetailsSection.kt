package com.example.facturacion_inventario.ui.components.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.draw.clip
import com.example.facturacion_inventario.domain.model.Product
import com.example.facturacion_inventario.ui.theme.Dimens
import com.example.facturacion_inventario.ui.theme.AccentOrange

@Composable
fun ProductDetailsSection(
    product: Product,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Título de la sección
        Text(
            text = "Detalles del producto",
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colors.onSurface
        )

        Spacer(modifier = Modifier.height(Dimens.lg))

        // Especificaciones técnicas (pares clave-valor)
        if (product.specifications.isNotEmpty()) {
            Text(
                text = "Especificaciones técnicas",
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = MaterialTheme.colors.onSurface
            )

            Spacer(modifier = Modifier.height(Dimens.md))

            // Lista de especificaciones
            product.specifications.forEach { spec ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Dimens.s),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = spec.key,
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                        fontSize = 14.sp,
                        modifier = Modifier.weight(0.4f)
                    )
                    Text(
                        text = spec.value,
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(0.6f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.End
                    )
                }

                if (spec != product.specifications.last()) {
                    Divider(
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
                        thickness = 0.5.dp
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens.xl))
        }

        // Características destacadas (lista con viñetas)
        if (product.features.isNotEmpty()) {
            Divider(
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.15f),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(Dimens.xl))

            Text(
                text = "Características principales",
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = MaterialTheme.colors.onSurface
            )

            Spacer(modifier = Modifier.height(Dimens.md))

            // Lista de características con iconos
            product.features.forEach { feature ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Dimens.s),
                    verticalAlignment = Alignment.Top
                ) {
                    // Icono de check o viñeta
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .padding(top = Dimens.s)
                            .clip(CircleShape)
                            .background(AccentOrange)
                    )

                    Spacer(modifier = Modifier.width(Dimens.md))

                    Text(
                        text = feature,
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.9f),
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
