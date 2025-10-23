package com.example.facturacion_inventario.ui.components

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
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.facturacion_inventario.domain.model.Product as DomainProduct
import androidx.compose.material.IconButton

// Tokens de tema
import com.example.facturacion_inventario.ui.theme.Dimens
import com.example.facturacion_inventario.ui.theme.AmazonYellow
import com.example.facturacion_inventario.ui.theme.SuccessGreen

// Data model UI para un repuesto (evita conflicto con domain.model.Product)
data class ProductUi(
    val id: String,
    val name: String,
    val price: Double,
    val currency: String = "S/",
    val oldPrice: Double? = null,
    val rating: Float? = null,
    val inStock: Boolean = true,
    val imageRes: Int? = null // recurso local opcional para preview
)

@Composable
fun ProductCard(
    product: ProductUi,
    modifier: Modifier = Modifier,
    onClick: (ProductUi) -> Unit = {},
    onToggleFavorite: (ProductUi, Boolean) -> Unit = { _, _ -> }
) {
    var favorite by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .width(Dimens.cardCompactWidth)
            .padding(Dimens.s)
            .clickable { onClick(product) },
        elevation = 4.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Imagen simulada / placeholder
            Box(modifier = Modifier
                .height(Dimens.cardImageHeight)
                .fillMaxWidth()
                .background(MaterialTheme.colors.surface)) {

                if (product.imageRes != null) {
                    Image(
                        painter = painterResource(id = product.imageRes),
                        contentDescription = product.name,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topStart = Dimens.cornerMedium, topEnd = Dimens.cornerMedium))
                    )
                } else {
                    // Placeholder estilizado similar a tarjetas compactas de e-commerce
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFFEFEFEF), Color(0xFFF7F7F7))
                            )
                        ), contentAlignment = Alignment.Center) {
                        // Reemplazamos Icon por texto Unicode para evitar dependencia de icons
                        Text(text = "★", color = Color(0xFFB0B0B0), fontSize = 28.sp)
                    }
                }

                // Favorite button overlay
                IconButton(
                    onClick = {
                        favorite = !favorite
                        onToggleFavorite(product, favorite)
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(Dimens.xs)
                        .size(Dimens.favoriteBadgeSize)
                        .background(MaterialTheme.colors.surface.copy(alpha = 0.6f), shape = MaterialTheme.shapes.small)
                ) {
                    if (favorite) {
                        // corazón lleno
                        Text(text = "♥", color = Color.Red, fontSize = 16.sp)
                    } else {
                        // corazón contorno
                        Text(text = "♡", color = MaterialTheme.colors.onSurface, fontSize = 16.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(Dimens.s))

            Column(modifier = Modifier.padding(horizontal = Dimens.md, vertical = Dimens.s)) {
                Text(
                    text = product.name,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.subtitle2,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(Dimens.s))

                // Precio y rating
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Text(
                            text = "${product.currency} ${"%.2f".format(product.price)}",
                            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                            fontSize = 14.sp
                        )
                        product.oldPrice?.let { old ->
                            Text(
                                text = "${product.currency} ${"%.2f".format(old)}",
                                style = MaterialTheme.typography.caption,
                                textDecoration = TextDecoration.LineThrough,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }

                    product.rating?.let { r ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // estrella como texto Unicode
                            Text(text = "★", color = AmazonYellow, fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(Dimens.s))
                            Text(text = "%.1f".format(r), style = MaterialTheme.typography.caption)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(Dimens.s))

                Text(
                    text = if (product.inStock) "En stock" else "Agotado",
                    style = MaterialTheme.typography.overline,
                    color = if (product.inStock) SuccessGreen else MaterialTheme.colors.error
                )
            }
        }
    }
}

@Composable
fun ProductGrid(
    products: List<ProductUi>,
    modifier: Modifier = Modifier,
    onItemClick: (ProductUi) -> Unit = {},
    useLazyLayout: Boolean = true
) {
    if (useLazyLayout) {
        LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = modifier.padding(Dimens.s), content = {
            items(products) { p ->
                ProductCard(product = p, onClick = onItemClick)
            }
        })
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
                    // Si solo hay un elemento en la fila, añadir spacer
                    if (row.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(Dimens.s))
            }
        }
    }
}

// Adaptador para usar la grid con el modelo de dominio
@Composable
fun ProductGridFromDomain(
    products: List<DomainProduct>,
    modifier: Modifier = Modifier,
    onItemClick: (DomainProduct) -> Unit = {},
    useLazyLayout: Boolean = true
) {
    // Mapeo simple: usamos campos disponibles y añadimos valores por defecto para precio/rating/stock
    val uiList = products.map { dp ->
        ProductUi(
            id = dp.id,
            name = dp.name,
            price = 19.99,
            currency = "S/",
            oldPrice = null,
            rating = 4.2f,
            inStock = true,
            imageRes = dp.imageRes
        )
    }

    if (useLazyLayout) {
        LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = modifier.padding(Dimens.s), content = {
            items(uiList) { p ->
                ProductCard(product = p, onClick = { onItemClick(products.first { it.id == p.id }) })
            }
        })
    } else {
        // Layout estático para usar dentro de otros componentes Lazy
        Column(modifier = modifier.padding(Dimens.s)) {
            uiList.chunked(2).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.s)
                ) {
                    row.forEach { p ->
                        Box(modifier = Modifier.weight(1f)) {
                            ProductCard(
                                product = p,
                                onClick = { onItemClick(products.first { it.id == p.id }) }
                            )
                        }
                    }
                    // Si solo hay un elemento en la fila, añadir spacer
                    if (row.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(Dimens.s))
            }
        }
    }
}

// Previews
@Preview(showBackground = true, widthDp = 360)
@Composable
fun ProductCardPreview() {
    val sample = ProductUi(id = "1", name = "Filtro de aceite para moto Yamaha YBR 125 - Repuesto original", price = 25.0, oldPrice = 35.0, rating = 4.5f, inStock = true)
    MaterialTheme {
        ProductCard(product = sample)
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun ProductGridPreview() {
    val samples = List(6) { i ->
        ProductUi(id = i.toString(), name = "Bujía NGK modelo X - apta para varias motos, alta duración", price = 18.0 + i, rating = 4.0f - (i % 3) * 0.3f, inStock = (i % 4 != 0))
    }
    MaterialTheme {
        ProductGrid(products = samples)
    }
}
