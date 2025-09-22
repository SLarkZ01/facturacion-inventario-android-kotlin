package com.example.facturacion_inventario

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.example.auth.TokenStorage

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        // Si ya hay token, ir al dashboard limpiando login del back stack
        val token = TokenStorage.getAccessToken(this)
        if (!token.isNullOrEmpty()) {
            val navController = findNavController(R.id.nav_host_fragment)
            val options = NavOptions.Builder()
                .setPopUpTo(R.id.loginFragment, true)
                .build()
            // Si ya estamos en dashboard, no navegar de nuevo
            val current = navController.currentDestination?.id
            if (current != R.id.dashboardFragment) {
                navController.navigate(R.id.dashboardFragment, null, options)
            }
        }
    }
}