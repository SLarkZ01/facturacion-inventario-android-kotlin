package com.example.facturacion_inventario.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.facturacion_inventario.domain.model.Product

// Tokens
import com.example.facturacion_inventario.ui.theme.Dimens

/**
 * ProductComponents - composables reutilizables para listas de productos a partir del modelo de dominio.
 * Provee:
 * - ProductHorizontalFromDomain: fila horizontal con tarjetas (usa `ProductCard` interno)
 * - ProductListFromDomain: adaptador que elige entre grid (2 columnas) o fila horizontal
 */

// Convierte Product (dominio) a ProductUi (simplificado) para ser usado por `ProductCard`.
private fun mapToUi(products: List<Product>): List<ProductUi> = products.map { dp ->
    ProductUi(
        id = dp.id,
        name = dp.name,
        price = dp.price.takeIf { it > 0.0 } ?: 0.0,
        currency = "S/",
        oldPrice = null,
        rating = null,
        inStock = dp.stock > 0,
        imageRes = dp.imageRes
    )
}

@Composable
fun ProductHorizontalFromDomain(products: List<Product>, modifier: Modifier = Modifier, onItemClick: (Product) -> Unit = {}) {
    val uiList = mapToUi(products)

    LazyRow(modifier = modifier.padding(Dimens.s), horizontalArrangement = Arrangement.spacedBy(Dimens.s)) {
        items(uiList) { ui ->
            // Encontrar el producto de dominio correspondiente por id
            val domain = products.firstOrNull { it.id == ui.id }
            ProductCard(product = ui, onClick = {
                domain?.let { onItemClick(it) }
            })
        }
    }
}

@Composable
fun ProductListFromDomain(
    products: List<Product>,
    modifier: Modifier = Modifier,
    layout: String = "horizontal",
    onItemClick: (Product) -> Unit = {},
    useLazyLayout: Boolean = false
) {
    when (layout) {
        "grid" -> {
            // Reusar ProductGridFromDomain que ya mapea y muestra una grid
            ProductGridFromDomain(
                products = products,
                modifier = modifier,
                onItemClick = onItemClick,
                useLazyLayout = useLazyLayout
            )
        }
        else -> {
            ProductHorizontalFromDomain(products = products, modifier = modifier, onItemClick = onItemClick)
        }
    }
}
