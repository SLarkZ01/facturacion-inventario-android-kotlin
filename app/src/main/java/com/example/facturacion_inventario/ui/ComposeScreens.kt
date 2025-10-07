package com.example.facturacion_inventario.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import com.example.facturacion_inventario.R
import com.example.auth.AuthViewModel

@Composable
fun LoginScreen(authViewModel: AuthViewModel, navController: NavController? = null, onLoginSuccess: (() -> Unit)? = null) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val uiState by authViewModel.uiState.collectAsState()

    // Mostrar errores con efecto
    LaunchedEffect(key1 = uiState.errorMessage) {
        uiState.errorMessage?.let { msg ->
            Toast.makeText(context, "Login falló: $msg", Toast.LENGTH_LONG).show()
            authViewModel.clearError()
        }
    }

    // Navegar si login correcto
    LaunchedEffect(key1 = uiState.success) {
        if (uiState.success) {
            Toast.makeText(context, "Login correcto", Toast.LENGTH_SHORT).show()
            onLoginSuccess?.invoke()
            navController?.let { nc ->
                nc.navigate("dashboard") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(MaterialTheme.colors.background, MaterialTheme.colors.background)))) {

            Column(modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {

                Spacer(Modifier.height(60.dp))

                // Header (logo + title)
                Image(painter = painterResource(id = R.drawable.ic_motorcycle_animated), contentDescription = "logo",
                    modifier = Modifier.size(80.dp))
                Spacer(Modifier.height(12.dp))
                Text(text = "Moto Parts", color = MaterialTheme.colors.onBackground, fontSize = 28.sp)
                Text(text = "Repuestos y accesorios", color = MaterialTheme.colors.secondary, fontSize = 14.sp)

                Spacer(Modifier.height(32.dp))

                Card(modifier = Modifier.fillMaxWidth(), backgroundColor = MaterialTheme.colors.surface, border = BorderStroke(1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.12f)), shape = RoundedCornerShape(8.dp), elevation = 4.dp) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text(text = "Usuario o correo", color = MaterialTheme.colors.secondary) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = MaterialTheme.colors.primary,
                                unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
                                textColor = MaterialTheme.colors.onBackground,
                                focusedLabelColor = MaterialTheme.colors.primary,
                                unfocusedLabelColor = MaterialTheme.colors.secondary
                            )
                        )

                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text(text = "Contraseña", color = MaterialTheme.colors.secondary) },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = MaterialTheme.colors.primary,
                                unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
                                textColor = MaterialTheme.colors.onBackground,
                                focusedLabelColor = MaterialTheme.colors.primary,
                                unfocusedLabelColor = MaterialTheme.colors.secondary
                            )
                        )

                        Spacer(Modifier.height(16.dp))

                        if (uiState.loading) {
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(color = MaterialTheme.colors.primary)
                            }
                        } else {
                            Button(onClick = {
                                if (username.isBlank() || password.isBlank()) {
                                    Toast.makeText(context, "Completa usuario y contraseña", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                scope.launch {
                                    authViewModel.login(username, password)
                                }

                            }, modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp), colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)) {
                                Text(text = "Ingresar", color = MaterialTheme.colors.onPrimary)
                            }

                            Spacer(Modifier.height(12.dp))

                            // Social login separator
                            Divider(color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f), thickness = 1.dp)
                            Spacer(Modifier.height(12.dp))
                            Text(text = "o continuar con", color = MaterialTheme.colors.secondary, modifier = Modifier.align(Alignment.CenterHorizontally))
                            Spacer(Modifier.height(8.dp))

                            // Google button
                            OutlinedButton(onClick = { /* implementar google */ }, modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp), colors = ButtonDefaults.outlinedButtonColors(backgroundColor = MaterialTheme.colors.surface)) {
                                Image(painter = painterResource(id = R.drawable.ic_google), contentDescription = "google", modifier = Modifier.size(24.dp))
                                Spacer(Modifier.width(12.dp))
                                Text(text = "Continuar con Google", color = MaterialTheme.colors.onBackground)
                            }

                            Spacer(Modifier.height(8.dp))

                            Button(onClick = { /* implementar facebook */ }, modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp), colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF1877F2))) {
                                Image(painter = painterResource(id = R.drawable.ic_facebook), contentDescription = "facebook", modifier = Modifier.size(24.dp))
                                Spacer(Modifier.width(12.dp))
                                Text(text = "Continuar con Facebook", color = Color.White)
                            }

                            Spacer(Modifier.height(12.dp))

                            TextButton(onClick = { navController?.navigate("register") }, modifier = Modifier.fillMaxWidth()) {
                                Text(text = "Crear cuenta", color = MaterialTheme.colors.primary)
                            }
                        }
                    }
                }

                Spacer(Modifier.weight(1f))

                Text(text = "© Tu Empresa", color = MaterialTheme.colors.secondary, fontSize = 12.sp)
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun RegisterScreen(authViewModel: AuthViewModel, navController: NavController? = null, onRegisterSuccess: (() -> Unit)? = null) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var inviteCode by remember { mutableStateOf("") }

    val uiState by authViewModel.uiState.collectAsState()

    LaunchedEffect(key1 = uiState.errorMessage) {
        uiState.errorMessage?.let { msg ->
            Toast.makeText(context, "Registro falló: $msg", Toast.LENGTH_LONG).show()
            authViewModel.clearError()
        }
    }

    LaunchedEffect(key1 = uiState.success) {
        if (uiState.success) {
            Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
            onRegisterSuccess?.invoke()
            navController?.let { nc ->
                nc.navigate("dashboard") {
                    popUpTo("register") { inclusive = true }
                }
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(MaterialTheme.colors.background, MaterialTheme.colors.background)))) {

            Column(modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {

                Spacer(Modifier.height(40.dp))

                // Back button + Title
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController?.navigateUp() }) {
                        Icon(painter = painterResource(id = R.drawable.ic_arrow_back), contentDescription = "back", tint = MaterialTheme.colors.onBackground)
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(text = "Crear cuenta", color = MaterialTheme.colors.onBackground, fontSize = 24.sp)
                }

                Spacer(Modifier.height(12.dp))

                Card(modifier = Modifier.fillMaxWidth(), backgroundColor = MaterialTheme.colors.surface, border = BorderStroke(1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.12f)), shape = RoundedCornerShape(8.dp), elevation = 4.dp) {
                    Column(modifier = Modifier.padding(24.dp)) {

                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Usuario", color = MaterialTheme.colors.secondary) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = MaterialTheme.colors.primary,
                                unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
                                textColor = MaterialTheme.colors.onBackground
                            )
                        )

                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email", color = MaterialTheme.colors.secondary) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = MaterialTheme.colors.primary,
                                unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
                                textColor = MaterialTheme.colors.onBackground
                            )
                        )

                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Contraseña", color = MaterialTheme.colors.secondary) },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = MaterialTheme.colors.primary,
                                unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
                                textColor = MaterialTheme.colors.onBackground
                            )
                        )

                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text("Nombre", color = MaterialTheme.colors.secondary) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = MaterialTheme.colors.primary,
                                unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
                                textColor = MaterialTheme.colors.onBackground
                            )
                        )

                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(
                            value = apellido,
                            onValueChange = { apellido = it },
                            label = { Text("Apellido", color = MaterialTheme.colors.secondary) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = MaterialTheme.colors.primary,
                                unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
                                textColor = MaterialTheme.colors.onBackground
                            )
                        )

                        Spacer(Modifier.height(12.dp))

                        Text(text = "Código de invitación (opcional)", color = MaterialTheme.colors.primary)
                        Spacer(Modifier.height(8.dp))

                        OutlinedTextField(
                            value = inviteCode,
                            onValueChange = { inviteCode = it },
                            label = { Text("Código de invitación", color = MaterialTheme.colors.secondary) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = MaterialTheme.colors.primary,
                                unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
                                textColor = MaterialTheme.colors.onBackground
                            )
                        )

                        Spacer(Modifier.height(20.dp))

                        if (uiState.loading) {
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(color = MaterialTheme.colors.primary)
                            }
                        } else {
                            Button(onClick = {
                                // Validaciones: username, email, password, nombre, apellido
                                if (username.isBlank() || email.isBlank() || password.isBlank() || nombre.isBlank() || apellido.isBlank()) {
                                    Toast.makeText(context, "Rellena usuario, email, contraseña, nombre y apellido", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                scope.launch {
                                    val req = com.example.auth.RegisterRequest(
                                        username = username,
                                        email = email,
                                        password = password,
                                        inviteCode = if (inviteCode.isBlank()) null else inviteCode,
                                        nombre = nombre,
                                        apellido = apellido
                                    )
                                    authViewModel.register(req)
                                }

                            }, modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp), colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)) {
                                Text(text = "Crear cuenta", color = MaterialTheme.colors.onPrimary)
                            }

                            Spacer(Modifier.height(12.dp))

                            Text(text = "Al crear una cuenta aceptas los términos", color = MaterialTheme.colors.secondary, fontSize = 12.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
                        }

                    }
                }

                Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun DashboardScreen(authViewModel: AuthViewModel, navController: NavController, onLogout: (() -> Unit)? = null) {
    val nombre = authViewModel.getNombre()
    val apellido = authViewModel.getApellido()
    val username = authViewModel.getUsername()

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(MaterialTheme.colors.background, MaterialTheme.colors.background)))) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Spacer(Modifier.height(24.dp))
                Text(text = "Dashboard", color = MaterialTheme.colors.onBackground, fontSize = 24.sp)
                Spacer(Modifier.height(12.dp))
                Text(text = "${nombre.orEmpty()} ${apellido.orEmpty()}", color = MaterialTheme.colors.secondary)
                Text(text = username ?: "-", color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f))

                Spacer(Modifier.height(20.dp))

                Button(onClick = { navController.navigate("home") }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)) {
                    Text(text = "Ir a la tienda", color = MaterialTheme.colors.onPrimary)
                }

                Spacer(Modifier.weight(1f))

                Button(onClick = {
                    authViewModel.logout()
                    onLogout?.invoke()
                }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)) {
                    Text(text = "Cerrar sesión", color = MaterialTheme.colors.onPrimary)
                }
            }
        }
    }
}

// --- Pantallas de tienda básicas ---
@Composable
fun HomeScreen(navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(MaterialTheme.colors.background, MaterialTheme.colors.background)))) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Text(text = "Tienda", color = MaterialTheme.colors.onBackground, fontSize = 24.sp)
                Spacer(Modifier.height(12.dp))

                // Lista ejemplo de productos (estáticos)
                val products = listOf("Cadena de transmisión", "Filtro de aire", "Bujía", "Neumático trasero")
                products.forEachIndexed { index, item ->
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable { navController.navigate("product/${index}") }, backgroundColor = MaterialTheme.colors.surface, border = BorderStroke(1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.12f)), elevation = 4.dp) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Image(painter = painterResource(id = R.drawable.ic_motorcycle_animated), contentDescription = "prod", modifier = Modifier.size(48.dp))
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(text = item, color = MaterialTheme.colors.onBackground)
                                Text(text = "Descripción breve...", color = MaterialTheme.colors.secondary, fontSize = 12.sp)
                            }
                        }
                    }
                }

                Spacer(Modifier.weight(1f))
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = { navController.navigate("cart") }, colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)) {
                        Text(text = "Carrito", color = MaterialTheme.colors.onPrimary)
                    }
                    Button(onClick = { navController.navigate("categories") }, colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)) {
                        Text(text = "Categorías", color = MaterialTheme.colors.onPrimary)
                    }
                }
            }
        }
    }
}

@Composable
fun ProductDetailScreen(productId: String?, navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(MaterialTheme.colors.background, MaterialTheme.colors.background)))) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Text(text = "Detalle del producto", color = MaterialTheme.colors.onBackground, fontSize = 24.sp)
                Spacer(Modifier.height(12.dp))
                Text(text = "ID: ${productId ?: "-"}", color = MaterialTheme.colors.secondary)

                Spacer(Modifier.weight(1f))
                Button(onClick = { navController.navigate("cart") }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)) {
                    Text(text = "Añadir al carrito", color = MaterialTheme.colors.onPrimary)
                }
            }
        }
    }
}

@Composable
fun CartScreen(navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(MaterialTheme.colors.background, MaterialTheme.colors.background)))) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Text(text = "Carrito", color = MaterialTheme.colors.onBackground, fontSize = 24.sp)
                Spacer(Modifier.height(12.dp))
                Text(text = "Tu carrito está vacío (placeholder)", color = MaterialTheme.colors.secondary)

                Spacer(Modifier.weight(1f))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(onClick = { navController.navigate("home") }, modifier = Modifier.weight(1f)) {
                        Text(text = "Seguir comprando", color = MaterialTheme.colors.primary)
                    }
                    Button(onClick = { /* ir a checkout */ }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)) {
                        Text(text = "Finalizar compra", color = MaterialTheme.colors.onPrimary)
                    }
                }
            }
        }
    }
}

@Composable
fun SearchScreen(navController: NavController) {
    var query by remember { mutableStateOf("") }
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(MaterialTheme.colors.background, MaterialTheme.colors.background)))) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Text(text = "Buscar", color = MaterialTheme.colors.onBackground, fontSize = 24.sp)
                Spacer(Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = query, onValueChange = { query = it }, label = { Text("Buscar productos", color = MaterialTheme.colors.secondary) }, modifier = Modifier.weight(1f))
                    Button(onClick = { /* aquí podrías navegar a resultados reales */ navController.navigate("home") }, colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)) {
                        Text(text = "Buscar", color = MaterialTheme.colors.onPrimary)
                    }
                }
            }
        }
    }
}

@Composable
fun CategoriesScreen(navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(MaterialTheme.colors.background, MaterialTheme.colors.background)))) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Text(text = "Categorías", color = MaterialTheme.colors.onBackground, fontSize = 24.sp)
                Spacer(Modifier.height(12.dp))
                val cats = listOf("Motor", "Transmisión", "Frenos", "Ruedas")
                cats.forEach { c ->
                    Text(text = c, color = MaterialTheme.colors.onBackground, modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { navController.navigate("home") })
                }
            }
        }
    }
}

@Composable
fun AppNavHost(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
    val startDestination = if (isAuthenticated) "dashboard" else "login"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            LoginScreen(authViewModel = authViewModel, navController = navController)
        }
        composable("register") {
            RegisterScreen(authViewModel = authViewModel, navController = navController)
        }
        composable("dashboard") {
            DashboardScreen(authViewModel = authViewModel, navController = navController, onLogout = {
                navController.navigate("login") {
                    popUpTo("dashboard") { inclusive = true }
                }
            })
        }
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("product/{productId}") { backStackEntry ->
            val pid = backStackEntry.arguments?.getString("productId")
            ProductDetailScreen(productId = pid, navController = navController)
        }
        composable("cart") {
            CartScreen(navController = navController)
        }
        composable("search") {
            SearchScreen(navController = navController)
        }
        composable("categories") {
            CategoriesScreen(navController = navController)
        }
    }
}
