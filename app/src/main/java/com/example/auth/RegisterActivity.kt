package com.example.auth

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var repo: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.facturacion_inventario.R.layout.activity_register)

        repo = AuthRepository(this, "http://10.0.2.2:8080/")

        val etUsername = findViewById<EditText>(com.example.facturacion_inventario.R.id.etUsername)
        val etEmail = findViewById<EditText>(com.example.facturacion_inventario.R.id.etEmail)
        val etPassword = findViewById<EditText>(com.example.facturacion_inventario.R.id.etPassword)
        val etNombre = findViewById<EditText>(com.example.facturacion_inventario.R.id.etNombre)
        val etApellido = findViewById<EditText>(com.example.facturacion_inventario.R.id.etApellido)
        val btnRegister = findViewById<Button>(com.example.facturacion_inventario.R.id.btnRegister)

        btnRegister.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()
            val nombre = etNombre.text.toString().trim()
            val apellido = etApellido.text.toString().trim()

            // Validaciones obligatorias: username, email, password, nombre y apellido
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
                Toast.makeText(this, "Rellena usuario, email, contraseña, nombre y apellido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val req = RegisterRequest(
                username = username,
                email = email,
                password = password,
                inviteCode = null,
                nombre = nombre,
                apellido = apellido
            )

            lifecycleScope.launch {
                try {
                    val resp = repo.register(req)
                    val access = resp?.accessTokenNormalized
                    val refresh = resp?.refreshTokenNormalized
                    if (!access.isNullOrEmpty()) TokenStorage.setAccessToken(this@RegisterActivity, access)
                    if (!refresh.isNullOrEmpty()) TokenStorage.setRefreshToken(this@RegisterActivity, refresh)

                    // Guardar info del usuario localmente (fallback)
                    TokenStorage.setUserInfo(this@RegisterActivity, username, nombre, apellido)

                    Toast.makeText(this@RegisterActivity, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    // Volver o continuar al dashboard según tu flujo. Aquí sólo cerramos.
                    finish()
                } catch (ex: Exception) {
                    Toast.makeText(this@RegisterActivity, "Registro fallido: ${ex.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}