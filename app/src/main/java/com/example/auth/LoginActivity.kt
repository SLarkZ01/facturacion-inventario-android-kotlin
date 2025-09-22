package com.example.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class LoginActivity : AppCompatActivity() {
    private lateinit var repo: AuthRepository
    private val isLoggingIn = AtomicBoolean(false)

    // Bandera de debug: si es true, limpiará tokens locales antes de intentar login
    private val DEBUG_FORCE_CLEAR_TOKENS = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.facturacion_inventario.R.layout.activity_login)

        // Inicializar repo con ApiClient que usa TokenStorage
        try {
            repo = AuthRepository(this, "http://10.0.2.2:8080/")
        } catch (ex: Exception) {
            Log.e("LoginActivity", "Error inicializando AuthRepository", ex)
            Toast.makeText(this, "Error interno al inicializar: ${ex.message}", Toast.LENGTH_LONG).show()
            // Evitar crash: devolver un repositorio fallback que lanza excepciones controladas
            repo = AuthRepository("http://10.0.2.2:8080/")
        }

        val editUser = findViewById<EditText>(com.example.facturacion_inventario.R.id.etUsername)
        val editPass = findViewById<EditText>(com.example.facturacion_inventario.R.id.etPassword)
        val btn = findViewById<Button>(com.example.facturacion_inventario.R.id.btnLogin)
        val btnReg = findViewById<Button>(com.example.facturacion_inventario.R.id.btnRegister)

        btn.setOnClickListener {
            // Prevent concurrent attempts using atomic flag
            if (!isLoggingIn.compareAndSet(false, true)) return@setOnClickListener
            btn.isEnabled = false

            val u = editUser.text.toString()
            val p = editPass.text.toString()
            val startTs = System.currentTimeMillis()
            Log.d("AuthUI", "login attempt start ts=$startTs thread=${Thread.currentThread().name} user=$u")

            lifecycleScope.launch {
                try {
                    if (DEBUG_FORCE_CLEAR_TOKENS) {
                        TokenStorage.clear(this@LoginActivity)
                        Log.d("AuthUI", "DEBUG: cleared tokens before login")
                    }

                    val resp = repo.login(u, p)
                    val access = resp?.accessTokenNormalized
                    val refresh = resp?.refreshTokenNormalized
                    TokenStorage.setAccessToken(this@LoginActivity, access)
                    TokenStorage.setRefreshToken(this@LoginActivity, refresh)
                    Toast.makeText(this@LoginActivity, "Login ok", Toast.LENGTH_SHORT).show()
                    Log.d("AuthUI", "login success ts=${System.currentTimeMillis()} user=$u")
                } catch (ex: Exception) {
                    Toast.makeText(this@LoginActivity, "Login failed: ${ex.message}", Toast.LENGTH_LONG).show()
                    Log.d("AuthUI", "login failed ts=${System.currentTimeMillis()} user=$u err=${ex.message}")
                } finally {
                    // Re-habilitar el botón e indicador
                    btn.isEnabled = true
                    isLoggingIn.set(false)
                    Log.d("AuthUI", "login attempt finished ts=${System.currentTimeMillis()} user=$u duration=${System.currentTimeMillis()-startTs}")
                }
            }
        }

        btnReg.setOnClickListener {
            // Abrir pantalla de registro
            startActivity(Intent(this, com.example.auth.RegisterActivity::class.java))
        }
    }
}
