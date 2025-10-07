package com.example.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.facturacion_inventario.ui.LoginScreen

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Usar Compose para la UI de login (mantener compatibilidad con llamadas que inician esta actividad)
        setContent {
            LoginScreen(onLoginSuccess = { finish() })
        }
    }
}
