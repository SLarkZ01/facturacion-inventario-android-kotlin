package com.example.facturacion_inventario.ui.components.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.facturacion_inventario.ui.components.shared.InputField
import com.example.facturacion_inventario.ui.components.product.ProductUi
import com.example.facturacion_inventario.ui.components.product.ProductCard

// Tokens
import com.example.facturacion_inventario.ui.theme.Dimens

/**
 * SearchField: campo reutilizable para introducir texto de búsqueda.
 */
@Composable
fun SearchField(query: String, onQueryChange: (String) -> Unit, modifier: Modifier = Modifier) {
    // Aplicar modifier para permitir personalización desde quien llama
    InputField(value = query, onValueChange = onQueryChange, labelText = "Buscar productos", modifier = modifier)
}

/**
 * SearchResults: lista simple de resultados (ProductUi). Usa `ProductCard` para mostrar cada item.
 */
@Composable
fun SearchResults(results: List<ProductUi>, modifier: Modifier = Modifier, onItemClick: (ProductUi) -> Unit = {}) {
    LazyColumn(modifier = modifier.padding(Dimens.s)) {
        items(results) { item ->
            ProductCard(product = item, modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.s), onClick = { onItemClick(item) })
        }
    }
}

/**
 * EmptySearchPlaceholder: mostrado cuando no hay resultados.
 */
@Composable
fun EmptySearchPlaceholder(modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth().padding(Dimens.lg)) {
        Column(modifier = Modifier.padding(Dimens.xl)) {
            Text(text = "Sin resultados", style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(Dimens.s))
            Text(text = "Prueba con otra palabra clave.")
        }
    }
}
