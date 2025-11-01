package com.example.facturacion_inventario.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.facturacion_inventario.domain.model.Product

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
