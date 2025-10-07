package com.example.facturacion_inventario.ui.store

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.facturacion_inventario.domain.model.Product
import com.example.facturacion_inventario.domain.repository.ProductRepository

// Composable reutilizable para el layout base (fondo y padding)
@Composable
fun StoreScreenScaffold(content: @Composable ColumnScope.() -> Unit) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(MaterialTheme.colors.background, MaterialTheme.colors.background)))) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                content()
            }
        }
    }
}

@Composable
fun HomeContent(
    repository: ProductRepository,
    onProductClick: (String) -> Unit,
    onCartClick: () -> Unit,
    onCategoriesClick: () -> Unit
) {
    StoreScreenScaffold {
        Text(text = "Tienda", color = MaterialTheme.colors.onBackground, fontSize = 24.sp)
        Spacer(Modifier.height(12.dp))

        val products = repository.getProducts()
        products.forEach { item ->
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .clickable { onProductClick(item.id) }, backgroundColor = MaterialTheme.colors.surface, border = BorderStroke(1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.12f)), elevation = 4.dp) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Image(painter = painterResource(id = item.imageRes), contentDescription = "prod", modifier = Modifier.size(48.dp))
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(text = item.name, color = MaterialTheme.colors.onBackground)
                        Text(text = item.description, color = MaterialTheme.colors.secondary, fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(Modifier.weight(1f))
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = onCartClick, colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)) {
                Text(text = "Carrito", color = MaterialTheme.colors.onPrimary)
            }
            Button(onClick = onCategoriesClick, colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)) {
                Text(text = "Categorías", color = MaterialTheme.colors.onPrimary)
            }
        }
    }
}

@Composable
fun ProductDetailContent(
    product: Product?,
    onAddToCart: () -> Unit
) {
    StoreScreenScaffold {
        Text(text = "Detalle del producto", color = MaterialTheme.colors.onBackground, fontSize = 24.sp)
        Spacer(Modifier.height(12.dp))
        Text(text = "ID: ${product?.id ?: "-"}", color = MaterialTheme.colors.secondary)
        product?.let {
            Spacer(Modifier.height(8.dp))
            Text(text = it.name, color = MaterialTheme.colors.onBackground)
            Text(text = it.description, color = MaterialTheme.colors.secondary, fontSize = 12.sp)
        }

        Spacer(Modifier.weight(1f))
        Button(onClick = onAddToCart, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)) {
            Text(text = "Añadir al carrito", color = MaterialTheme.colors.onPrimary)
        }
    }
}

@Composable
fun CartContent(
    onContinueShopping: () -> Unit,
    onCheckout: () -> Unit
) {
    StoreScreenScaffold {
        Text(text = "Carrito", color = MaterialTheme.colors.onBackground, fontSize = 24.sp)
        Spacer(Modifier.height(12.dp))
        Text(text = "Tu carrito está vacío (placeholder)", color = MaterialTheme.colors.secondary)

        Spacer(Modifier.weight(1f))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onContinueShopping, modifier = Modifier.weight(1f)) {
                Text(text = "Seguir comprando", color = MaterialTheme.colors.primary)
            }
            Button(onClick = onCheckout, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)) {
                Text(text = "Finalizar compra", color = MaterialTheme.colors.onPrimary)
            }
        }
    }
}

@Composable
fun SearchContent(
    initialQuery: String = "",
    onSearch: (String) -> Unit,
    onBack: () -> Unit
) {
    var query by remember { mutableStateOf(initialQuery) }

    StoreScreenScaffold {
        // Usamos onBack: botón para volver
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
            TextButton(onClick = onBack) {
                Text(text = "Volver")
            }
        }

        Text(text = "Buscar", color = MaterialTheme.colors.onBackground, fontSize = 24.sp)
        Spacer(Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(value = query, onValueChange = { query = it }, label = { Text("Buscar productos", color = MaterialTheme.colors.secondary) }, modifier = Modifier.weight(1f))
            Button(onClick = { onSearch(query) }, colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)) {
                Text(text = "Buscar", color = MaterialTheme.colors.onPrimary)
            }
        }
    }
}

@Composable
fun CategoriesContent(
    categories: List<String>,
    onCategoryClick: (String) -> Unit
) {
    StoreScreenScaffold {
        Text(text = "Categorías", color = MaterialTheme.colors.onBackground, fontSize = 24.sp)
        Spacer(Modifier.height(12.dp))
        categories.forEach { c ->
            Text(text = c, color = MaterialTheme.colors.onBackground, modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable { onCategoryClick(c) })
        }
    }
}
