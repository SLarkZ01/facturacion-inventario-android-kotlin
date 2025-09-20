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
        val etRol = findViewById<EditText>(com.example.facturacion_inventario.R.id.etRol)
        val btnRegister = findViewById<Button>(com.example.facturacion_inventario.R.id.btnRegister)

        btnRegister.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()
            val nombre = etNombre.text.toString().trim().ifEmpty { null }
            val apellido = etApellido.text.toString().trim().ifEmpty { null }
            val rol = etRol.text.toString().trim().ifEmpty { "VENDEDOR" }

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Rellena usuario, email y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val req = RegisterRequest(
                username = username,
                email = email,
                password = password,
                nombre = nombre,
                apellido = apellido,
                rol = rol
            )

            lifecycleScope.launch {
                try {
                    repo.register(req)
                    Toast.makeText(this@RegisterActivity, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    finish()
                } catch (ex: Exception) {
                    Toast.makeText(this@RegisterActivity, "Registro fallido: ${ex.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

