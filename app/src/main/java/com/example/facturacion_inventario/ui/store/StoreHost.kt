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
import androidx.compose.ui.draw.clip
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
            painter = painterResource(id = R.drawable.ic_badge),
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

// Nuevo composable para el buscador con control fino del layout
@Composable
fun SearchBox(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Hacemos el buscador más compacto y centrado verticalmente
    Row(
        modifier = modifier
            .height(36.dp), // más compacto
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_search),
            contentDescription = "search",
            tint = Color(0xFF757575),
            modifier = Modifier.size(16.dp) // icono aún más pequeño
        )
        Spacer(modifier = Modifier.width(8.dp))

        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            singleLine = true,
            textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(vertical = 2.dp), // ajustar centrado
            decorationBox = { innerTextField ->
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                    if (query.isEmpty()) {
                        Text(
                            text = "Buscar productos, marcas y más",
                            color = Color(0xFF9E9E9E),
                            style = TextStyle(fontSize = 13.sp)
                        )
                    }
                    innerTextField()
                }
            }
        )

        if (query.isNotBlank()) {
            IconButton(onClick = onClear, modifier = Modifier
                .size(30.dp)
                .padding(end = 2.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "clear",
                    tint = Color(0xFF757575),
                    modifier = Modifier.size(16.dp)
                )
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
    val maxHeight = 140.dp
    val minHeight = 56.dp
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
                        shape = RoundedCornerShape(28.dp),
                        elevation = 16.dp,
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth(0.92f)
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
                                    itemCount = cartViewModel.totalItemCount, // Usar el contador real del carrito
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
                    verticalArrangement = Arrangement.Center) {

                    // Fila superior (logo y perfil) que desaparece al colapsar
                    val fadingAlpha = 1f - collapseProgress
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(painter = painterResource(id = R.drawable.ic_motorcycle_animated), contentDescription = "logo", tint = Color.White.copy(alpha = fadingAlpha), modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        // Caja de búsqueda
                        Box(modifier = Modifier
                            .weight(1f)
                            .height(36.dp)) {
                            Surface(
                                shape = RoundedCornerShape(24.dp),
                                color = Color.White,
                                elevation = 0.dp,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(24.dp))
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 10.dp, end = 8.dp)) {
                                    SearchBox(
                                        query = query,
                                        onQueryChange = { new: String -> query = new },
                                        onClear = { query = "" },
                                        modifier = Modifier.fillMaxHeight()
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = { /* acción futura */ }, enabled = fadingAlpha > 0.05f) {
                            Icon(painter = painterResource(id = R.drawable.ic_person), contentDescription = "perfil", tint = Color.White.copy(alpha = fadingAlpha))
                        }
                    }

                    // Línea inferior (por ejemplo, destino de envío) que se oculta al colapsar
                    if (fadingAlpha > 0.05f) {
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
                .padding(top = headerHeightDp)
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
