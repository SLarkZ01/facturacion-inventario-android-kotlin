@file:Suppress("DEPRECATION")
package com.example.facturacion_inventario.ui.store

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.auth.AuthViewModel
import com.example.facturacion_inventario.R
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.zIndex
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import com.example.facturacion_inventario.data.repository.FakeProductRepository
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel

// Badge del carrito con animación
@Composable
fun CartIconWithBadge(
    itemCount: Int,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Icon(
            // Icono base del carrito (nuevo drawable ic_cart)
            painter = painterResource(id = R.drawable.ic_cart),
            contentDescription = "cart",
            tint = iconColor,
            modifier = Modifier.size(26.dp)
        )

        // Badge animado
        AnimatedVisibility(
            visible = itemCount > 0,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 4.dp, y = (-4).dp)
        ) {
            val animatedCount by animateIntAsState(
                targetValue = itemCount,
                label = "cart_count"
            )

            Box(
                modifier = Modifier
                    .size(18.dp)
                    .background(Color.Red, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (animatedCount > 99) "99+" else animatedCount.toString(),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Nuevo SearchBar más elegante y con comportamiento según collapseProgress
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
    collapseProgress: Float = 0f // 0 = expanded, 1 = collapsed
) {
    // Color y elevación que cambian según el scroll
    val backgroundAlpha = (1f - 0.22f * collapseProgress).coerceIn(0.75f, 1f)
    val elevation = if (collapseProgress > 0.5f) 8.dp else 2.dp

    Surface(
        modifier = modifier,
        color = Color.White.copy(alpha = backgroundAlpha),
        shape = RoundedCornerShape(24.dp),
        elevation = elevation
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "search",
                tint = Color(0xFF757575),
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Box(modifier = Modifier.weight(1f)) {
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    decorationBox = { innerTextField ->
                        Box(modifier = Modifier.fillMaxWidth()) {
                            if (query.isEmpty()) {
                                Text(
                                    text = "Buscar productos, marcas y más",
                                    color = Color(0xFF9E9E9E),
                                    style = TextStyle(fontSize = 14.sp)
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }

            if (query.isNotBlank()) {
                IconButton(onClick = onClear, modifier = Modifier.size(36.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "clear",
                        tint = Color(0xFF757575),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun StoreHost(authViewModel: AuthViewModel, rootNavController: NavController) {
    val storeNavController = rememberNavController()
    val repo = remember { FakeProductRepository() }
    var selectedTab by remember { mutableStateOf("home") }
    var isSearchActive by rememberSaveable { mutableStateOf(false) }

    // ViewModel del carrito compartido entre todas las pantallas
    val cartViewModel: CartViewModel = viewModel()

    // OPTIMIZACIÓN CRÍTICA: Usar collectAsState para observar StateFlow de forma eficiente
    // Solo se recompone cuando cambia el valor, no todo el ViewModel
    val cartItemCount by cartViewModel.totalItemCount.collectAsState()

    // Mantener query en el scope del host para usarla en el topbar y en las sugerencias
    var query by rememberSaveable { mutableStateOf("") }

    // Colores del header (inspiración Amazon)
    val headerStartColor = Color(0xFFFF6F00) // naranja intenso
    val headerEndColor = Color.Transparent

    val view = LocalView.current
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val density = LocalDensity.current

    // Dimensiones del header
    // Hacemos la franja naranja más compacta: menos alto cuando está expandido
    val maxHeight = 92.dp
    val minHeight = 52.dp
    val maxHeightPx = with(density) { maxHeight.toPx() }
    val minHeightPx = with(density) { minHeight.toPx() }
    var headerHeightPx by remember { mutableStateOf(maxHeightPx) }

    // Progreso de colapso: 0f expandido, 1f colapsado
    val collapseProgress by remember {
        derivedStateOf {
            val range = (maxHeightPx - minHeightPx).coerceAtLeast(1f)
            ((maxHeightPx - headerHeightPx) / range).coerceIn(0f, 1f)
        }
    }

    // Altura de la barra de estado para que el header pueda pintarse detrás
    val statusBarHeightDp = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    // Conexión de scroll anidado para sincronizar lista y header
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val dy = available.y
                if (dy < 0f) {
                    val prev = headerHeightPx
                    headerHeightPx = (headerHeightPx + dy).coerceIn(minHeightPx, maxHeightPx)
                    val consumed = headerHeightPx - prev
                    // Consumimos solo lo que usamos para colapsar
                    return Offset(0f, consumed)
                }
                return Offset.Zero
            }

            override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                val dy = available.y
                if (dy > 0f) {
                    val prev = headerHeightPx
                    headerHeightPx = (headerHeightPx + dy).coerceIn(minHeightPx, maxHeightPx)
                    val consumedY = headerHeightPx - prev
                    return Offset(0f, consumedY)
                }
                return Offset.Zero
            }
        }
    }

    // Hacemos la barra de estado transparente y permitimos dibujar detrás de ella
    if (!view.isInEditMode) {
        SideEffect {
            val activity = (context as? Activity)
            activity?.let { act ->
                WindowCompat.setDecorFitsSystemWindows(act.window, false)
                act.window.statusBarColor = android.graphics.Color.TRANSPARENT
                val controller = WindowCompat.getInsetsController(act.window, view)
                controller.isAppearanceLightStatusBars = collapseProgress > 0.5f
            }
        }
    }

    Scaffold(
        // Eliminamos topBar; ahora el header es personalizado y colapsable
        bottomBar = {
            // Barra inferior estilo cápsula (ajustada para parecerse a la referencia)
            if (!isSearchActive) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(vertical = 10.dp), contentAlignment = Alignment.Center) {
                    Surface(
                        // Full-width flat bar: sin esquinas redondeadas ocupando ancho total
                        shape = RoundedCornerShape(0.dp),
                        elevation = 4.dp, // estilo más plano
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(62.dp)
                    ) {
                        Row(modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {

                            // Home
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable {
                                selectedTab = "home"
                                storeNavController.navigate("home") { popUpTo("home") }
                            }) {
                                Icon(painter = painterResource(id = R.drawable.ic_motorcycle_animated), contentDescription = "home", tint = if (selectedTab == "home") headerStartColor else Color(0xFF9E9E9E), modifier = Modifier.size(26.dp))
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(text = "Inicio", color = if (selectedTab == "home") headerStartColor else Color(0xFF9E9E9E), fontWeight = if (selectedTab == "home") FontWeight.SemiBold else FontWeight.Normal, style = MaterialTheme.typography.caption)
                            }

                            // Categories (nuevo botón movido desde la parte superior)
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable {
                                selectedTab = "categories"
                                storeNavController.navigate("categories") { popUpTo("home") }
                            }) {
                                // Icono representativo de categorías
                                Icon(painter = painterResource(id = R.drawable.ic_categories), contentDescription = "categories", tint = if (selectedTab == "categories") headerStartColor else Color(0xFF9E9E9E), modifier = Modifier.size(26.dp))
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(text = "Categorías", color = if (selectedTab == "categories") headerStartColor else Color(0xFF9E9E9E), fontWeight = if (selectedTab == "categories") FontWeight.SemiBold else FontWeight.Normal, style = MaterialTheme.typography.caption)
                            }

                            // Profile
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable {
                                selectedTab = "profile"
                                storeNavController.navigate("profile") { popUpTo("home") }
                            }) {
                                Icon(painter = painterResource(id = R.drawable.ic_person), contentDescription = "profile", tint = if (selectedTab == "profile") headerStartColor else Color(0xFF9E9E9E), modifier = Modifier.size(26.dp))
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(text = "Perfil", color = if (selectedTab == "profile") headerStartColor else Color(0xFF9E9E9E), fontWeight = if (selectedTab == "profile") FontWeight.SemiBold else FontWeight.Normal, style = MaterialTheme.typography.caption)
                            }

                            // Cart
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable {
                                selectedTab = "cart"
                                storeNavController.navigate("cart") { popUpTo("home") }
                            }) {
                                CartIconWithBadge(
                                    itemCount = cartItemCount, // Usar el contador real del carrito
                                    iconColor = if (selectedTab == "cart") headerStartColor else Color(0xFF9E9E9E),
                                    modifier = Modifier.size(26.dp)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(text = "Carrito", color = if (selectedTab == "cart") headerStartColor else Color(0xFF9E9E9E), fontWeight = if (selectedTab == "cart") FontWeight.SemiBold else FontWeight.Normal, style = MaterialTheme.typography.caption)
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        // Contenedor principal con nestedScroll para coordinar header y contenido
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .nestedScroll(nestedScrollConnection)) {

            val headerHeightDp = with(density) { headerHeightPx.toDp() }
            val backgroundColor = lerp(headerStartColor, headerEndColor, collapseProgress)
            // Espacio extra dinámico: cuando el header está expandido, empuja el contenido hacia abajo
            // Aumentado para que las categorías queden más visibles y no "metidas" en la franja naranja.
            val extraTop = 20.dp * (1f - collapseProgress)

            // Header colapsable superpuesto (ahora incluye el alto de la barra de estado)
            Surface(
                color = backgroundColor,
                elevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(headerHeightDp + statusBarHeightDp)
                    .zIndex(2f)
            ) {
                // Contenido interno del header
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp)
                    .statusBarsPadding(),
                    verticalArrangement = Arrangement.Top) { // Cambiado a Top para que la barra no quede tan abajo

                    // Espacio superior pequeño para separarlo del status bar
                    Spacer(modifier = Modifier.height(2.dp))

                    // SearchBar elegante y centrada horizontalmente
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                        .zIndex(3f), contentAlignment = Alignment.TopCenter) {

                        SearchBar(
                            query = query,
                            onQueryChange = { new: String -> query = new },
                            onClear = { query = "" },
                            modifier = Modifier
                                .fillMaxWidth(0.96f),
                            collapseProgress = collapseProgress
                        )
                    }

                    // Mantener el logo/perfil y el texto de destino, pero ocultarlos según el colapso
                    val fadingAlpha = 1f - collapseProgress

                    if (fadingAlpha > 0.05f) {
                        Spacer(modifier = Modifier.height(4.dp))

                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(painter = painterResource(id = R.drawable.ic_motorcycle_animated), contentDescription = "logo", tint = Color.White.copy(alpha = fadingAlpha), modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(onClick = { /* acción futura */ }) {
                                Icon(painter = painterResource(id = R.drawable.ic_person), contentDescription = "perfil", tint = Color.White.copy(alpha = fadingAlpha))
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Enviar a tu ubicación",
                            color = Color.White.copy(alpha = fadingAlpha),
                            style = MaterialTheme.typography.caption
                        )
                    }
                }
            }

            // Sugerencias: aparecen justo bajo el header, superpuestas sobre el contenido
            val suggestions = remember(query) {
                if (query.isBlank()) emptyList() else repo.getProducts().filter { it.name.contains(query, ignoreCase = true) }
            }

            if (suggestions.isNotEmpty()) {
                Card(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .padding(top = headerHeightDp + 8.dp)
                        .zIndex(3f)
                ) {
                    LazyColumn(modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp)) {
                        items(suggestions) { product ->
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // navegar al producto seleccionado
                                    storeNavController.navigate("product/${URLEncoder.encode(product.id, StandardCharsets.UTF_8.toString())}")
                                    query = ""
                                    focusManager.clearFocus()
                                }
                                .padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(painter = painterResource(id = product.imageRes), contentDescription = product.name, tint = Color.Unspecified, modifier = Modifier.size(36.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = product.name, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    Text(text = product.description, style = MaterialTheme.typography.caption, color = Color.Gray, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                }
                            }
                            Divider()
                        }
                    }
                }
            }

            // Contenido principal desplazable bajo el header
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(top = headerHeightDp + extraTop)
                 .zIndex(1f)) {
                NavHost(navController = storeNavController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(navController = storeNavController)
                    }
                    composable("home/{categoryId}") { backStackEntry ->
                        val categoryId = backStackEntry.arguments?.getString("categoryId")
                        HomeScreen(navController = storeNavController, selectedCategoryId = categoryId)
                    }
                    composable("product/{productId}") { backStackEntry ->
                        val pid = backStackEntry.arguments?.getString("productId")
                        ProductDetailScreen(productId = pid, cartViewModel = cartViewModel)
                    }
                    composable("cart") {
                        CartScreen(navController = storeNavController, cartViewModel = cartViewModel)
                    }
                    composable("profile") {
                        ProfileScreen(authViewModel = authViewModel, rootNavController = rootNavController)
                    }
                    composable("categories") {
                        CategoriesScreen(navController = storeNavController)
                    }
                }
            }
        }
    }
}
