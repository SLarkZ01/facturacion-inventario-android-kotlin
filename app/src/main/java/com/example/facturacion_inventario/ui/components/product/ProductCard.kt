package com.example.facturacion_inventario.ui.components.product

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.example.facturacion_inventario.domain.model.Product as DomainProduct
import com.example.facturacion_inventario.ui.theme.Dimens
import com.example.facturacion_inventario.ui.theme.SuccessGreen
import com.example.facturacion_inventario.ui.animations.bounceClick

/**
 * Data model UI para un producto optimizado con @Stable.
 * @Stable le indica a Compose que esta clase es inmutable y estable,
 * permitiendo saltarse recomposiciones innecesarias.
 */
@Stable
data class ProductUi(
    val id: String,
    val name: String,
    val price: Double,
    val currency: String = "S/",
    val oldPrice: Double? = null,
    val rating: Float? = null,
    val inStock: Boolean = true,
    val imageRes: Int? = null, // recurso local opcional para preview
    val imageUrl: String? = null // URL para Coil
)

/**
 * ProductCard optimizado para máximo rendimiento:
 * - Usa remember para estado interno
 * - Usa derivedStateOf para cálculos
 * - Integra Coil para carga eficiente de imágenes
 * - Minimiza recomposiciones con keys estables
 * - Microinteracciones con bounceClick
 */
@Composable
fun ProductCard(
    product: ProductUi,
    modifier: Modifier = Modifier,
    onClick: (ProductUi) -> Unit = {},
    onToggleFavorite: (ProductUi, Boolean) -> Unit = { _, _ -> }
) {
    var favorite by remember(product.id) { mutableStateOf(false) }

    // Usa derivedStateOf para evitar recomposiciones cuando el cálculo no cambia
    val priceText = remember(product.price, product.currency) {
        "${product.currency} ${"%.2f".format(product.price)}"
    }

    val oldPriceText = remember(product.oldPrice, product.currency) {
        product.oldPrice?.let { "${product.currency} ${"%.2f".format(it)}" }
    }

    Card(
        modifier = modifier
            .width(Dimens.cardCompactWidth)
            .padding(Dimens.s)
            .bounceClick { onClick(product) }, // Microinteracción bounce
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 4.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Imagen optimizada con Coil
            Box(modifier = Modifier
                .height(Dimens.cardImageHeight)
                .fillMaxWidth()
                .background(MaterialTheme.colors.surface)) {

                if (product.imageUrl != null) {
                    // Usar AsyncImage de Coil para carga eficiente
                    AsyncImage(
                        model = product.imageUrl,
                        contentDescription = product.name,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topStart = Dimens.cornerMedium, topEnd = Dimens.cornerMedium)),
                        contentScale = ContentScale.Crop,
                        // Placeholder mientras carga
                        placeholder = null,
                        error = null // Placeholder en caso de error
                    )
                } else if (product.imageRes != null) {
                    AsyncImage(
                        model = product.imageRes,
                        contentDescription = product.name,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topStart = Dimens.cornerMedium, topEnd = Dimens.cornerMedium)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Placeholder estilizado
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFFEFEFEF), Color(0xFFF7F7F7))
                            )
                        ), contentAlignment = Alignment.Center) {
                        Text(text = "★", color = Color(0xFFB0B0B0), fontSize = 28.sp)
                    }
                }

                // Favorite button overlay optimizado
                IconButton(
                    onClick = {
                        favorite = !favorite
                        onToggleFavorite(product, favorite)
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(Dimens.xs)
                        .size(Dimens.favoriteBadgeSize)
                ) {
                    Surface(
                        color = MaterialTheme.colors.surface.copy(alpha = 0.6f),
                        shape = MaterialTheme.shapes.small,
                        elevation = 0.dp
                    ) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                text = if (favorite) "♥" else "♡",
                                color = if (favorite) Color.Red else MaterialTheme.colors.onSurface,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(Dimens.s))

            Column(modifier = Modifier.padding(horizontal = Dimens.md, vertical = Dimens.s)) {
                Text(
                    text = product.name,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.subtitle1,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(Dimens.s))

                // Precio y rating
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = priceText,
                            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                            fontSize = 14.sp
                        )
                        oldPriceText?.let { oldText ->
                            Text(
                                text = oldText,
                                style = MaterialTheme.typography.body2,
                                textDecoration = TextDecoration.LineThrough,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }

                    // Espacio reservado para mantener layout consistente
                    product.rating?.let {
                        Box(modifier = Modifier.width(48.dp))
                    }
                }

                Spacer(modifier = Modifier.height(Dimens.s))

                Text(
                    text = if (product.inStock) "En stock" else "Agotado",
                    style = MaterialTheme.typography.caption,
                    color = if (product.inStock) SuccessGreen else MaterialTheme.colors.error
                )
            }
        }
    }
}

/**
 * ProductGrid optimizado con keys para mejor rendimiento.
 * La key permite a Compose identificar items únicos y evitar recomposiciones completas.
 */
@Composable
fun ProductGrid(
    products: List<ProductUi>,
    modifier: Modifier = Modifier,
    onItemClick: (ProductUi) -> Unit = {},
    useLazyLayout: Boolean = true
) {
    if (useLazyLayout) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier.padding(Dimens.s),
            // Importante: contentPadding para evitar clipping
            contentPadding = PaddingValues(bottom = Dimens.md)
        ) {
            items(products, key = { it.id }) { p ->
                ProductCard(product = p, onClick = onItemClick)
            }
        }
    } else {
        // Layout estático para usar dentro de otros componentes Lazy
        Column(modifier = modifier.padding(Dimens.s)) {
            products.chunked(2).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.s)
                ) {
                    row.forEach { p ->
                        Box(modifier = Modifier.weight(1f)) {
                            ProductCard(product = p, onClick = onItemClick)
                        }
                    }
                    if (row.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(Dimens.s))
            }
        }
    }
}

/**
 * Adaptador optimizado con remember para mapeo.
 * El mapeo solo se recalcula cuando cambia la lista de productos.
 */
@Composable
fun ProductGridFromDomain(
    products: List<DomainProduct>,
    modifier: Modifier = Modifier,
    onItemClick: (DomainProduct) -> Unit = {},
    useLazyLayout: Boolean = true
) {
    // Usa remember para cachear el mapeo y evitar recalcularlo en cada recomposición
    val uiList = remember(products) {
        products.map { dp ->
            ProductUi(
                id = dp.id,
                name = dp.name,
                price = dp.price.takeIf { it > 0.0 } ?: 0.0,
                currency = "S/",
                oldPrice = null,
                rating = null,
                inStock = dp.stock > 0,
                imageRes = dp.imageRes,
                imageUrl = null
            )
        }
    }

    if (useLazyLayout) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier.padding(Dimens.s),
            horizontalArrangement = Arrangement.spacedBy(Dimens.s),
            verticalArrangement = Arrangement.spacedBy(Dimens.s),
            contentPadding = PaddingValues(Dimens.s)
        ) {
            items(uiList, key = { it.id }) { ui ->
                val domain = products.firstOrNull { it.id == ui.id }
                ProductCard(product = ui, onClick = {
                    domain?.let { onItemClick(it) }
                })
            }
        }
    } else {
        // Grid de 2 columnas para usar dentro de LazyColumn
        Column(
            modifier = modifier.padding(Dimens.s),
            verticalArrangement = Arrangement.spacedBy(Dimens.s)
        ) {
            uiList.chunked(2).forEach { rowProducts ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.s)
                ) {
                    rowProducts.forEach { ui ->
                        val domain = products.firstOrNull { it.id == ui.id }
                        Box(modifier = Modifier.weight(1f)) {
                            ProductCard(product = ui, onClick = {
                                domain?.let { onItemClick(it) }
                            })
                        }
                    }
                    // Si solo hay 1 producto en la fila, agregar espacio vacío
                    if (rowProducts.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

// Preview
@Preview(showBackground = true)
@Composable
fun ProductCardPreview() {
    MaterialTheme {
        ProductCard(
            product = ProductUi(
                id = "1",
                name = "Aceite Motor Castrol 20W-50",
                price = 45.99,
                oldPrice = 55.00,
                inStock = true
            )
        )
    }
}


