package com.example.facturacion_inventario.ui.components.banner

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.facturacion_inventario.domain.model.Category
import com.example.facturacion_inventario.ui.theme.Dimens

/**
 * CategoryBanner - Banner destacado para mostrar una categoría seleccionada
 * Útil para páginas de detalle de categoría o secciones promocionales
 */
@Composable
fun CategoryBanner(
    categoryName: String,
    categoryDescription: String,
    iconRes: Int,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.primary.copy(alpha = 0.08f),
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.s)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        backgroundColor = backgroundColor,
        elevation = 2.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(Dimens.lg),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = categoryName,
                modifier = Modifier.size(Dimens.imageLarge)
            )

            Spacer(modifier = Modifier.width(Dimens.md))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = categoryName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colors.onSurface
                )
                Spacer(modifier = Modifier.height(Dimens.s))
                Text(
                    text = categoryDescription,
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.75f)
                )
            }
        }
    }
}

/**
 * CategoryBanner (sobrecarga) - Acepta directamente el objeto Category del dominio
 */
@Composable
fun CategoryBanner(
    category: Category,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.primary.copy(alpha = 0.08f),
    onClick: (() -> Unit)? = null
) {
    CategoryBanner(
        categoryName = category.name,
        categoryDescription = category.description,
        iconRes = category.iconRes,
        modifier = modifier,
        backgroundColor = backgroundColor,
        onClick = onClick
    )
}

/**
 * OfferBanner - Banner promocional con gradiente y texto destacado
 * Ideal para ofertas, descuentos o anuncios importantes
 */
@Composable
fun OfferBanner(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.secondary,
    textColor: Color = Color.White,
    gradientColors: List<Color>? = null,
    onClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(
                brush = gradientColors?.let {
                    Brush.horizontalGradient(it)
                } ?: Brush.horizontalGradient(listOf(backgroundColor, backgroundColor))
            )
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(Dimens.lg)
    ) {
        Column {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            subtitle?.let {
                Spacer(modifier = Modifier.height(Dimens.s))
                Text(
                    text = it,
                    fontSize = 14.sp,
                    color = textColor.copy(alpha = 0.9f)
                )
            }
        }
    }
}

/**
 * InfoBanner - Banner informativo genérico con icono opcional
 * Útil para mensajes, alertas o notificaciones
 */
@Suppress("unused") // Componente útil para notificaciones
@Composable
fun InfoBanner(
    message: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.primary.copy(alpha = 0.1f),
    textColor: Color = MaterialTheme.colors.onSurface,
    iconRes: Int? = null
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = backgroundColor,
        elevation = 0.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(Dimens.lg),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            iconRes?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(Dimens.md))
            }
            Text(
                text = message,
                color = textColor,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

// Previews
@Preview(showBackground = true)
@Composable
fun OfferBannerPreview() {
    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            OfferBanner(
                title = "¡50% de descuento!",
                subtitle = "En repuestos seleccionados",
                gradientColors = listOf(Color(0xFFFF6B35), Color(0xFFF7931E))
            )
        }
    }
}
