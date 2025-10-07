package com.example.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.facturacion_inventario.ui.LoginScreen
import com.example.facturacion_inventario.ui.AppTheme
import com.example.auth.AuthRepository
import com.example.auth.AuthViewModelFactory
import com.example.auth.AuthViewModel

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inyectar repositorio y crear ViewModel
        val repo = AuthRepository(this, "http://10.0.2.2:8080/")
        val factory = AuthViewModelFactory(application, repo)
        val authViewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]

        // Usar Compose para la UI de login (mantener compatibilidad con llamadas que inician esta actividad)
        setContent {
            AppTheme {
                LoginScreen(authViewModel = authViewModel, onLoginSuccess = { finish() })
            }
        }
    }
}
