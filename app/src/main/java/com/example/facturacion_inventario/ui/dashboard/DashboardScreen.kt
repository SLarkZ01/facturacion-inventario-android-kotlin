package com.example.facturacion_inventario.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.auth.AuthViewModel

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

