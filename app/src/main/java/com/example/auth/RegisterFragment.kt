package com.example.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.facturacion_inventario.BuildConfig
import com.example.facturacion_inventario.R
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {
    private var repo: AuthRepository? = null

    // TODO: Reemplaza por tu Client ID de tipo "Web application" (server) que está en Google Cloud.
    // Este Client ID es el que el backend usa para verificar tokens.
    private val googleServerClientId: String by lazy { BuildConfig.GOOGLE_SERVER_CLIENT_ID }

    private lateinit var googleAuthManager: GoogleAuthManager

    // ActivityResultLauncher para manejar el resultado del flujo interactivo (si lo añades a la UI)
    private val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            googleAuthManager.handleSignInResult(data, onSuccess = { idToken ->
                // Si obtenemos idToken aquí, enviarlo al backend
                handleGoogleIdToken(idToken)
            }, onFailure = { ex ->
                Toast.makeText(requireContext(), "Google Sign-in falló: ${ex.message}", Toast.LENGTH_LONG).show()
            })
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ctx = requireContext()
        repo = AuthRepository(ctx, "http://10.0.2.2:8080/")

        // Inicializar el GoogleAuthManager con el client id placeholder
        googleAuthManager = GoogleAuthManager(ctx, googleServerClientId)

        val btnBack = view.findViewById<ImageView>(R.id.btnBack)
        val btnRegister = view.findViewById<MaterialButton>(R.id.btnRegister)
        val etUsername = view.findViewById<EditText>(R.id.etUsername)
        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val etPassword = view.findViewById<EditText>(R.id.etPassword)
        val etNombre = view.findViewById<EditText>(R.id.etNombre)
        val etApellido = view.findViewById<EditText>(R.id.etApellido)
        val etInviteCode = view.findViewById<EditText>(R.id.etInviteCode)

        btnBack?.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }

        // Intentar sign-in silencioso con Google; si funciona, enviar idToken al backend y navegar al dashboard
        try {
            googleAuthManager.trySilentSignIn(onSuccess = { idToken ->
                // Llamada suspend dentro de coroutine
                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        val inviteCode = etInviteCode?.text?.toString()?.trim()?.ifEmpty { null }
                        val resp = repo?.oauthGoogle(idToken, inviteCode)
                        val access = resp?.accessTokenNormalized
                        val refresh = resp?.refreshTokenNormalized
                        if (!access.isNullOrEmpty()) TokenStorage.setAccessToken(ctx, access)
                        if (!refresh.isNullOrEmpty()) TokenStorage.setRefreshToken(ctx, refresh)

                        // Guardar info del usuario si viene en la respuesta
                        TokenStorage.setUserFromMap(ctx, resp?.user)
                        // Fallback si no vino user
                        if (TokenStorage.getUsername(ctx).isNullOrEmpty()) {
                            // No conocemos username desde el provider, dejamos vacío o usamos email si está disponible
                        }

                        val options = NavOptions.Builder()
                            .setPopUpTo(R.id.registerFragment, true)
                            .build()
                        findNavController().navigate(R.id.dashboardFragment, null, options)
                    } catch (ex: Exception) {
                        Toast.makeText(ctx, "Autenticación Google falló: ${ex.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }, onFailure = { /* Silent sign-in not available; user will have to sign in traditionally or with interactive flow */ })
        } catch (e: Exception) {
            // Guardar el fallo en log o mostrar pequeño mensaje no intrusivo
        }

        btnRegister?.setOnClickListener {
            val username = etUsername?.text?.toString()?.trim().orEmpty()
            val email = etEmail?.text?.toString()?.trim().orEmpty()
            val password = etPassword?.text?.toString().orEmpty()
            val nombre = etNombre?.text?.toString()?.trim().orEmpty()
            val apellido = etApellido?.text?.toString()?.trim().orEmpty()
            val inviteCode = etInviteCode?.text?.toString()?.trim()?.ifEmpty { null }

            // Validaciones obligatorias: username, email, password, nombre y apellido
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
                Toast.makeText(ctx, "Rellena usuario, email, contraseña, nombre y apellido", Toast.LENGTH_SHORT).show()
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
                        apellido = apellido
                    )
                    val resp = repo?.register(req)
                    val access = resp?.accessTokenNormalized
                    val refresh = resp?.refreshTokenNormalized
                    if (!access.isNullOrEmpty()) TokenStorage.setAccessToken(ctx, access)
                    if (!refresh.isNullOrEmpty()) TokenStorage.setRefreshToken(ctx, refresh)

                    // Guardar info del usuario: preferimos lo que venga en la respuesta
                    TokenStorage.setUserFromMap(ctx, resp?.user)
                    // Si backend no devolvió user, guardamos los campos locales
                    if (TokenStorage.getUsername(ctx).isNullOrEmpty()) {
                        TokenStorage.setUserInfo(ctx, username, nombre, apellido)
                    }

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

    private fun handleGoogleIdToken(idToken: String) {
        // Método auxiliar que llama a repo.oauthGoogle en background
        val ctx = requireContext()
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val inviteCodeField = view?.findViewById<EditText>(R.id.etInviteCode)
                val inviteCode = inviteCodeField?.text?.toString()?.trim()?.ifEmpty { null }
                val resp = repo?.oauthGoogle(idToken, inviteCode)
                val access = resp?.accessTokenNormalized
                val refresh = resp?.refreshTokenNormalized
                if (!access.isNullOrEmpty()) TokenStorage.setAccessToken(ctx, access)
                if (!refresh.isNullOrEmpty()) TokenStorage.setRefreshToken(ctx, refresh)

                // Guardar info del usuario
                TokenStorage.setUserFromMap(ctx, resp?.user)
                // Fallback local (si es necesario) no aplicable aquí porque normalmente provider da info

                val options = NavOptions.Builder()
                    .setPopUpTo(R.id.registerFragment, true)
                    .build()
                findNavController().navigate(R.id.dashboardFragment, null, options)
            } catch (ex: Exception) {
                Toast.makeText(ctx, "Autenticación Google falló: ${ex.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}