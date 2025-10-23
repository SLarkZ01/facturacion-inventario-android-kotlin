package com.example.facturacion_inventario.ui.store

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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

// Import de composables reutilizables
import com.example.facturacion_inventario.ui.components.ProductListFromDomain

// Tokens visuales centralizados
import com.example.facturacion_inventario.ui.theme.Dimens
import com.example.facturacion_inventario.ui.theme.AccentOrange
import com.example.facturacion_inventario.ui.theme.AmazonYellow
import com.example.facturacion_inventario.ui.theme.SuccessGreen


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
        Spacer(modifier = Modifier.width(Dimens.md))

        // Botón decrementar
        OutlinedButton(
            onClick = { if (quantity > 1) onQuantityChange(quantity - 1) },
            modifier = Modifier.size(Dimens.iconButtonSize),
            contentPadding = PaddingValues(0.dp),
            enabled = quantity > 1,
            shape = CircleShape
        ) {
            Text(text = "−", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.width(Dimens.md))

        // Display cantidad
        Card(
            backgroundColor = MaterialTheme.colors.surface,
            elevation = 0.dp,
            border = BorderStroke(1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.2f)),
            shape = MaterialTheme.shapes.small
        ) {
            Box(
                modifier = Modifier
                    .width(Dimens.quantityBoxWidth)
                    .padding(vertical = Dimens.s),
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

        Spacer(modifier = Modifier.width(Dimens.md))

        // Botón incrementar
        OutlinedButton(
            onClick = { if (quantity < maxQuantity) onQuantityChange(quantity + 1) },
            modifier = Modifier.size(Dimens.iconButtonSize),
            contentPadding = PaddingValues(0.dp),
            enabled = quantity < maxQuantity,
            shape = CircleShape
        ) {
            Text(text = "+", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.width(Dimens.s))

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
            Column(modifier = Modifier.fillMaxSize().padding(Dimens.lg)) {
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
            .padding(vertical = Dimens.s)
            .clickable { onClick(item.id) },
        backgroundColor = MaterialTheme.colors.surface,
        border = BorderStroke(1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.08f)),
        elevation = 4.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(modifier = Modifier.padding(Dimens.md), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.name,
                modifier = Modifier.size(Dimens.imageMedium)
            )
            Spacer(modifier = Modifier.width(Dimens.md))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    color = MaterialTheme.colors.onBackground,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.subtitle1
                )
                Spacer(modifier = Modifier.height(Dimens.s))
                Text(
                    text = item.description,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(Dimens.s))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Disponible", color = MaterialTheme.colors.primary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(Dimens.md))
                    Text(text = "Código: ${item.id}", color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f), fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.width(Dimens.s))

            Column(horizontalAlignment = Alignment.End) {
                Text(text = "Precio: --", color = MaterialTheme.colors.primary, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(Dimens.s))
                Button(
                    onClick = { /* acción */ },
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                    modifier = Modifier.height(Dimens.buttonHeight)
                ) {
                    Text(text = "Añadir", color = MaterialTheme.colors.onPrimary)
                }
            }
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
            ProductListFromDomain(
                products = products,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.lg),
                layout = "grid",
                onItemClick = { p -> onProductClick(p.id) }
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
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Dimens.lg)
            ) {
                // Mostrar banner si hay categoría seleccionada
                if (selectedCategoryId != null) {
                    item {
                        val selectedCategory = categories.find { it.id == selectedCategoryId }
                        selectedCategory?.let { category ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = Dimens.s),
                                backgroundColor = AccentOrange.copy(alpha = 0.08f),
                                elevation = 2.dp,
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Row(
                                    modifier = Modifier.padding(Dimens.lg),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(id = category.iconRes),
                                        contentDescription = category.name,
                                        modifier = Modifier.size(Dimens.imageSmall)
                                    )
                                    Spacer(modifier = Modifier.width(Dimens.md))
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
                    Spacer(modifier = Modifier.height(Dimens.s))
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
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(Dimens.xxl)) // Espacio para los botones
                }
            }

            // Botones fijos en la parte inferior
            Surface(
                modifier = Modifier.fillMaxWidth(),
                elevation = 8.dp,
                color = MaterialTheme.colors.background
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.md),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.lg)
                ) {
                    OutlinedButton(
                        onClick = onCategoriesClick,
                        modifier = Modifier.weight(1f).height(Dimens.buttonHeight)
                    ) {
                        Text(text = "Categorías", color = MaterialTheme.colors.primary)
                    }
                    Button(
                        onClick = onCartClick,
                        modifier = Modifier.weight(1f).height(Dimens.buttonHeight),
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
                .height(Dimens.pagerHeight)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val media = mediaList[page]

                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = Dimens.s),
                    elevation = 0.dp,
                    backgroundColor = MaterialTheme.colors.surface,
                    shape = MaterialTheme.shapes.medium
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
                                        .height(Dimens.pagerHeight - Dimens.xs)
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
                                            .height(Dimens.pagerHeight - Dimens.xs)
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
                        .padding(Dimens.lg),
                    shape = MaterialTheme.shapes.small,
                    color = Color.Black.copy(alpha = 0.6f)
                ) {
                    Text(
                        text = "${pagerState.currentPage + 1}/${mediaList.size}",
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = Dimens.md, vertical = Dimens.xs)
                    )
                }
            }
        }

        // Indicadores de posición (puntos)
        if (mediaList.size > 1) {
            Spacer(modifier = Modifier.height(Dimens.md))
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(Dimens.s),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(mediaList.size) { index ->
                    Box(
                        modifier = Modifier
                            .size(if (index == pagerState.currentPage) 10.dp else 8.dp)
                            .padding(Dimens.xs)
                            .clip(CircleShape)
                            .background(
                                if (index == pagerState.currentPage)
                                    AccentOrange
                                else
                                    MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
                            )
                    )
                    if (index < mediaList.size - 1) {
                        Spacer(modifier = Modifier.width(Dimens.s))
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

        Spacer(modifier = Modifier.height(Dimens.lg))

        // Especificaciones técnicas (pares clave-valor)
        if (product.specifications.isNotEmpty()) {
            Text(
                text = "Especificaciones técnicas",
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = MaterialTheme.colors.onSurface
            )

            Spacer(modifier = Modifier.height(Dimens.md))

            // Lista de especificaciones
            product.specifications.forEach { spec ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Dimens.s),
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

            Spacer(modifier = Modifier.height(Dimens.xl))
        }

        // Características destacadas (lista con viñetas)
        if (product.features.isNotEmpty()) {
            Divider(
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.15f),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(Dimens.xl))

            Text(
                text = "Características principales",
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = MaterialTheme.colors.onSurface
            )

            Spacer(modifier = Modifier.height(Dimens.md))

            // Lista de características con iconos
            product.features.forEach { feature ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Dimens.s),
                    verticalAlignment = Alignment.Top
                ) {
                    // Icono de check o viñeta
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .padding(top = Dimens.s)
                            .clip(CircleShape)
                            .background(AccentOrange)
                    )

                    Spacer(modifier = Modifier.width(Dimens.md))

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
                Spacer(modifier = Modifier.height(Dimens.md))
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
                            modifier = Modifier.padding(Dimens.lg)
                        )
                    }
                    Spacer(modifier = Modifier.height(Dimens.xl))
                }

                // Información del producto
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 6.dp,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Column(modifier = Modifier.padding(Dimens.lg)) {
                            // Nombre y descripción
                            Text(
                                text = prod.name,
                                color = MaterialTheme.colors.onBackground,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                style = MaterialTheme.typography.h6
                            )
                            Spacer(modifier = Modifier.height(Dimens.s))
                            Text(
                                text = prod.description,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.9f),
                                fontSize = 14.sp
                            )

                            Spacer(modifier = Modifier.height(Dimens.lg))
                            Divider()
                            Spacer(modifier = Modifier.height(Dimens.lg))

                            // Precio
                            Text(
                                text = "Precio",
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(Dimens.xs))
                            Text(
                                text = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CO"))
                                    .format(prod.price),
                                color = AccentOrange,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )

                            Spacer(modifier = Modifier.height(Dimens.lg))

                            // Stock disponible
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (prod.stock > 0) "En stock" else "Sin stock",
                                    color = if (prod.stock > 0) SuccessGreen else MaterialTheme.colors.error,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                if (prod.stock > 0 && prod.stock <= 10) {
                                    Spacer(modifier = Modifier.width(Dimens.s))
                                    Text(
                                        text = "¡Solo quedan ${prod.stock}!",
                                        color = MaterialTheme.colors.error,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(Dimens.xl))
                            Divider()
                            Spacer(modifier = Modifier.height(Dimens.xl))

                            // Selector de cantidad
                            if (prod.stock > 0) {
                                QuantitySelector(
                                    quantity = selectedQuantity,
                                    maxQuantity = prod.stock,
                                    onQuantityChange = { selectedQuantity = it },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(Dimens.xl))

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
                                        .height(Dimens.buttonHeight)
                                        .graphicsLayer {
                                            scaleX = scale
                                            scaleY = scale
                                        },
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = AmazonYellow // Amarillo Amazon
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

                                Spacer(modifier = Modifier.height(Dimens.md))

                                // Botón naranja "Comprar ahora"
                                Button(
                                    onClick = { /* Acción de comprar ahora */ },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(Dimens.buttonHeight),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = AccentOrange // Naranja
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
                                        modifier = Modifier.padding(Dimens.lg),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "Producto agotado",
                                            color = MaterialTheme.colors.error,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        )
                                        Spacer(modifier = Modifier.height(Dimens.s))
                                        Text(
                                            text = "Este producto no está disponible actualmente",
                                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                                            fontSize = 13.sp,
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Sección de detalles
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 6.dp,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        ProductDetailsSection(
                            product = prod,
                            modifier = Modifier.padding(Dimens.lg)
                        )
                    }
                    Spacer(modifier = Modifier.height(Dimens.xl))
                }
            }

            item {
                Spacer(modifier = Modifier.height(Dimens.xl)) // Espacio final
            }
        }
    }
}

// Agrego CartContent y CategoriesContent faltantes (usadas por StoreScreens.kt)
@Composable
fun CartContent(onContinueShopping: () -> Unit, onCheckout: () -> Unit, cartViewModel: CartViewModel? = null) {
    StoreScreenScaffold {
        Text(text = "Carrito / Documentos", color = MaterialTheme.colors.onBackground, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(Dimens.md))

        if (cartViewModel == null || cartViewModel.cartItems.isEmpty()) {
            Card(modifier = Modifier.fillMaxWidth(), elevation = 4.dp) {
                Column(modifier = Modifier.padding(Dimens.lg)) {
                    Text(text = "Tu carrito está vacío", color = MaterialTheme.colors.onSurface.copy(alpha = 0.9f))
                    Spacer(modifier = Modifier.height(Dimens.s))
                    Text(text = "Aquí verás los items seleccionados para facturar o mover en inventario.", color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f), fontSize = 13.sp)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Dimens.md)
            ) {
                items(cartViewModel.cartItems) { cartItem ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 4.dp,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Row(
                            modifier = Modifier.padding(Dimens.lg),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = cartItem.product.imageRes),
                                contentDescription = cartItem.product.name,
                                modifier = Modifier.size(Dimens.imageLarge)
                            )

                            Spacer(modifier = Modifier.width(Dimens.md))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = cartItem.product.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(Dimens.s))
                                Text(
                                    text = "Cantidad: ${cartItem.quantity}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                                )
                                Spacer(modifier = Modifier.height(Dimens.s))
                                Text(
                                    text = "S/ ${"%.2f".format(cartItem.product.price)}",
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colors.primary
                                )
                            }

                            IconButton(onClick = { cartViewModel.removeFromCart(cartItem.product.id) }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_arrow_back),
                                    contentDescription = "Eliminar",
                                    tint = MaterialTheme.colors.error,
                                    modifier = Modifier.size(Dimens.iconSize)
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(Dimens.lg))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 4.dp,
                        backgroundColor = AccentOrange.copy(alpha = 0.08f)
                    ) {
                        Column(modifier = Modifier.padding(Dimens.lg)) {
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
                                    text = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CO")).format(cartViewModel.getTotalPrice()),
                                    color = AccentOrange,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(Dimens.s))
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
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(Dimens.md)) {
            OutlinedButton(onClick = onContinueShopping, modifier = Modifier.weight(1f).height(Dimens.buttonHeight)) {
                Text(text = "Seguir comprando", color = MaterialTheme.colors.primary)
            }
            Button(
                onClick = onCheckout,
                modifier = Modifier.weight(1f).height(Dimens.buttonHeight),
                enabled = cartViewModel?.cartItems?.isNotEmpty() == true,
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
            ) {
                Text(text = "Generar factura", color = MaterialTheme.colors.onPrimary)
            }
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
        Spacer(modifier = Modifier.height(Dimens.s))
        Text(
            text = "Explora nuestro catálogo completo",
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(Dimens.lg))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(Dimens.md),
            contentPadding = PaddingValues(Dimens.lg)
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
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.s)
            .clickable { onClick() },
        elevation = 4.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .padding(Dimens.lg)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = category.iconRes),
                contentDescription = category.name,
                modifier = Modifier.size(Dimens.imageMedium)
            )

            Spacer(modifier = Modifier.width(Dimens.md))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.onSurface
                )
                Spacer(modifier = Modifier.height(Dimens.s))
                Text(
                    text = category.description,
                    fontSize = 13.sp,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
            }

            TextButton(onClick = onClick) {
                Text(text = "Ver", color = MaterialTheme.colors.primary)
            }
        }
    }
}
