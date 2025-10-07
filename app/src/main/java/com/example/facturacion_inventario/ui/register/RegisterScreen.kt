package com.example.facturacion_inventario.ui.register

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
