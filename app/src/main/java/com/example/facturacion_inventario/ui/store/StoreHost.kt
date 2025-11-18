@file:Suppress("DEPRECATION")
package com.example.facturacion_inventario.ui.store

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.ColorFilter
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.auth.AuthViewModel
import com.example.facturacion_inventario.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.zIndex
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.facturacion_inventario.ui.animations.NavigationTransitions
import com.example.facturacion_inventario.ui.animations.bounceClick
import com.example.facturacion_inventario.ui.theme.TransparentStatusBar
import com.example.facturacion_inventario.ui.screens.SearchScreen

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
    onClick: (() -> Unit)? = null, // Nuevo parámetro para manejar clicks
    collapseProgress: Float = 0f // 0 = expanded, 1 = collapsed
) {
    // Mejoras visuales: borde sutil, elevación animada y micro-scale al colapsar
    val backgroundAlpha = (1f - 0.22f * collapseProgress).coerceIn(0.75f, 1f)
    val targetElevation = if (collapseProgress > 0.5f) 10.dp else 4.dp
    val elevationDp by animateDpAsState(targetValue = targetElevation, animationSpec = tween(durationMillis = 220))
    val targetScale = 1f - (0.02f * collapseProgress) // shrinks max 2%
    val scale by animateFloatAsState(targetValue = targetScale, animationSpec = tween(durationMillis = 220))

    Surface(
        modifier = modifier
            .scale(scale)
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(28.dp)),
        color = Color.White.copy(alpha = backgroundAlpha),
        shape = RoundedCornerShape(28.dp),
        elevation = elevationDp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Replace search icon with a spacer to hide it but keep spacing
            Spacer(modifier = Modifier.size(20.dp))

            Spacer(modifier = Modifier.width(10.dp))

            Box(modifier = Modifier.weight(1f)) {
                // Cuando tiene onClick, la barra es solo decorativa
                if (onClick != null) {
                    Text(
                        text = "Buscar productos, marcas y más",
                        color = Color(0xFF9E9E9E),
                        style = TextStyle(fontSize = 14.sp)
                    )
                } else {
                    // Barra funcional con TextField
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
            }

            if (query.isNotBlank() && onClick == null) {
                IconButton(onClick = onClear, modifier = Modifier.size(36.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = "clear",
                        tint = Color(0xFF757575),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Hide camera icon: replace with spacer
            Spacer(modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
fun StoreHost(authViewModel: AuthViewModel, rootNavController: NavController) {
    // Configurar barras transparentes para el store
    TransparentStatusBar(darkIcons = false)

    val storeNavController = rememberNavController()
    var selectedTab by remember { mutableStateOf("home") }

    // Observar la ruta actual para ocultar el header en SearchScreen
    val currentRoute by storeNavController.currentBackStackEntryAsState()
    val currentDestination = currentRoute?.destination?.route
    val isInSearchScreen = currentDestination == Routes.SEARCH

    // 🔥 CAMBIO CRÍTICO: Usar RemoteCartViewModel en lugar de CartViewModel local
    // Esto permite que el badge se actualice cuando se agregan productos al carrito del backend
    val context = LocalContext.current
    val remoteCartViewModel: RemoteCartViewModel = viewModel(
        factory = RemoteCartViewModelFactory(
            context.applicationContext as android.app.Application
        )
    )

    // OPTIMIZACIÓN CRÍTICA: Usar collectAsState para observar StateFlow de forma eficiente
    // Solo se recompone cuando cambia el valor, no todo el ViewModel
    val cartItemCount by remoteCartViewModel.totalItemCount.collectAsState()

    // Inicializar carrito al cargar la pantalla
    LaunchedEffect(Unit) {
        remoteCartViewModel.obtenerOCrearCarrito(usuarioId = null)
    }

    // Colores del header (inspiración Amazon)
    val headerStartColor = Color(0xFFFF6F00) // naranja intenso

    val view = LocalView.current
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

    // Animación suavizada del progreso para transiciones visuales más suaves
    val animatedCollapse by animateFloatAsState(targetValue = collapseProgress, animationSpec = tween(durationMillis = 260))

    // Altura de la barra de estado para que el header pueda pintarse detrás
    val statusBarHeightDp = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val statusBarHeightPx = with(density) { statusBarHeightDp.toPx() }

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
                        .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically) {

                        // Home con microinteracción
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.bounceClick {
                                selectedTab = "home"
                                storeNavController.navigate("home") { popUpTo("home") }
                            }
                        ) {
                            // Mostrar icono Ermotos en el tab Inicio usando el asset raster (WebP)
                            Image(
                                painter = painterResource(id = R.mipmap.ic_ermotos_foreground),
                                contentDescription = "home",
                                modifier = Modifier.size(26.dp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Inicio",
                                color = if (selectedTab == "home") headerStartColor else Color(0xFF9E9E9E),
                                fontWeight = if (selectedTab == "home") FontWeight.SemiBold else FontWeight.Normal,
                                style = MaterialTheme.typography.caption
                            )
                        }

                        // Categories con microinteracción
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.bounceClick {
                                selectedTab = "categories"
                                storeNavController.navigate("categories") { popUpTo("home") }
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_categories),
                                contentDescription = "categories",
                                tint = if (selectedTab == "categories") headerStartColor else Color(0xFF9E9E9E),
                                modifier = Modifier.size(26.dp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Categorías",
                                color = if (selectedTab == "categories") headerStartColor else Color(0xFF9E9E9E),
                                fontWeight = if (selectedTab == "categories") FontWeight.SemiBold else FontWeight.Normal,
                                style = MaterialTheme.typography.caption
                            )
                        }

                        // Profile con microinteracción
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.bounceClick {
                                selectedTab = "profile"
                                storeNavController.navigate("profile") { popUpTo("home") }
                            }
                        ) {
                            // Restaurar icono de perfil en bottom bar
                            Icon(
                                painter = painterResource(id = R.drawable.ic_person),
                                contentDescription = "profile",
                                tint = if (selectedTab == "profile") headerStartColor else Color(0xFF9E9E9E),
                                modifier = Modifier.size(26.dp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Perfil",
                                color = if (selectedTab == "profile") headerStartColor else Color(0xFF9E9E9E),
                                fontWeight = if (selectedTab == "profile") FontWeight.SemiBold else FontWeight.Normal,
                                style = MaterialTheme.typography.caption
                            )
                        }

                        // Cart con microinteracción
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.bounceClick {
                                selectedTab = "cart"
                                storeNavController.navigate("cart") { popUpTo("home") }
                            }
                        ) {
                            CartIconWithBadge(
                                itemCount = cartItemCount,
                                iconColor = if (selectedTab == "cart") headerStartColor else Color(0xFF9E9E9E),
                                modifier = Modifier.size(26.dp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Carrito",
                                color = if (selectedTab == "cart") headerStartColor else Color(0xFF9E9E9E),
                                fontWeight = if (selectedTab == "cart") FontWeight.SemiBold else FontWeight.Normal,
                                style = MaterialTheme.typography.caption
                            )
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
            // Espacio extra dinámico: cuando el header está expandido, empuja el contenido hacia abajo
            val extraTop = 20.dp * (1f - animatedCollapse)
            // Gradiente sutil para dar más profundidad al header naranja
            val bgBrush = Brush.verticalGradient(
                 colors = listOf(headerStartColor, headerStartColor.copy(alpha = 0.9f), headerStartColor.copy(alpha = 0.75f)),
                 startY = 0f,
                 endY = headerHeightPx + statusBarHeightPx
             )

            // Header colapsable superpuesto (ahora incluye el alto de la barra de estado)
            // Solo se muestra si NO estamos en SearchScreen
            if (!isInSearchScreen) {
                // Fondo naranja independiente que se desplaza hacia arriba al hacer scroll
                val headerOffsetDp = with(density) { (animatedCollapse * (headerHeightPx + statusBarHeightPx)).toDp() }
                Surface(
                    color = Color.Transparent,
                    elevation = 4.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(headerHeightDp + statusBarHeightDp)
                        .offset(y = -headerOffsetDp)
                        .zIndex(1f)
                ) {
                    // Usamos un Box con degradado dentro del Surface para controlar mejor el look
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(brush = bgBrush)
                        .padding(horizontal = 12.dp)
                        .statusBarsPadding(),
                        contentAlignment = Alignment.TopStart) {
                        Column(verticalArrangement = Arrangement.Top) {

                            // Espacio superior pequeño para separarlo del status bar
                            Spacer(modifier = Modifier.height(2.dp))

                            // Mantener el logo/perfil y el texto de destino, pero ocultarlos según el colapso
                            val fadingAlpha = 1f - animatedCollapse

                            if (fadingAlpha > 0.05f) {
                                Spacer(modifier = Modifier.height(4.dp))

                                Row(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                    // Mostrar logo Ermotos en header usando el foreground WebP directamente
                                    Image(
                                        painter = painterResource(id = R.mipmap.ic_ermotos_foreground),
                                        contentDescription = "logo",
                                        modifier = Modifier.size(24.dp),
                                        colorFilter = ColorFilter.tint(Color.White.copy(alpha = fadingAlpha))
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    // Eliminado: botón de perfil en el header según solicitud del usuario.
                                    // Mantenemos un spacer para conservar el layout visual.
                                    Spacer(modifier = Modifier.size(36.dp))
                                }

                                // Mensaje de ubicación eliminado (solicitado por el usuario)
                            }
                        }
                    }
                }

                // SearchBar fijo y separado del fondo naranja (overlay)
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(2f)
                    .statusBarsPadding(), contentAlignment = Alignment.TopCenter) {

                    // Barra de búsqueda del inicio: solo navega a SearchScreen
                    SearchBar(
                        query = "", // Siempre vacía porque no es funcional
                        onQueryChange = { }, // No hace nada
                        onClear = { },
                        onClick = {
                            // Navegar a la pantalla de búsqueda dedicada
                            storeNavController.navigate(Routes.SEARCH)
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.96f)
                            // Sutil elevación extra para que el buscador flote sobre el contenido
                            .shadow(6.dp, RoundedCornerShape(28.dp)),
                        collapseProgress = animatedCollapse
                    )
                }
            }

            // Contenido principal con transiciones animadas
            Box(modifier = Modifier
                .fillMaxSize()
                // Solo agregar padding superior si NO estamos en SearchScreen
                .padding(top = if (isInSearchScreen) 0.dp else headerHeightDp + extraTop)
                .zIndex(1f)) {
                NavHost(
                    navController = storeNavController,
                    startDestination = "home",
                    enterTransition = { NavigationTransitions.fadeInOnly() },
                    exitTransition = { NavigationTransitions.fadeOutOnly() },
                    popEnterTransition = { NavigationTransitions.fadeInOnly() },
                    popExitTransition = { NavigationTransitions.fadeOutOnly() }
                ) {
                    composable(
                        "home",
                        enterTransition = { NavigationTransitions.fadeInOnly() },
                        exitTransition = { NavigationTransitions.fadeOutOnly() }
                    ) {
                        HomeScreen(navController = storeNavController)
                    }

                    composable("home/{categoryId}") { backStackEntry ->
                        val categoryId = backStackEntry.arguments?.getString("categoryId")
                        HomeScreen(navController = storeNavController, selectedCategoryId = categoryId)
                    }

                    composable(
                        "product/{productId}",
                        enterTransition = { NavigationTransitions.slideInFromRight() },
                        exitTransition = { NavigationTransitions.slideOutToRight() }
                    ) { backStackEntry ->
                        val pid = backStackEntry.arguments?.getString("productId")
                        ProductDetailScreen(productId = pid, cartViewModel = remoteCartViewModel)
                    }

                    composable("cart") {
                        CartScreen(navController = storeNavController, cartViewModel = remoteCartViewModel)
                    }

                    composable("profile") {
                        ProfileScreen(authViewModel = authViewModel, rootNavController = rootNavController)
                    }

                    composable("categories") {
                        CategoriesScreen(navController = storeNavController)
                    }

                    // Nueva ruta para SearchScreen
                    composable("search") {
                        // Simplemente navegamos a la pantalla de búsqueda
                        SearchScreen(
                            navController = storeNavController // Pasar el navController directamente
                        )
                    }
                }
            }
        }
    }
}
