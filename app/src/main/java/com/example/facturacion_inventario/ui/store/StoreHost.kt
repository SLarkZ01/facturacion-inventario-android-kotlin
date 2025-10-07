package com.example.facturacion_inventario.ui.store

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.compose.ui.graphics.Color
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import com.example.facturacion_inventario.data.repository.FakeProductRepository
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.foundation.layout.heightIn
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.BasicTextField

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

    // Mantener query en el scope del host para usarla en el topbar y en las sugerencias
    var query by rememberSaveable { mutableStateOf("") }

    // Color azul para TopBar/statusBar (similar a Amazon) con iconos blancos
    val topBarBlue = Color(0xFF0A6ED1)

    val view = LocalView.current
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    if (!view.isInEditMode) {
        @Suppress("DEPRECATION")
        SideEffect {
            val activity = (context as? Activity)
            activity?.window?.statusBarColor = topBarBlue.toArgb()
            activity?.let { act ->
                // permitir que la app dibuje detrás de las barras del sistema
                WindowCompat.setDecorFitsSystemWindows(act.window, false)
                val controller = WindowCompat.getInsetsController(act.window, view)
                controller.isAppearanceLightStatusBars = false // icons light (white)
            }
        }
    }

    Scaffold(
        topBar = {
            // TopAppBar con búsqueda estilo Amazon
            TopAppBar(
                backgroundColor = topBarBlue,
                contentColor = Color.White,
                elevation = 4.dp,
                modifier = Modifier.statusBarsPadding()
            ) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {

                    // Logo o ícono a la izquierda
                    Icon(painter = painterResource(id = R.drawable.ic_motorcycle_animated), contentDescription = "logo", tint = Color.White, modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(8.dp))

                    // Caja de búsqueda blanca y redondeada (altura reducida para el estilo deseado)
                    Box(modifier = Modifier
                        .weight(1f)
                        .height(36.dp)) {
                        Surface(
                            shape = RoundedCornerShape(24.dp), // pill más pronunciada
                            color = Color.White,
                            elevation = 0.dp, // más plano
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(24.dp))
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 10.dp, end = 8.dp)) {
                                // Reemplazado TextField por SearchBox para controlar mejor el layout
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

                    // Perfil o acciones
                    IconButton(onClick = { /* acción futura */ }) {
                        Icon(painter = painterResource(id = R.drawable.ic_person), contentDescription = "perfil", tint = Color.White)
                    }
                }
            }
        },
        bottomBar = {
            // Barra inferior estilo elevado y central
            if (!isSearchActive) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                    Surface(
                        shape = RoundedCornerShape(28.dp),
                        elevation = 12.dp,
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth(0.92f)
                            .height(64.dp)
                    ) {
                        Row(modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {

                            // Home
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable {
                                selectedTab = "home"
                                storeNavController.navigate("home") { popUpTo("home") }
                            }) {
                                Icon(painter = painterResource(id = R.drawable.ic_motorcycle_animated), contentDescription = "home", tint = if (selectedTab == "home") topBarBlue else Color.Gray)
                                Text(text = "Inicio", color = if (selectedTab == "home") topBarBlue else Color.Gray, fontWeight = if (selectedTab == "home") FontWeight.Bold else FontWeight.Normal, style = MaterialTheme.typography.caption)
                            }

                            // Profile
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable {
                                selectedTab = "profile"
                                storeNavController.navigate("profile") { popUpTo("home") }
                            }) {
                                Icon(painter = painterResource(id = R.drawable.ic_person), contentDescription = "profile", tint = if (selectedTab == "profile") topBarBlue else Color.Gray)
                                Text(text = "Cuenta", color = if (selectedTab == "profile") topBarBlue else Color.Gray, fontWeight = if (selectedTab == "profile") FontWeight.Bold else FontWeight.Normal, style = MaterialTheme.typography.caption)
                            }

                            // Cart
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable {
                                selectedTab = "cart"
                                storeNavController.navigate("cart") { popUpTo("home") }
                            }) {
                                Icon(painter = painterResource(id = R.drawable.ic_badge), contentDescription = "cart", tint = if (selectedTab == "cart") topBarBlue else Color.Gray)
                                Text(text = "Carrito", color = if (selectedTab == "cart") topBarBlue else Color.Gray, fontWeight = if (selectedTab == "cart") FontWeight.Bold else FontWeight.Normal, style = MaterialTheme.typography.caption)
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {

            Column(modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)) {

                Spacer(modifier = Modifier.height(8.dp))

                // Sugerencias desplegables: se muestran justo debajo de la barra de búsqueda cuando hay texto.
                val suggestions = remember(query) {
                    if (query.isBlank()) emptyList() else repo.getProducts().filter { it.name.contains(query, ignoreCase = true) }
                }

                if (suggestions.isNotEmpty()) {
                    Card(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
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

                // Contenido principal de la tienda (NavHost)
                Box(modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 8.dp)) {
                    NavHost(navController = storeNavController, startDestination = "home") {
                        composable("home") {
                            HomeScreen(navController = storeNavController)
                        }
                        composable("product/{productId}") { backStackEntry ->
                            val pid = backStackEntry.arguments?.getString("productId")
                            ProductDetailScreen(productId = pid, navController = storeNavController)
                        }
                        composable("cart") {
                            CartScreen(navController = storeNavController)
                        }
                        composable("profile") {
                            ProfileScreen(authViewModel = authViewModel, rootNavController = rootNavController)
                        }
                    }
                }
            }
        }
    }
}
