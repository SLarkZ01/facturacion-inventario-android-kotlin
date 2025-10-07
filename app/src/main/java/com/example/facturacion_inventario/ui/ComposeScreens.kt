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
import com.example.auth.AuthRepository
import com.example.auth.TokenStorage
import com.example.facturacion_inventario.R

// Fondo blanco puro y tarjetas blancas diferenciadas por borde sutil y elevación mayor
private val BackgroundTop = Color(0xFFFFFFFF) // blanco puro
private val BackgroundBottom = Color(0xFFFFFFFF) // blanco puro
private val CardColor = Color(0xFFFFFFFF) // tarjetas blancas
private val CardBorder = Color(0xFFDBDBDB) // borde sutil pero más visible para separar tarjeta del fondo
private val Accent = Color(0xFF0B3D91) // azul oscuro
private val DarkText = Color(0xFF0F1724) // texto principal oscuro
private val SubtleText = Color(0xFF6B7280) // subtítulo / labels

@Composable
fun LoginScreen(navController: NavController? = null, onLoginSuccess: (() -> Unit)? = null) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(BackgroundTop, BackgroundBottom)))) {

            Column(modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {

                Spacer(Modifier.height(60.dp))

                // Header (logo + title)
                Image(painter = painterResource(id = R.drawable.ic_motorcycle_animated), contentDescription = "logo",
                    modifier = Modifier.size(80.dp))
                Spacer(Modifier.height(12.dp))
                Text(text = "Moto Parts", color = DarkText, fontSize = 28.sp)
                Text(text = "Repuestos y accesorios", color = SubtleText, fontSize = 14.sp)

                Spacer(Modifier.height(32.dp))

                Card(modifier = Modifier.fillMaxWidth(), backgroundColor = CardColor, border = BorderStroke(1.dp, CardBorder), shape = RoundedCornerShape(8.dp), elevation = 4.dp) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text(text = "Usuario o correo", color = SubtleText) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Accent,
                                unfocusedBorderColor = Color(0xFFDDDDDF),
                                textColor = DarkText,
                                focusedLabelColor = Accent,
                                unfocusedLabelColor = SubtleText
                            )
                        )

                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text(text = "Contraseña", color = SubtleText) },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Accent,
                                unfocusedBorderColor = Color(0xFFDDDDDF),
                                textColor = DarkText,
                                focusedLabelColor = Accent,
                                unfocusedLabelColor = SubtleText
                            )
                        )

                        Spacer(Modifier.height(16.dp))

                        if (loading) {
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(color = Accent)
                            }
                        } else {
                            Button(onClick = {
                                if (username.isBlank() || password.isBlank()) {
                                    Toast.makeText(context, "Completa usuario y contraseña", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                loading = true
                                scope.launch {
                                    try {
                                        val repo = AuthRepository(context, "http://10.0.2.2:8080/")
                                        val resp = repo.login(username, password)
                                        val access = resp?.accessTokenNormalized
                                        val refresh = resp?.refreshTokenNormalized
                                        if (!access.isNullOrEmpty()) TokenStorage.setAccessToken(context, access)
                                        if (!refresh.isNullOrEmpty()) TokenStorage.setRefreshToken(context, refresh)
                                        Toast.makeText(context, "Login correcto", Toast.LENGTH_SHORT).show()
                                        onLoginSuccess?.invoke()
                                        navController?.let { nc ->
                                            nc.navigate("dashboard") {
                                                popUpTo("login") { inclusive = true }
                                            }
                                        }
                                    } catch (ex: Exception) {
                                        Toast.makeText(context, "Login falló: ${ex.message}", Toast.LENGTH_LONG).show()
                                    } finally {
                                        loading = false
                                    }
                                }

                            }, modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp), colors = ButtonDefaults.buttonColors(backgroundColor = Accent)) {
                                Text(text = "Ingresar", color = Color.White)
                            }

                            Spacer(Modifier.height(12.dp))

                            // Social login separator
                            Divider(color = Color(0xFFDDDDDD), thickness = 1.dp)
                            Spacer(Modifier.height(12.dp))
                            Text(text = "o continuar con", color = SubtleText, modifier = Modifier.align(Alignment.CenterHorizontally))
                            Spacer(Modifier.height(8.dp))

                            // Google button
                            OutlinedButton(onClick = { /* implementar google */ }, modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp), colors = ButtonDefaults.outlinedButtonColors(backgroundColor = CardColor)) {
                                Image(painter = painterResource(id = R.drawable.ic_google), contentDescription = "google", modifier = Modifier.size(24.dp))
                                Spacer(Modifier.width(12.dp))
                                Text(text = "Continuar con Google", color = DarkText)
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
                                Text(text = "Crear cuenta", color = Accent)
                            }
                        }
                    }
                }

                Spacer(Modifier.weight(1f))

                Text(text = "© Tu Empresa", color = SubtleText, fontSize = 12.sp)
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun RegisterScreen(navController: NavController? = null, onRegisterSuccess: (() -> Unit)? = null) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var inviteCode by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(BackgroundTop, BackgroundBottom)))) {

            Column(modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {

                Spacer(Modifier.height(40.dp))

                // Back button + Title
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController?.navigateUp() }) {
                        Icon(painter = painterResource(id = R.drawable.ic_arrow_back), contentDescription = "back", tint = DarkText)
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(text = "Crear cuenta", color = DarkText, fontSize = 24.sp)
                }

                Spacer(Modifier.height(12.dp))

                Card(modifier = Modifier.fillMaxWidth(), backgroundColor = CardColor, border = BorderStroke(1.dp, CardBorder), shape = RoundedCornerShape(8.dp), elevation = 4.dp) {
                    Column(modifier = Modifier.padding(24.dp)) {

                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Usuario", color = SubtleText) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Accent,
                                unfocusedBorderColor = Color(0xFFDDDDDF),
                                textColor = DarkText
                            )
                        )

                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email", color = SubtleText) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Accent,
                                unfocusedBorderColor = Color(0xFFDDDDDF),
                                textColor = DarkText
                            )
                        )

                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Contraseña", color = SubtleText) },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Accent,
                                unfocusedBorderColor = Color(0xFFDDDDDF),
                                textColor = DarkText
                            )
                        )

                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text("Nombre", color = SubtleText) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Accent,
                                unfocusedBorderColor = Color(0xFFDDDDDF),
                                textColor = DarkText
                            )
                        )

                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(
                            value = apellido,
                            onValueChange = { apellido = it },
                            label = { Text("Apellido", color = SubtleText) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Accent,
                                unfocusedBorderColor = Color(0xFFDDDDDF),
                                textColor = DarkText
                            )
                        )

                        Spacer(Modifier.height(12.dp))

                        Text(text = "Código de invitación (opcional)", color = Accent)
                        Spacer(Modifier.height(8.dp))

                        OutlinedTextField(
                            value = inviteCode,
                            onValueChange = { inviteCode = it },
                            label = { Text("Código de invitación", color = SubtleText) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Accent,
                                unfocusedBorderColor = Color(0xFFDDDDDF),
                                textColor = DarkText
                            )
                        )

                        Spacer(Modifier.height(20.dp))

                        if (loading) {
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(color = Accent)
                            }
                        } else {
                            Button(onClick = {
                                // Validaciones: username, email, password, nombre, apellido
                                if (username.isBlank() || email.isBlank() || password.isBlank() || nombre.isBlank() || apellido.isBlank()) {
                                    Toast.makeText(context, "Rellena usuario, email, contraseña, nombre y apellido", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                loading = true
                                scope.launch {
                                    try {
                                        val repo = AuthRepository(context, "http://10.0.2.2:8080/")
                                        val req = com.example.auth.RegisterRequest(
                                            username = username,
                                            email = email,
                                            password = password,
                                            inviteCode = if (inviteCode.isBlank()) null else inviteCode,
                                            nombre = nombre,
                                            apellido = apellido
                                        )
                                        val resp = repo.register(req)
                                        val access = resp?.accessTokenNormalized
                                        val refresh = resp?.refreshTokenNormalized
                                        if (!access.isNullOrEmpty()) TokenStorage.setAccessToken(context, access)
                                        if (!refresh.isNullOrEmpty()) TokenStorage.setRefreshToken(context, refresh)

                                        // Guardar info del usuario localmente
                                        TokenStorage.setUserInfo(context, username, nombre, apellido)

                                        Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                                        onRegisterSuccess?.invoke()
                                        navController?.let { nc ->
                                            nc.navigate("dashboard") {
                                                popUpTo("register") { inclusive = true }
                                            }
                                        }
                                    } catch (ex: Exception) {
                                        Toast.makeText(context, "Registro fallido: ${ex.message}", Toast.LENGTH_LONG).show()
                                    } finally {
                                        loading = false
                                    }
                                }

                            }, modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp), colors = ButtonDefaults.buttonColors(backgroundColor = Accent)) {
                                Text(text = "Crear cuenta", color = Color.White)
                            }

                            Spacer(Modifier.height(12.dp))

                            Text(text = "Al crear una cuenta aceptas los términos", color = SubtleText, fontSize = 12.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
                        }

                    }
                }

                Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun DashboardScreen(navController: NavController, onLogout: (() -> Unit)? = null) {
    val context = LocalContext.current
    val nombre = TokenStorage.getNombre(context)
    val apellido = TokenStorage.getApellido(context)
    val username = TokenStorage.getUsername(context)

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(BackgroundTop, BackgroundBottom)))) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Spacer(Modifier.height(24.dp))
                Text(text = "Dashboard", color = DarkText, fontSize = 24.sp)
                Spacer(Modifier.height(12.dp))
                Text(text = "${nombre.orEmpty()} ${apellido.orEmpty()}", color = SubtleText)
                Text(text = username ?: "-", color = Color(0xFF9CA3AF))

                Spacer(Modifier.height(20.dp))

                Button(onClick = { navController.navigate("home") }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(backgroundColor = Accent)) {
                    Text(text = "Ir a la tienda", color = Color.White)
                }

                Spacer(Modifier.weight(1f))

                Button(onClick = {
                    TokenStorage.clear(context)
                    onLogout?.invoke()
                }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(backgroundColor = Accent)) {
                    Text(text = "Cerrar sesión", color = Color.White)
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
            .background(brush = Brush.verticalGradient(listOf(BackgroundTop, BackgroundBottom)))) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Text(text = "Tienda", color = DarkText, fontSize = 24.sp)
                Spacer(Modifier.height(12.dp))

                // Lista ejemplo de productos (estáticos)
                val products = listOf("Cadena de transmisión", "Filtro de aire", "Bujía", "Neumático trasero")
                products.forEachIndexed { index, item ->
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable { navController.navigate("product/${index}") }, backgroundColor = CardColor, border = BorderStroke(1.dp, CardBorder), elevation = 4.dp) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Image(painter = painterResource(id = R.drawable.ic_motorcycle_animated), contentDescription = "prod", modifier = Modifier.size(48.dp))
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(text = item, color = DarkText)
                                Text(text = "Descripción breve...", color = SubtleText, fontSize = 12.sp)
                            }
                        }
                    }
                }

                Spacer(Modifier.weight(1f))
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = { navController.navigate("cart") }, colors = ButtonDefaults.buttonColors(backgroundColor = Accent)) {
                        Text(text = "Carrito", color = Color.White)
                    }
                    Button(onClick = { navController.navigate("categories") }, colors = ButtonDefaults.buttonColors(backgroundColor = Accent)) {
                        Text(text = "Categorías", color = Color.White)
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
            .background(brush = Brush.verticalGradient(listOf(BackgroundTop, BackgroundBottom)))) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Text(text = "Detalle del producto", color = DarkText, fontSize = 24.sp)
                Spacer(Modifier.height(12.dp))
                Text(text = "ID: ${productId ?: "-"}", color = SubtleText)

                Spacer(Modifier.weight(1f))
                Button(onClick = { navController.navigate("cart") }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(backgroundColor = Accent)) {
                    Text(text = "Añadir al carrito", color = Color.White)
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
            .background(brush = Brush.verticalGradient(listOf(BackgroundTop, BackgroundBottom)))) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Text(text = "Carrito", color = DarkText, fontSize = 24.sp)
                Spacer(Modifier.height(12.dp))
                Text(text = "Tu carrito está vacío (placeholder)", color = SubtleText)

                Spacer(Modifier.weight(1f))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(onClick = { navController.navigate("home") }, modifier = Modifier.weight(1f)) {
                        Text(text = "Seguir comprando", color = Accent)
                    }
                    Button(onClick = { /* ir a checkout */ }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(backgroundColor = Accent)) {
                        Text(text = "Finalizar compra", color = Color.White)
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
            .background(brush = Brush.verticalGradient(listOf(BackgroundTop, BackgroundBottom)))) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Text(text = "Buscar", color = DarkText, fontSize = 24.sp)
                Spacer(Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = query, onValueChange = { query = it }, label = { Text("Buscar productos", color = SubtleText) }, modifier = Modifier.weight(1f))
                    Button(onClick = { /* aquí podrías navegar a resultados reales */ navController.navigate("home") }, colors = ButtonDefaults.buttonColors(backgroundColor = Accent)) {
                        Text(text = "Buscar", color = Color.White)
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
            .background(brush = Brush.verticalGradient(listOf(BackgroundTop, BackgroundBottom)))) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Text(text = "Categorías", color = DarkText, fontSize = 24.sp)
                Spacer(Modifier.height(12.dp))
                val cats = listOf("Motor", "Transmisión", "Frenos", "Ruedas")
                cats.forEach { c ->
                    Text(text = c, color = DarkText, modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { navController.navigate("home") })
                }
            }
        }
    }
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val token = TokenStorage.getAccessToken(context)
    val startDestination = if (!token.isNullOrEmpty()) "dashboard" else "login"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("register") {
            RegisterScreen(navController = navController)
        }
        composable("dashboard") {
            DashboardScreen(navController = navController, onLogout = {
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
