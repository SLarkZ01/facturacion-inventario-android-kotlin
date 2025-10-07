package com.example.facturacion_inventario

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.facturacion_inventario.ui.nav.AppNavHost
import com.example.facturacion_inventario.ui.theme.AppTheme
import com.example.auth.AuthViewModel
import com.example.auth.AuthViewModelFactory
import com.example.data.auth.AuthRepository
import com.example.data.auth.ApiConfig

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Crear repositorio concreto e inyectarlo en la fábrica del ViewModel
        val repo = AuthRepository(this, ApiConfig.BASE_URL)
        val factory = AuthViewModelFactory(application, repo)
        val authViewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]

        setContent {
            AppTheme {
                AppNavHost(authViewModel)
            }
        }
    }
}