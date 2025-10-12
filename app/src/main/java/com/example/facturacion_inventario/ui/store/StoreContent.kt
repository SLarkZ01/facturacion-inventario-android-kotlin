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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import com.example.facturacion_inventario.domain.model.Product
import com.example.facturacion_inventario.domain.model.Category
import com.example.facturacion_inventario.domain.model.MediaType
import com.example.facturacion_inventario.domain.repository.ProductRepository
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.graphics.Color
import com.example.facturacion_inventario.data.repository.FakeProductRepository
import com.example.facturacion_inventario.R
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.animation.core.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState

// Selector de cantidad estilo Amazon
@Composable
fun QuantitySelector(
    quantity: Int,
    maxQuantity: Int,
    onQuantityChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = "Cantidad:",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colors.onSurface
        )
        Spacer(modifier = Modifier.width(12.dp))

        // Botón decrementar
        OutlinedButton(
            onClick = { if (quantity > 1) onQuantityChange(quantity - 1) },
            modifier = Modifier.size(40.dp),
            contentPadding = PaddingValues(0.dp),
            enabled = quantity > 1,
            shape = CircleShape
        ) {
            Text(text = "−", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Display cantidad
        Card(
            backgroundColor = MaterialTheme.colors.surface,
            elevation = 0.dp,
            border = BorderStroke(1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.2f)),
            shape = MaterialTheme.shapes.small
        ) {
            Box(
                modifier = Modifier
                    .width(50.dp)
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = quantity.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Botón incrementar
        OutlinedButton(
            onClick = { if (quantity < maxQuantity) onQuantityChange(quantity + 1) },
            modifier = Modifier.size(40.dp),
            contentPadding = PaddingValues(0.dp),
            enabled = quantity < maxQuantity,
            shape = CircleShape
        ) {
            Text(text = "+", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Mostrar stock máximo disponible
        Text(
            text = "($maxQuantity disponibles)",
            fontSize = 11.sp,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
        )
    }
}

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
                text = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CO")).format(product.price),
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

// Carrusel de medios para productos (imágenes y videos)
@Composable
fun ProductMediaCarousel(
    product: Product,
    modifier: Modifier = Modifier
) {
    // Si el producto tiene medios, usarlos, sino usar la imagen por defecto
    val mediaList = if (product.mediaList.isNotEmpty()) {
        product.mediaList
    } else {
        listOf(
            com.example.facturacion_inventario.domain.model.ProductMedia(
                resourceId = product.imageRes,
                type = MediaType.IMAGE
            )
        )
    }

    val pagerState = rememberPagerState(pageCount = { mediaList.size })

    Column(modifier = modifier) {
        // Carrusel de medios
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val media = mediaList[page]

                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    elevation = 0.dp,
                    backgroundColor = MaterialTheme.colors.surface
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        when (media.type) {
                            MediaType.IMAGE -> {
                                Image(
                                    painter = painterResource(id = media.resourceId),
                                    contentDescription = "Imagen ${page + 1} de ${product.name}",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(240.dp)
                                )
                            }
                            MediaType.VIDEO -> {
                                // Para videos, mostrar una imagen con un icono de play
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Image(
                                        painter = painterResource(id = media.resourceId),
                                        contentDescription = "Video ${page + 1} de ${product.name}",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(240.dp)
                                    )
                                    // Overlay con icono de play
                                    Surface(
                                        modifier = Modifier.size(60.dp),
                                        shape = CircleShape,
                                        color = Color.Black.copy(alpha = 0.6f)
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_motorcycle_animated),
                                                contentDescription = "Reproducir video",
                                                tint = Color.White,
                                                modifier = Modifier.size(30.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Contador de medios en la esquina superior derecha
            if (mediaList.size > 1) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp),
                    shape = MaterialTheme.shapes.small,
                    color = Color.Black.copy(alpha = 0.6f)
                ) {
                    Text(
                        text = "${pagerState.currentPage + 1}/${mediaList.size}",
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Indicadores de posición (puntos)
        if (mediaList.size > 1) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(mediaList.size) { index ->
                    Box(
                        modifier = Modifier
                            .size(if (index == pagerState.currentPage) 10.dp else 8.dp)
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(
                                if (index == pagerState.currentPage)
                                    Color(0xFFFF6F00)
                                else
                                    MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
                            )
                    )
                    if (index < mediaList.size - 1) {
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }
        }
    }
}

// Sección de Detalles del Producto estilo Amazon
@Composable
fun ProductDetailsSection(
    product: Product,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Título de la sección
        Text(
            text = "Detalles del producto",
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colors.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Especificaciones técnicas (pares clave-valor)
        if (product.specifications.isNotEmpty()) {
            Text(
                text = "Especificaciones técnicas",
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = MaterialTheme.colors.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Lista de especificaciones
            product.specifications.forEach { spec ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = spec.key,
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                        fontSize = 14.sp,
                        modifier = Modifier.weight(0.4f)
                    )
                    Text(
                        text = spec.value,
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(0.6f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.End
                    )
                }

                if (spec != product.specifications.last()) {
                    Divider(
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
                        thickness = 0.5.dp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Características destacadas (lista con viñetas)
        if (product.features.isNotEmpty()) {
            Divider(
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.15f),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Características principales",
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = MaterialTheme.colors.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Lista de características con iconos
            product.features.forEach { feature ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    // Icono de check o viñeta
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .padding(top = 6.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFF6F00))
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = feature,
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.9f),
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun ProductDetailContent(product: Product?, onAddToCart: () -> Unit, cartViewModel: CartViewModel? = null) {
    // Estado para la cantidad seleccionada
    var selectedQuantity by remember { mutableStateOf(1) }

    // Estado para la animación del botón "Agregar al carrito"
    var isAnimating by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isAnimating) 0.92f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        finishedListener = { isAnimating = false }
    )

    StoreScreenScaffold {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Text(
                    text = "Detalle del producto",
                    color = MaterialTheme.colors.onBackground,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            product?.let { prod ->
                // Carrusel de medios
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 6.dp,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        ProductMediaCarousel(
                            product = prod,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Información del producto
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 6.dp,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Nombre y descripción
                            Text(
                                text = prod.name,
                                color = MaterialTheme.colors.onBackground,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = prod.description,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.9f),
                                fontSize = 14.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                            Divider()
                            Spacer(modifier = Modifier.height(16.dp))

                            // Precio
                            Text(
                                text = "Precio",
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CO"))
                                    .format(prod.price),
                                color = Color(0xFFFF6F00),
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Stock disponible
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (prod.stock > 0) "En stock" else "Sin stock",
                                    color = if (prod.stock > 0) Color(0xFF388E3C) else MaterialTheme.colors.error,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                if (prod.stock > 0 && prod.stock <= 10) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "¡Solo quedan ${prod.stock}!",
                                        color = MaterialTheme.colors.error,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))
                            Divider()
                            Spacer(modifier = Modifier.height(20.dp))

                            // Selector de cantidad
                            if (prod.stock > 0) {
                                QuantitySelector(
                                    quantity = selectedQuantity,
                                    maxQuantity = prod.stock,
                                    onQuantityChange = { selectedQuantity = it },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                // Botón amarillo "Agregar al carrito"
                                Button(
                                    onClick = {
                                        isAnimating = true
                                        // Agregar producto al carrito usando el ViewModel
                                        cartViewModel?.addToCart(prod, selectedQuantity)
                                        onAddToCart()
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                        .graphicsLayer {
                                            scaleX = scale
                                            scaleY = scale
                                        },
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color(0xFFFFC107) // Amarillo Amazon
                                    ),
                                    shape = MaterialTheme.shapes.medium
                                ) {
                                    Text(
                                        text = "Agregar al carrito",
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Botón naranja "Comprar ahora"
                                Button(
                                    onClick = { /* Acción de comprar ahora */ },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color(0xFFFF6F00) // Naranja
                                    ),
                                    shape = MaterialTheme.shapes.medium
                                ) {
                                    Text(
                                        text = "Comprar ahora",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                }
                            } else {
                                // Mensaje cuando no hay stock
                                Card(
                                    backgroundColor = MaterialTheme.colors.error.copy(alpha = 0.1f),
                                    elevation = 0.dp,
                                    shape = MaterialTheme.shapes.medium
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "Producto agotado",
                                            color = MaterialTheme.colors.error,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Este producto no está disponible actualmente",
                                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                                            fontSize = 13.sp,
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))
                            Divider()
                            Spacer(modifier = Modifier.height(16.dp))

                            // Código del producto
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "Código del producto",
                                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                                        fontSize = 12.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = prod.id,
                                        color = MaterialTheme.colors.onSurface,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "Categoría",
                                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                                        fontSize = 12.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = prod.categoryId.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                                        color = MaterialTheme.colors.onSurface,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }

                // Sección de Detalles del producto
                if (prod.specifications.isNotEmpty() || prod.features.isNotEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = 6.dp,
                            shape = MaterialTheme.shapes.medium
                        ) {
                            ProductDetailsSection(
                                product = prod,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            } ?: item {
                Text(
                    text = "Producto no encontrado",
                    color = MaterialTheme.colors.onSurface
                )
            }

            // Espacio al final
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun CartContent(onContinueShopping: () -> Unit, onCheckout: () -> Unit, cartViewModel: CartViewModel? = null) {
    StoreScreenScaffold {
        Text(text = "Carrito / Documentos", color = MaterialTheme.colors.onBackground, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        // Mostrar items del carrito o mensaje de vacío
        if (cartViewModel?.cartItems?.isEmpty() == true || cartViewModel == null) {
            Card(modifier = Modifier.fillMaxWidth(), elevation = 4.dp) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "Tu carrito está vacío", color = MaterialTheme.colors.onSurface.copy(alpha = 0.9f))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Aquí verás los items seleccionados para facturar o mover en inventario.", color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f), fontSize = 13.sp)
                }
            }
        } else {
            // Mostrar lista de productos en el carrito
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cartViewModel.cartItems) { cartItem ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 4.dp,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = cartItem.product.imageRes),
                                contentDescription = cartItem.product.name,
                                modifier = Modifier.size(60.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = cartItem.product.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Cantidad: ${cartItem.quantity}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CO"))
                                        .format(cartItem.product.price * cartItem.quantity),
                                    color = Color(0xFFFF6F00),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                            IconButton(onClick = { cartViewModel.removeFromCart(cartItem.product.id) }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_arrow_back),
                                    contentDescription = "Eliminar",
                                    tint = MaterialTheme.colors.error
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 4.dp,
                        backgroundColor = Color(0xFFFF6F00).copy(alpha = 0.1f)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Total:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Text(
                                    text = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CO"))
                                        .format(cartViewModel.getTotalPrice()),
                                    color = Color(0xFFFF6F00),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "${cartViewModel.totalItemCount} producto(s) en total",
                                fontSize = 12.sp,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onContinueShopping, modifier = Modifier.weight(1f)) {
                Text(text = "Seguir comprando", color = MaterialTheme.colors.primary)
            }
            Button(
                onClick = onCheckout,
                modifier = Modifier.weight(1f),
                enabled = cartViewModel?.cartItems?.isNotEmpty() == true,
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
            ) {
                Text(text = "Generar factura", color = MaterialTheme.colors.onPrimary)
            }
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
