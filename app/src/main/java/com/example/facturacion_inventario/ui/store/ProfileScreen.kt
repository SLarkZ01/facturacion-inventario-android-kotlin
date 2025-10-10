package com.example.facturacion_inventario.ui.store

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.auth.AuthViewModel
import com.example.facturacion_inventario.R
import androidx.compose.ui.text.font.FontWeight

@Composable
fun ProfileScreen(authViewModel: AuthViewModel, rootNavController: NavController) {
    val nombre = authViewModel.getNombre()
    val apellido = authViewModel.getApellido()
    val username = authViewModel.getUsername()

    // Estado: si el usuario decidió omitir el inicio de sesión
    val skipped by authViewModel.skippedLogin.collectAsState()

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {

            Spacer(modifier = Modifier.height(12.dp))

            // Card de perfil
            Card(elevation = 6.dp, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), verticalAlignment = Alignment.CenterVertically) {

                    // Avatar / icono
                    Image(painter = painterResource(id = R.drawable.ic_person), contentDescription = "avatar",
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(36.dp)))

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = if (!nombre.isNullOrBlank() || !apellido.isNullOrBlank()) "${nombre.orEmpty()} ${apellido.orEmpty()}" else "Usuario", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colors.onBackground)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "@${username ?: "invitado"}", color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Acciones principales
            if (skipped) {
                Text(text = "Estás usando la aplicación como invitado", color = MaterialTheme.colors.onSurface, modifier = Modifier.padding(bottom = 8.dp))
                Button(onClick = { rootNavController.navigate("login") }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)) {
                    Text(text = "Iniciar sesión", color = MaterialTheme.colors.onPrimary)
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(onClick = { rootNavController.navigate("register") }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Crear cuenta")
                }
            } else {
                // Acciones para usuario autenticado
                OutlinedButton(onClick = { /* navegar a inventario */ rootNavController.navigate("home") }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Ver inventario")
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(onClick = { /* navegar a documentos/facturas */ rootNavController.navigate("cart") }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Ver facturas / Documentos")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    // Cerrar sesión y volver a login en el nav principal
                    authViewModel.logout()
                    rootNavController.navigate("login") {
                        popUpTo("store") { inclusive = true }
                    }
                }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)) {
                    Text(text = "Cerrar sesión", color = MaterialTheme.colors.onPrimary)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Pie de página
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(text = "", color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f), fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
