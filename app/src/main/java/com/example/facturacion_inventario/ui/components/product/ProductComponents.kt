package com.example.facturacion_inventario.ui.components.product

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.facturacion_inventario.domain.model.Product
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items

// Tokens
import com.example.facturacion_inventario.ui.theme.Dimens

/**
 * ProductComponents - composables reutilizables optimizados para listas de productos.
 * Optimizaciones:
 * - Usa remember para cachear mapeos
 * - Usa keys en items() para mejor rendimiento
 * - Evita recalcular transformaciones en cada recomposición
 */

// Convierte Product (dominio) a ProductUi (simplificado) para ser usado por `ProductCard`.
// Ahora es una función de utilidad que se cachea con remember.
private fun mapToUi(products: List<Product>): List<ProductUi> = products.map { dp ->
    ProductUi(
        id = dp.id,
        name = dp.name,
        price = dp.price.takeIf { it > 0.0 } ?: 0.0,
        currency = "S/",
        oldPrice = null,
        rating = null,
        inStock = dp.stock > 0,
        imageRes = dp.imageRes,
        imageUrl = null // TODO: agregar cuando tengas URLs del backend
    )
}

/**
 * ProductHorizontalFromDomain optimizado con remember y keys.
 */
@Composable
fun ProductHorizontalFromDomain(
    products: List<Product>,
    modifier: Modifier = Modifier,
    onItemClick: (Product) -> Unit = {}
) {
    // Cachea el mapeo - solo se recalcula cuando cambia products
    val uiList = remember(products) { mapToUi(products) }

    LazyRow(
        modifier = modifier.padding(Dimens.s),
        horizontalArrangement = Arrangement.spacedBy(Dimens.s),
        // Agrega contentPadding para mejor UX
        contentPadding = PaddingValues(horizontal = Dimens.s)
    ) {
        // KEY CRÍTICO: permite a Compose identificar items únicos y evitar recomposiciones completas
        items(uiList, key = { it.id }) { ui ->
            // Busca el producto de dominio correspondiente por id
            val domain = products.firstOrNull { it.id == ui.id }
            ProductCard(product = ui, onClick = {
                domain?.let { onItemClick(it) }
            })
        }
    }
}

/**
 * ProductListFromDomain optimizado que delega a componentes especializados.
 */
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
            // Reusar ProductGridFromDomain que ya está optimizado
            ProductGridFromDomain(
                products = products,
                modifier = modifier,
                onItemClick = onItemClick,
                useLazyLayout = useLazyLayout
            )
        }
        else -> {
            ProductHorizontalFromDomain(
                products = products,
                modifier = modifier,
                onItemClick = onItemClick
            )
        }
    }
}

/**
 * ProductHeader: encabezado simple para la sección de productos.
 * Parámetros: title - texto mostrado.
 */
@Composable
fun ProductHeader(title: String, modifier: Modifier = Modifier) {
    androidx.compose.material.Text(text = title, modifier = modifier.padding(Dimens.md), style = androidx.compose.material.MaterialTheme.typography.h6)
}

/**
 * ProductList: Lista reutilizable que recibe una lista de `Product` del dominio y muestra una grid usando `ProductCard`.
 * - products: lista del dominio
 * - onItemClick: callback cuando se pulsa un producto
 */
@Composable
fun ProductList(products: List<Product>, modifier: Modifier = Modifier, onItemClick: (Product) -> Unit = {}, useLazyLayout: Boolean = false) {
    // Reutilizamos ProductGridFromDomain del componente ProductCard
    ProductGridFromDomain(
        products = products,
        modifier = modifier,
        useLazyLayout = useLazyLayout,
        onItemClick = { clickedDomain ->
            onItemClick(clickedDomain)
        }
    )
}

/**
 * ProductDetailSummary: tarjeta simple que muestra nombre, precio y descripción corta.
 * Mantenerla ligera así puede usarse en pantallas de detalle compactas.
 */
@Composable
fun ProductDetailSummary(product: Product, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(Dimens.md)) {
        androidx.compose.material.Text(text = product.name, style = androidx.compose.material.MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(Dimens.s))
        androidx.compose.material.Text(text = "S/ ${"%.2f".format(product.price)}", style = androidx.compose.material.MaterialTheme.typography.subtitle1)
        Spacer(modifier = Modifier.height(Dimens.s))
        androidx.compose.material.Text(text = product.description, style = androidx.compose.material.MaterialTheme.typography.body2, color = androidx.compose.material.MaterialTheme.colors.onSurface.copy(alpha = 0.7f))
    }
}
