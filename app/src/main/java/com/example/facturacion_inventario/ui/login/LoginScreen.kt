package com.example.facturacion_inventario.ui.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.facturacion_inventario.R
import com.example.auth.AuthViewModel
import kotlinx.coroutines.launch
import com.example.facturacion_inventario.ui.components.*
import androidx.compose.ui.graphics.Color

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
                nc.navigate("store") {
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

                // Header (logo + title) reutilizando AuthHeader
                AuthHeader(title = "Moto Parts", subtitle = "Repuestos y accesorios")

                Spacer(Modifier.height(32.dp))

                AuthCard {
                    // Campos de input reutilizables
                    InputField(value = username, onValueChange = { username = it }, labelText = "Usuario o correo")
                    Spacer(Modifier.height(12.dp))
                    InputField(value = password, onValueChange = { password = it }, labelText = "Contraseña", isPassword = true)

                    Spacer(Modifier.height(16.dp))

                    if (uiState.loading) {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = MaterialTheme.colors.primary)
                        }
                    } else {
                        PrimaryButton(text = "Ingresar", onClick = {
                            if (username.isBlank() || password.isBlank()) {
                                Toast.makeText(context, "Completa usuario y contraseña", Toast.LENGTH_SHORT).show()
                                return@PrimaryButton
                            }
                            scope.launch {
                                authViewModel.login(username, password)
                            }
                        })

                        Spacer(Modifier.height(12.dp))

                        Divider(color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f), thickness = 1.dp)
                        Spacer(Modifier.height(12.dp))
                        Text(text = "o continuar con", color = MaterialTheme.colors.secondary, modifier = Modifier.align(Alignment.CenterHorizontally))
                        Spacer(Modifier.height(8.dp))

                        SocialButton(text = "Continuar con Google", iconRes = R.drawable.ic_google, onClick = { /* implementar google */ })
                        Spacer(Modifier.height(8.dp))
                        // Facebook con color propio
                        SocialButton(text = "Continuar con Facebook", iconRes = R.drawable.ic_facebook, onClick = { /* implementar facebook */ }, background = Color(0xFF1877F2))

                        Spacer(Modifier.height(12.dp))

                        SecondaryTextButton(text = "Crear cuenta", onClick = { navController?.navigate("register") })

                        Spacer(Modifier.height(8.dp))

                        SecondaryTextButton(text = "Omitir inicio de sesión", onClick = {
                            authViewModel.skipLogin()
                            onLoginSuccess?.invoke()
                            navController?.let { nc ->
                                nc.navigate("store") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        })
                    }
                }

                Spacer(Modifier.weight(1f))

                Text(text = "© Tu Empresa", color = MaterialTheme.colors.secondary, fontSize = 12.sp)
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}
