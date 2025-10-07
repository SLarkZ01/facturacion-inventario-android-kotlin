package com.example.facturacion_inventario.ui.login

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
                                // Si se nos pasó navController (modo navegación interna), simulamos éxito y vamos a la tienda
                                navController?.let { nc ->
                                    nc.navigate("store") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                    return@Button
                                }

                                // En caso de uso fuera de la navegación (LoginActivity), usar el ViewModel real
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
                                .height(48.dp), colors = ButtonDefaults.buttonColors(backgroundColor = androidx.compose.ui.graphics.Color(0xFF1877F2))) {
                                Image(painter = painterResource(id = R.drawable.ic_facebook), contentDescription = "facebook", modifier = Modifier.size(24.dp))
                                Spacer(Modifier.width(12.dp))
                                Text(text = "Continuar con Facebook", color = androidx.compose.ui.graphics.Color.White)
                            }

                            Spacer(Modifier.height(12.dp))

                            TextButton(onClick = { navController?.navigate("register") }, modifier = Modifier.fillMaxWidth()) {
                                Text(text = "Crear cuenta", color = MaterialTheme.colors.primary)
                            }

                            Spacer(Modifier.height(8.dp))

                            // Nuevo: botón para omitir inicio de sesión y continuar como invitado
                            TextButton(onClick = {
                                // marcar skip y navegar al store
                                authViewModel.skipLogin()
                                onLoginSuccess?.invoke()
                                navController?.let { nc ->
                                    nc.navigate("store") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            }, modifier = Modifier.fillMaxWidth()) {
                                Text(text = "Omitir inicio de sesión", color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f))
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
