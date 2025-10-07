package com.example.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.facturacion_inventario.ui.RegisterScreen
import com.example.facturacion_inventario.ui.AppTheme
import com.example.auth.AuthRepository
import com.example.auth.AuthViewModelFactory
import com.example.auth.AuthViewModel

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inyectar repositorio y crear ViewModel
        val repo = AuthRepository(this, "http://10.0.2.2:8080/")
        val factory = AuthViewModelFactory(application, repo)
        val authViewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]

        setContent {
            AppTheme {
                RegisterScreen(authViewModel = authViewModel, onRegisterSuccess = { finish() })
            }
        }
    }
}