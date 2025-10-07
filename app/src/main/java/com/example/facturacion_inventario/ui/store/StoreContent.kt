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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow

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

// Nuevo: tarjeta de producto con diseño orientado a inventario/facturación
@Composable
fun ProductCardItem(item: Product, onClick: (String) -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick(item.id) },
        backgroundColor = MaterialTheme.colors.surface,
        border = BorderStroke(1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.08f)),
        elevation = 6.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = item.imageRes), contentDescription = item.name, modifier = Modifier.size(56.dp))
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.name, color = MaterialTheme.colors.onBackground, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(4.dp))
                Text(text = item.description, color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f), fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(6.dp))
                // Mostrar información genérica cuando el modelo no incluye stock/price
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Disponible", color = MaterialTheme.colors.primary, fontSize = 12.sp)
                    Spacer(Modifier.width(12.dp))
                    Text(text = "Código: ${item.id}", color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f), fontSize = 11.sp)
                }
            }

            Spacer(Modifier.width(8.dp))

            Column(horizontalAlignment = Alignment.End) {
                // Si el modelo no tiene precio, mostramos un placeholder
                Text(text = "Precio: --", color = MaterialTheme.colors.primary, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Button(onClick = { /* navegar o añadir */ }, colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)) {
                    Text(text = "Añadir", color = MaterialTheme.colors.onPrimary)
                }
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
        // Encabezado con información del taller
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Taller - Gestión de Facturación e Inventario", color = MaterialTheme.colors.onBackground, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Text(text = "Controla tus repuestos, precios y stock desde una única app", color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f), fontSize = 13.sp)
        }

        Spacer(Modifier.height(12.dp))

        val products = repository.getProducts()

        // Lista más compacta y profesional
        Column(modifier = Modifier.fillMaxWidth()) {
            products.forEach { item ->
                ProductCardItem(item = item, onClick = onProductClick)
            }
        }

        Spacer(Modifier.weight(1f))

        // Acciones principales al final
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = onCategoriesClick, modifier = Modifier.weight(1f)) {
                Text(text = "Categorías", color = MaterialTheme.colors.primary)
            }
            Spacer(Modifier.width(12.dp))
            Button(onClick = onCartClick, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)) {
                Text(text = "Carrito", color = MaterialTheme.colors.onPrimary)
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
        Text(text = "Detalle del producto", color = MaterialTheme.colors.onBackground, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))

        product?.let {
            Card(modifier = Modifier.fillMaxWidth(), elevation = 6.dp, shape = MaterialTheme.shapes.medium) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(painter = painterResource(id = it.imageRes), contentDescription = it.name, modifier = Modifier.size(96.dp))
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = it.name, color = MaterialTheme.colors.onBackground, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(6.dp))
                            Text(text = it.description, color = MaterialTheme.colors.onSurface.copy(alpha = 0.9f), fontSize = 13.sp)
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    // Información básica (placeholders si el modelo no tiene price/stock)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text(text = "Precio", color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f), fontSize = 12.sp)
                            Text(text = "S/ --", color = MaterialTheme.colors.primary, fontWeight = FontWeight.Bold)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "Stock", color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f), fontSize = 12.sp)
                            Text(text = "--", color = MaterialTheme.colors.onSurface.copy(alpha = 0.9f), fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    Button(onClick = onAddToCart, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)) {
                        Text(text = "Añadir al inventario / Facturar", color = MaterialTheme.colors.onPrimary)
                    }
                }
            }
        } ?: run {
            Text(text = "Producto no encontrado", color = MaterialTheme.colors.onSurface)
        }
    }
}

@Composable
fun CartContent(
    onContinueShopping: () -> Unit,
    onCheckout: () -> Unit
) {
    StoreScreenScaffold {
        Text(text = "Carrito / Documentos", color = MaterialTheme.colors.onBackground, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))

        // Placeholder: lista de items del carrito con diseño limpio
        Card(modifier = Modifier.fillMaxWidth(), elevation = 4.dp) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = "Tu carrito está vacío", color = MaterialTheme.colors.onSurface.copy(alpha = 0.9f))
                Spacer(Modifier.height(6.dp))
                Text(text = "Aquí verás los items seleccionados para facturar o mover en inventario.", color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f), fontSize = 13.sp)
            }
        }

        Spacer(Modifier.weight(1f))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onContinueShopping, modifier = Modifier.weight(1f)) {
                Text(text = "Seguir", color = MaterialTheme.colors.primary)
            }
            Button(onClick = onCheckout, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)) {
                Text(text = "Generar factura", color = MaterialTheme.colors.onPrimary)
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
