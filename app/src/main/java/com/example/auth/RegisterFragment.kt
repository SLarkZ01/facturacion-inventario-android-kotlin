package com.example.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.facturacion_inventario.R
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {
    private var repo: AuthRepository? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ctx = requireContext()
        repo = AuthRepository(ctx, "http://10.0.2.2:8080/")

        val btnBack = view.findViewById<ImageView>(R.id.btnBack)
        val btnRegister = view.findViewById<MaterialButton>(R.id.btnRegister)
        val etUsername = view.findViewById<EditText>(R.id.etUsername)
        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val etPassword = view.findViewById<EditText>(R.id.etPassword)
        val etNombre = view.findViewById<EditText>(R.id.etNombre)
        val etApellido = view.findViewById<EditText>(R.id.etApellido)
        val etRol = view.findViewById<EditText>(R.id.etRol)
        val etInviteCode = view.findViewById<EditText>(R.id.etInviteCode)

        btnBack?.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }

        btnRegister?.setOnClickListener {
            val username = etUsername?.text?.toString()?.trim().orEmpty()
            val email = etEmail?.text?.toString()?.trim().orEmpty()
            val password = etPassword?.text?.toString().orEmpty()
            val nombre = etNombre?.text?.toString()?.trim()?.ifEmpty { null }
            val apellido = etApellido?.text?.toString()?.trim()?.ifEmpty { null }
            val rol = etRol?.text?.toString()?.trim()?.ifEmpty { "ADMIN" }
            val inviteCode = etInviteCode?.text?.toString()?.trim()?.ifEmpty { null }

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(ctx, "Rellena usuario, email y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnRegister.isEnabled = false
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    val req = RegisterRequest(
                        username = username,
                        email = email,
                        password = password,
                        inviteCode = inviteCode,
                        nombre = nombre,
                        apellido = apellido,
                        rol = rol
                    )
                    val resp = repo?.register(req)
                    val access = resp?.accessTokenNormalized
                    val refresh = resp?.refreshTokenNormalized
                    if (!access.isNullOrEmpty()) TokenStorage.setAccessToken(ctx, access)
                    if (!refresh.isNullOrEmpty()) TokenStorage.setRefreshToken(ctx, refresh)

                    val options = NavOptions.Builder()
                        .setPopUpTo(R.id.registerFragment, true)
                        .build()
                    findNavController().navigate(R.id.dashboardFragment, null, options)
                } catch (ex: Exception) {
                    // OpenAPI: 409 para ya registrado; puede llegar como error HTTP -> mostrar mensaje claro
                    Toast.makeText(ctx, "Registro fallido: ${ex.message}", Toast.LENGTH_LONG).show()
                } finally {
                    btnRegister.isEnabled = true
                }
            }
        }
    }
}
