package com.example.facturacion_inventario.ui.components.product

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.facturacion_inventario.domain.model.Product
import com.example.facturacion_inventario.ui.components.ProductGridFromDomain

// Tokens
import com.example.facturacion_inventario.ui.theme.Dimens


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
        androidx.compose.material.Text(text = product.description.take(200), style = androidx.compose.material.MaterialTheme.typography.body2)
    }
}
