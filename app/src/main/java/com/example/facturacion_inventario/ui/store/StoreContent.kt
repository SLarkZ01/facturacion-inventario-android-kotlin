package com.example.facturacion_inventario.ui.store

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.facturacion_inventario.domain.model.Product
import com.example.facturacion_inventario.domain.model.Category
import com.example.facturacion_inventario.domain.repository.ProductRepository
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.graphics.Color
import com.example.facturacion_inventario.data.repository.FakeProductRepository
import com.example.facturacion_inventario.R
import java.text.NumberFormat
import java.util.Locale

@Composable
fun StoreScreenScaffold(
    headerProgress: Float = 0f,
    content: @Composable ColumnScope.() -> Unit
) {
    // Sincroniza la barra de estado
    StatusBarSync(headerProgress = headerProgress)

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = Brush.verticalGradient(listOf(MaterialTheme.colors.background, MaterialTheme.colors.background)))
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                content()
            }
        }
    }
}

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
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.name,
                modifier = Modifier.size(56.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    color = MaterialTheme.colors.onBackground,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.description,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Disponible", color = MaterialTheme.colors.primary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "Código: ${item.id}", color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f), fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(text = "Precio: --", color = MaterialTheme.colors.primary, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { /* acción */ }, colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)) {
                    Text(text = "Añadir", color = MaterialTheme.colors.onPrimary)
                }
            }
        }
    }
}

// Nuevo componente para tarjeta de producto horizontal estilo Amazon
@Composable
fun ProductCardHorizontal(product: Product, onClick: (String) -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .width(160.dp)
            .clickable { onClick(product.id) },
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 2.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.name,
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = NumberFormat.getCurrencyInstance(Locale("es", "CO")).format(product.price),
                style = MaterialTheme.typography.body2,
                color = Color(0xFFFF6F00), // Naranja directo
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                text = "Stock: ${product.stock}",
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                fontSize = 11.sp
            )
        }
    }
}

// Sección de categoría con productos horizontales estilo Amazon
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
                .padding(horizontal = 16.dp, vertical = 8.dp),
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
                    Text(text = "Ver todos", color = Color(0xFFFF6F00), fontSize = 13.sp)
                }
            }
        }

        if (showAll) {
            // Vista de grid cuando se muestra todo
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                products.chunked(2).forEach { rowProducts ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowProducts.forEach { product ->
                            ProductCardHorizontal(
                                product = product,
                                onClick = onProductClick,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        // Si solo hay un producto en la fila, agregar un espacio vacío
                        if (rowProducts.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        } else {
            // Vista horizontal normal con máximo 5 productos
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(products.take(5)) { product ->
                    ProductCardHorizontal(product = product, onClick = onProductClick)
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
    onCategoriesClick: () -> Unit,
    onSeeAllCategoryClick: (String) -> Unit,
    selectedCategoryId: String? = null
) {
    val threshold = 120.dp
    val density = LocalDensity.current
    val thresholdPx = with(density) { threshold.toPx() }

    val listState = rememberLazyListState()

    val progress by remember {
        derivedStateOf {
            if (listState.firstVisibleItemIndex > 0) 1f
            else (listState.firstVisibleItemScrollOffset / thresholdPx).coerceIn(0f, 1f)
        }
    }

    StoreScreenScaffold(headerProgress = progress) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Obtenemos las categorías y productos desde el repositorio
            val repo = repository as? FakeProductRepository
            val categories = repo?.getCategories() ?: emptyList()

            // Filtrar categorías si hay una seleccionada
            val displayCategories = if (selectedCategoryId != null) {
                categories.filter { it.id == selectedCategoryId }
            } else {
                categories
            }

            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f)
            ) {
                // Mostrar banner si hay categoría seleccionada
                if (selectedCategoryId != null) {
                    item {
                        val selectedCategory = categories.find { it.id == selectedCategoryId }
                        selectedCategory?.let { category ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                backgroundColor = Color(0xFFFF6F00).copy(alpha = 0.1f),
                                elevation = 2.dp
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(id = category.iconRes),
                                        contentDescription = category.name,
                                        modifier = Modifier.size(40.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = category.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = MaterialTheme.colors.onSurface
                                        )
                                        Text(
                                            text = category.description,
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Mostramos cada categoría con sus productos
                items(displayCategories) { category ->
                    val productsInCategory = repo?.getProductsByCategory(category.id) ?: emptyList()
                    if (productsInCategory.isNotEmpty()) {
                        CategorySection(
                            category = category,
                            products = productsInCategory,
                            onProductClick = onProductClick,
                            onSeeAllClick = onSeeAllCategoryClick,
                            showAll = selectedCategoryId != null
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp)) // Espacio para los botones
                }
            }

            // Botones fijos en la parte inferior
            Surface(
                modifier = Modifier.fillMaxWidth(),
                elevation = 8.dp,
                color = MaterialTheme.colors.background
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    OutlinedButton(
                        onClick = onCategoriesClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Categorías", color = MaterialTheme.colors.primary)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = onCartClick,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                    ) {
                        Text(text = "Carrito", color = MaterialTheme.colors.onPrimary)
                    }
                }
            }
        }
    }
}

@Composable
fun ProductDetailContent(product: Product?, onAddToCart: () -> Unit) {
    StoreScreenScaffold {
        Text(text = "Detalle del producto", color = MaterialTheme.colors.onBackground, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        product?.let {
            Card(modifier = Modifier.fillMaxWidth(), elevation = 6.dp, shape = MaterialTheme.shapes.medium) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(painter = painterResource(id = it.imageRes), contentDescription = it.name, modifier = Modifier.size(96.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = it.name, color = MaterialTheme.colors.onBackground, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = it.description, color = MaterialTheme.colors.onSurface.copy(alpha = 0.9f), fontSize = 13.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
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

                    Spacer(modifier = Modifier.height(14.dp))
                    Button(onClick = onAddToCart, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)) {
                        Text(text = "Añadir al inventario / Facturar", color = MaterialTheme.colors.onPrimary)
                    }
                }
            }
        } ?: run { Text(text = "Producto no encontrado", color = MaterialTheme.colors.onSurface) }
    }
}

@Composable
fun CartContent(onContinueShopping: () -> Unit, onCheckout: () -> Unit) {
    StoreScreenScaffold {
        Text(text = "Carrito / Documentos", color = MaterialTheme.colors.onBackground, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        Card(modifier = Modifier.fillMaxWidth(), elevation = 4.dp) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = "Tu carrito está vacío", color = MaterialTheme.colors.onSurface.copy(alpha = 0.9f))
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Aquí verás los items seleccionados para facturar o mover en inventario.", color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f), fontSize = 13.sp)
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onContinueShopping, modifier = Modifier.weight(1f)) { Text(text = "Seguir", color = MaterialTheme.colors.primary) }
            Button(onClick = onCheckout, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)) { Text(text = "Generar factura", color = MaterialTheme.colors.onPrimary) }
        }
    }
}

@Composable
fun SearchContent(initialQuery: String = "", onSearch: (String) -> Unit, onBack: () -> Unit) {
    var query by remember { mutableStateOf(initialQuery) }
    StoreScreenScaffold {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) { TextButton(onClick = onBack) { Text(text = "Volver") } }
        Text(text = "Buscar", color = MaterialTheme.colors.onBackground, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(value = query, onValueChange = { query = it }, label = { Text("Buscar productos", color = MaterialTheme.colors.secondary) }, modifier = Modifier.weight(1f))
            Button(onClick = { onSearch(query) }, colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)) { Text(text = "Buscar", color = MaterialTheme.colors.onPrimary) }
        }
    }
}

@Composable
fun CategoriesContent(
    categories: List<Category>,
    onCategoryClick: (String) -> Unit
) {
    StoreScreenScaffold {
        Text(
            text = "Todas las Categorías",
            color = MaterialTheme.colors.onBackground,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Explora nuestro catálogo completo",
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                CategoryCard(
                    category = category,
                    onClick = { onCategoryClick(category.id) }
                )
            }
        }
    }
}

@Composable
fun CategoryCard(
    category: Category,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 4.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono de categoría
            Card(
                backgroundColor = Color(0xFFFF6F00).copy(alpha = 0.1f),
                shape = MaterialTheme.shapes.medium,
                elevation = 0.dp,
                modifier = Modifier.size(60.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(id = category.iconRes),
                        contentDescription = category.name,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Información de categoría
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = category.description,
                    style = MaterialTheme.typography.body2,
                    fontSize = 13.sp,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Icono de flecha
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_forward),
                contentDescription = "Ver categoría",
                tint = Color(0xFFFF6F00),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
