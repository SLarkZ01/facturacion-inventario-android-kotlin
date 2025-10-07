package com.example.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.facturacion_inventario.R
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import androidx.appcompat.app.AlertDialog
import android.text.InputType
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import android.util.Log
import com.example.data.auth.AuthRepository
import com.example.data.auth.TokenStorage
import com.example.data.auth.ApiConfig

class LoginFragment : Fragment() {
    private var repo: AuthRepository? = null

    // Google
    private var googleAuth: GoogleAuthManager? = null
    private val googleLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // Manejar resultado interactivo de Google Sign-In
        googleAuth?.handleSignInResult(result.data,
            onSuccess = { idToken ->
                oauthGoogle(idToken)
            },
            onFailure = { e ->
                Log.e("GoogleAuth", "handleSignInResult error: ${e.message}", e)
                Toast.makeText(requireContext(), "Google Sign-In falló: ${e.message}", Toast.LENGTH_LONG).show()
                view?.findViewById<MaterialButton>(R.id.btnLoginGoogle)?.isEnabled = true
            }
        )
    }

    // Facebook
    private lateinit var fbCallbackManager: CallbackManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ctx = requireContext()
        repo = AuthRepository(ctx, ApiConfig.BASE_URL)

        // Inicializar GoogleAuthManager con el Server Client ID desde recursos
        val serverClientId = getString(R.string.google_server_client_id)
        // Log / Toast diagnóstico para confirmar que la APK desplegada contiene el Client ID correcto
        Log.i("GoogleAuth", "google_server_client_id (from resources): $serverClientId")
        Toast.makeText(ctx, "google_client: ${serverClientId.take(40)}...", Toast.LENGTH_SHORT).show()
        googleAuth = GoogleAuthManager(ctx, serverClientId)

        // Inicializar Facebook CallbackManager
        fbCallbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(fbCallbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                val token = result.accessToken?.token
                if (!token.isNullOrEmpty()) {
                    oauthFacebook(token)
                } else {
                    Toast.makeText(ctx, "No se obtuvo token de Facebook", Toast.LENGTH_SHORT).show()
                    view.findViewById<MaterialButton>(R.id.btnLoginFacebook)?.isEnabled = true
                }
            }

            override fun onCancel() {
                Toast.makeText(ctx, "Inicio de sesión de Facebook cancelado", Toast.LENGTH_SHORT).show()
                view.findViewById<MaterialButton>(R.id.btnLoginFacebook)?.isEnabled = true
            }

            override fun onError(error: FacebookException) {
                Toast.makeText(ctx, "Facebook Login error: ${error.message}", Toast.LENGTH_LONG).show()
                view.findViewById<MaterialButton>(R.id.btnLoginFacebook)?.isEnabled = true
            }
        })

        val btnRegister = view.findViewById<MaterialButton>(R.id.btnRegister)
        val btnLogin = view.findViewById<MaterialButton>(R.id.btnLogin)
        val etUser = view.findViewById<EditText>(R.id.etUsername)
        val etPass = view.findViewById<EditText>(R.id.etPassword)
        val btnLoginGoogle = view.findViewById<MaterialButton>(R.id.btnLoginGoogle)
        val btnLoginFacebook = view.findViewById<MaterialButton>(R.id.btnLoginFacebook)

        btnRegister?.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }

        btnLogin?.setOnClickListener {
            val u = etUser?.text?.toString()?.trim().orEmpty()
            val p = etPass?.text?.toString().orEmpty()
            if (u.isEmpty() || p.isEmpty()) {
                Toast.makeText(ctx, "Ingresa usuario y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            btnLogin.isEnabled = false
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    val resp = repo?.login(u, p)
                    val access = resp?.accessTokenNormalized
                    val refresh = resp?.refreshTokenNormalized
                    if (!access.isNullOrEmpty()) TokenStorage.setAccessToken(ctx, access)
                    if (!refresh.isNullOrEmpty()) TokenStorage.setRefreshToken(ctx, refresh)


                     // Guardar info del usuario si viene en la respuesta
                    TokenStorage.setUserFromMap(ctx, resp?.user)

                    val options = NavOptions.Builder()
                        .setPopUpTo(R.id.loginFragment, true)
                        .build()
                    findNavController().navigate(R.id.dashboardFragment, null, options)
                } catch (ex: Exception) {
                    Toast.makeText(ctx, "Login fallido: ${ex.message}", Toast.LENGTH_LONG).show()
                } finally {
                    btnLogin.isEnabled = true
                }
            }
        }

        btnLoginGoogle?.setOnClickListener {
            // Intentar primero silent sign-in para no mostrar diálogo si ya hay sesión de Google en el dispositivo
            btnLoginGoogle.isEnabled = false
            googleAuth?.trySilentSignIn(
                onSuccess = { idToken ->
                    oauthGoogle(idToken)
                    btnLoginGoogle.isEnabled = true
                },
                onFailure = { e ->
                    // Log del fallo silencioso para diagnostico
                    Log.w("GoogleAuth", "Silent sign-in failed: ${e.message}", e)
                    // Abrir selector de cuenta (flujo interactivo)
                    val intent = googleAuth?.getSignInIntent()
                    if (intent != null) {
                        googleLauncher.launch(intent)
                    } else {
                        Toast.makeText(ctx, "No se pudo iniciar Google Sign-In", Toast.LENGTH_SHORT).show()
                        btnLoginGoogle.isEnabled = true
                    }
                }
            )
        }

        btnLoginFacebook?.setOnClickListener {
            // Disparar flujo de Facebook (abre app o Custom Tab y luego retorna al fragment)
            btnLoginFacebook.isEnabled = false
            try {
                LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile", "email"))
            } catch (e: Exception) {
                Toast.makeText(ctx, "No se pudo iniciar Facebook Login: ${e.message}", Toast.LENGTH_LONG).show()
                btnLoginFacebook.isEnabled = true
            }
        }
    }

    private fun oauthGoogle(idToken: String) {
        val ctx = requireContext()
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val resp = repo?.oauthGoogle(idToken)
                val access = resp?.accessTokenNormalized
                val refresh = resp?.refreshTokenNormalized
                if (!access.isNullOrEmpty()) TokenStorage.setAccessToken(ctx, access)
                if (!refresh.isNullOrEmpty()) TokenStorage.setRefreshToken(ctx, refresh)

                // Guardar info del usuario
                TokenStorage.setUserFromMap(ctx, resp?.user)

                val options = NavOptions.Builder()
                    .setPopUpTo(R.id.loginFragment, true)
                    .build()
                findNavController().navigate(R.id.dashboardFragment, null, options)
            } catch (ex: Exception) {
                Toast.makeText(ctx, "Google OAuth fallido: ${ex.message}", Toast.LENGTH_LONG).show()
            } finally {
                view?.findViewById<MaterialButton>(R.id.btnLoginGoogle)?.isEnabled = true
            }
        }
    }

    private fun oauthFacebook(fbAccessToken: String) {
        val ctx = requireContext()
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val resp = repo?.oauthFacebook(fbAccessToken)
                val access = resp?.accessTokenNormalized
                val refresh = resp?.refreshTokenNormalized
                if (!access.isNullOrEmpty()) TokenStorage.setAccessToken(ctx, access)
                if (!refresh.isNullOrEmpty()) TokenStorage.setRefreshToken(ctx, refresh)

                // Guardar info del usuario
                TokenStorage.setUserFromMap(ctx, resp?.user)

                val options = NavOptions.Builder()
                    .setPopUpTo(R.id.loginFragment, true)
                    .build()
                findNavController().navigate(R.id.dashboardFragment, null, options)
            } catch (ex: Exception) {
                Toast.makeText(ctx, "Facebook OAuth fallido: ${ex.message}", Toast.LENGTH_LONG).show()
            } finally {
                view?.findViewById<MaterialButton>(R.id.btnLoginFacebook)?.isEnabled = true
            }
        }
    }

    // Reenvía el resultado a Facebook CallbackManager
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (this::fbCallbackManager.isInitialized) {
            fbCallbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    // Utilidad previa (usada para pruebas) – la conservamos por si se necesita manualmente
    private fun showTokenDialog(title: String, hint: String, onSubmit: (String) -> Unit) {
        val ctx = requireContext()
        val input = EditText(ctx)
        input.hint = hint
        input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        AlertDialog.Builder(ctx)
            .setTitle(title)
            .setView(input)
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                val token = input.text?.toString()?.trim().orEmpty()
                if (token.isEmpty()) {
                    Toast.makeText(ctx, "El token no puede estar vacío", Toast.LENGTH_SHORT).show()
                } else {
                    onSubmit(token)
                    dialog.dismiss()
                }
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }
}