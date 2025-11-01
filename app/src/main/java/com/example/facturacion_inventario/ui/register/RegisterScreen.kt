package com.example.facturacion_inventario.ui.register

import android.widget.Toast
import android.util.Patterns
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
import com.example.data.auth.RegisterRequest
import kotlinx.coroutines.launch
import com.example.facturacion_inventario.ui.components.shared.AuthHeader
import com.example.facturacion_inventario.ui.components.shared.InputField
import com.example.facturacion_inventario.ui.components.controls.PrimaryButton
import com.example.facturacion_inventario.ui.components.controls.SecondaryTextButton
import com.example.facturacion_inventario.ui.components.controls.SocialButton
import com.example.facturacion_inventario.ui.components.controls.AuthCard

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
                nc.navigate("store") {
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

                // Reemplazamos la Card estática por AuthCard y InputField reutilizables
                AuthCard {
                    InputField(value = username, onValueChange = { username = it }, labelText = "Usuario")
                    Spacer(Modifier.height(12.dp))
                    InputField(value = email, onValueChange = { email = it }, labelText = "Email")
                    Spacer(Modifier.height(12.dp))
                    InputField(value = password, onValueChange = { password = it }, labelText = "Contraseña", isPassword = true)
                    Spacer(Modifier.height(12.dp))
                    InputField(value = nombre, onValueChange = { nombre = it }, labelText = "Nombre")
                    Spacer(Modifier.height(12.dp))
                    InputField(value = apellido, onValueChange = { apellido = it }, labelText = "Apellido")

                    Spacer(Modifier.height(12.dp))

                    Text(text = "Código de invitación (opcional)", color = MaterialTheme.colors.primary)
                    Spacer(Modifier.height(8.dp))

                    InputField(value = inviteCode, onValueChange = { inviteCode = it }, labelText = "Código de invitación")

                    Spacer(Modifier.height(20.dp))

                    if (uiState.loading) {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = MaterialTheme.colors.primary)
                        }
                    } else {
                        PrimaryButton(onClick = {
                            // Validaciones: username, email, password, nombre, apellido
                            if (username.isBlank() || email.isBlank() || password.isBlank() || nombre.isBlank() || apellido.isBlank()) {
                                Toast.makeText(context, "Rellena usuario, email, contraseña, nombre y apellido", Toast.LENGTH_SHORT).show()
                                return@PrimaryButton
                            }
                            // Validar formato de email
                            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                Toast.makeText(context, "Ingresa un email válido", Toast.LENGTH_SHORT).show()
                                return@PrimaryButton
                            }

                            // Longitud mínima de contraseña
                            if (password.length < 6) {
                                Toast.makeText(context, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                                return@PrimaryButton
                            }
                            scope.launch {
                                val req = RegisterRequest(
                                    username = username,
                                    email = email,
                                    password = password,
                                    inviteCode = if (inviteCode.isBlank()) null else inviteCode,
                                    nombre = nombre,
                                    apellido = apellido
                                )
                                authViewModel.register(req)
                            }

                        }, text = "Crear cuenta")

                        Spacer(Modifier.height(12.dp))

                        Text(text = "Al crear una cuenta aceptas los términos", color = MaterialTheme.colors.secondary, fontSize = 12.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
                    }

                }

                Spacer(Modifier.weight(1f))
            }
        }
    }
}
