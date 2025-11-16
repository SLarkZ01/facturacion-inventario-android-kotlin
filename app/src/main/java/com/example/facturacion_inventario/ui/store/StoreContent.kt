package com.example.facturacion_inventario.ui.store

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.facturacion_inventario.domain.model.Product
import com.example.facturacion_inventario.domain.model.Category

// Componentes reutilizables
import com.example.facturacion_inventario.ui.components.product.ProductListFromDomain

// Tokens visuales
import com.example.facturacion_inventario.ui.theme.Dimens
import com.example.facturacion_inventario.ui.theme.AccentOrange

@Composable
fun StoreScreenScaffold(
    headerProgress: Float = 0f,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = Brush.verticalGradient(listOf(MaterialTheme.colors.background, MaterialTheme.colors.background)))
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(Dimens.lg)) {
                content()
            }
        }
    }
}

@Composable
fun CategorySection(
    category: Category,
    products: List<Product>,
    onProductClick: (String) -> Unit,
    onSeeAllClick: (String) -> Unit,
    showAll: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.lg, vertical = Dimens.s),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = category.description,
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }
            if (!showAll) {
                TextButton(onClick = { onSeeAllClick(category.id) }) {
                    Text(text = "Ver todos", color = AccentOrange, fontSize = 13.sp)
                }
            }
        }

        if (showAll) {
            // Cuando showAll es true, mostrar en grid de 2 columnas con useLazyLayout = false
            // para que funcione dentro del LazyColumn padre
            ProductListFromDomain(
                products = products,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.lg),
                layout = "grid",
                onItemClick = { p -> onProductClick(p.id) },
                useLazyLayout = false // IMPORTANTE: false para usar dentro de LazyColumn
            )
        } else {
            ProductListFromDomain(
                products = products,
                modifier = Modifier
                    .fillMaxWidth(),
                layout = "horizontal",
                onItemClick = { p -> onProductClick(p.id) }
            )
        }
    }
}
