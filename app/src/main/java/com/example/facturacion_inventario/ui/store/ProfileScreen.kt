package com.example.facturacion_inventario.ui.store

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.auth.AuthViewModel

@Composable
fun ProfileScreen(authViewModel: AuthViewModel, rootNavController: NavController) {
    val nombre = authViewModel.getNombre()
    val apellido = authViewModel.getApellido()
    val username = authViewModel.getUsername()

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Perfil", style = MaterialTheme.typography.h5, color = MaterialTheme.colors.onBackground)
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Nombre: ${nombre.orEmpty()} ${apellido.orEmpty()}", color = MaterialTheme.colors.onSurface)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Usuario: ${username ?: "-"}", color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f))

            Spacer(modifier = Modifier.weight(1f))

            Button(onClick = {
                // Cerrar sesión y volver a login en el nav principal
                authViewModel.logout()
                rootNavController.navigate("login") {
                    popUpTo("store") { inclusive = true }
                }
            }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)) {
                Text(text = "Cerrar sesión", color = MaterialTheme.colors.onPrimary)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
