package com.example.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.facturacion_inventario.R
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import androidx.appcompat.app.AlertDialog
import android.text.InputType

class LoginFragment : Fragment() {
    private var repo: AuthRepository? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Inicializar repo con contexto
        val ctx = requireContext()
        repo = AuthRepository(ctx, "http://10.0.2.2:8080/")

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
            showTokenDialog(
                title = getString(R.string.btn_login_google),
                hint = getString(R.string.hint_google_token)
            ) { idToken ->
                btnLoginGoogle.isEnabled = false
                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        val resp = repo?.oauthGoogle(idToken)
                        val access = resp?.accessTokenNormalized
                        val refresh = resp?.refreshTokenNormalized
                        if (!access.isNullOrEmpty()) TokenStorage.setAccessToken(ctx, access)
                        if (!refresh.isNullOrEmpty()) TokenStorage.setRefreshToken(ctx, refresh)

                        val options = NavOptions.Builder()
                            .setPopUpTo(R.id.loginFragment, true)
                            .build()
                        findNavController().navigate(R.id.dashboardFragment, null, options)
                    } catch (ex: Exception) {
                        Toast.makeText(ctx, "Google OAuth fallido: ${ex.message}", Toast.LENGTH_LONG).show()
                    } finally {
                        btnLoginGoogle.isEnabled = true
                    }
                }
            }
        }

        btnLoginFacebook?.setOnClickListener {
            showTokenDialog(
                title = getString(R.string.btn_login_facebook),
                hint = getString(R.string.hint_facebook_token)
            ) { fbToken ->
                btnLoginFacebook.isEnabled = false
                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        val resp = repo?.oauthFacebook(fbToken)
                        val access = resp?.accessTokenNormalized
                        val refresh = resp?.refreshTokenNormalized
                        if (!access.isNullOrEmpty()) TokenStorage.setAccessToken(ctx, access)
                        if (!refresh.isNullOrEmpty()) TokenStorage.setRefreshToken(ctx, refresh)

                        val options = NavOptions.Builder()
                            .setPopUpTo(R.id.loginFragment, true)
                            .build()
                        findNavController().navigate(R.id.dashboardFragment, null, options)
                    } catch (ex: Exception) {
                        Toast.makeText(ctx, "Facebook OAuth fallido: ${ex.message}", Toast.LENGTH_LONG).show()
                    } finally {
                        btnLoginFacebook.isEnabled = true
                    }
                }
            }
        }
    }

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
