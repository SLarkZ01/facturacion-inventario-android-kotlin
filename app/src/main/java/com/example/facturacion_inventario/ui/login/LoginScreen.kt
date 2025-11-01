package com.example.facturacion_inventario.ui.login

import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.facturacion_inventario.R
import com.example.auth.AuthViewModel
import kotlinx.coroutines.launch
import com.example.facturacion_inventario.ui.components.shared.AuthHeader
import com.example.facturacion_inventario.ui.components.shared.InputField
import com.example.facturacion_inventario.ui.components.controls.PrimaryButton
import com.example.facturacion_inventario.ui.components.controls.SecondaryTextButton
import com.example.facturacion_inventario.ui.components.controls.SocialButton
import com.example.facturacion_inventario.ui.components.controls.AuthCard
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.LocalActivity
import android.app.Activity
import android.content.Intent
import com.example.facturacion_inventario.BuildConfig

@Composable
fun LoginScreen(authViewModel: AuthViewModel, navController: NavController? = null, onLoginSuccess: (() -> Unit)? = null) {
    val context = LocalContext.current
    val activity = LocalActivity.current
    val scope = rememberCoroutineScope()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val uiState by authViewModel.uiState.collectAsState()

    // GoogleSignInClient setup
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestIdToken(BuildConfig.GOOGLE_SERVER_CLIENT_ID)
        .build()

    val googleSignInClient: GoogleSignInClient? = activity?.let { GoogleSignIn.getClient(it, gso) }

    // Launcher para recibir el resultado del intent de Google
    val googleLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(Exception::class.java)
                val idToken = account?.idToken
                // Aquí llamamos al ViewModel para manejar el token si tu backend lo requiere
                if (!idToken.isNullOrBlank()) {
                    scope.launch {
                        // Usa la API del repo: oauthGoogle
                        // authViewModel debería exponer una función que llamara a repo.oauthGoogle
                        // Si no existe, puedes implementar un handler en el ViewModel. Aquí invocamos un metodo esperado
                        try {
                            val method = authViewModel::class.java.getMethod("loginWithGoogle", String::class.java)
                            method.invoke(authViewModel, idToken)
                        } catch (_: NoSuchMethodException) {
                            // Si no existe, llamamos a la función estándar del repo vía login usando token (si tu ViewModel la expone en otro nombre, ajústalo)
                            // Por ahora solo mostramos un mensaje y delegamos al desarrollador implementar el handler en AuthViewModel
                            Toast.makeText(context, "Token de Google recibido, implementa loginWithGoogle en AuthViewModel", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "No se obtuvo token de Google", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Google sign-in falló: ${e.message}", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(context, "Google sign-in cancelado", Toast.LENGTH_SHORT).show()
        }
    }

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

                        // Botón Google: mantiene ícono y texto
                        SocialButton(text = "Continuar con Google", iconRes = R.drawable.ic_google, onClick = {
                            val signInIntent: Intent? = googleSignInClient?.signInIntent
                            if (signInIntent != null) {
                                googleLauncher.launch(signInIntent)
                            } else {
                                Toast.makeText(context, "No se pudo inicializar Google Sign-In", Toast.LENGTH_LONG).show()
                            }
                        })

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
