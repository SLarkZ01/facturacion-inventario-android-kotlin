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
import com.example.facturacion_inventario.data.remote.api.RetrofitClient
import androidx.core.view.WindowCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Instalar el SplashScreen antes de super.onCreate
        installSplashScreen()

        super.onCreate(savedInstanceState)

        // Permitir que el contenido se dibuje detrás de las system bars (status/navigation)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Inicializar RetrofitClient con contexto para autenticación JWT
        RetrofitClient.initialize(applicationContext)

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