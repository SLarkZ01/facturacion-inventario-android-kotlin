package com.example.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.facturacion_inventario.R
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class DashboardFragment : Fragment() {
    private var repo: AuthRepository? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ctx = requireContext()
        repo = AuthRepository(ctx, "http://10.0.2.2:8080/")

        // Poblamos la UI con la información del usuario almacenada localmente
        val tvFullName = view.findViewById<TextView>(R.id.tv_full_name)
        val tvUsername = view.findViewById<TextView>(R.id.tv_user_name)
        val avatar = view.findViewById<TextView>(R.id.avatar)
        val tvDashboardEmoji = view.findViewById<TextView>(R.id.tv_dashboard_emoji)
        val tvDashboardTitle = view.findViewById<TextView>(R.id.tv_dashboard_title)
        val tvDashboardBody = view.findViewById<TextView>(R.id.tv_dashboard_body)

        val nombre = TokenStorage.getNombre(ctx)?.takeIf { it.isNotBlank() }
        val apellido = TokenStorage.getApellido(ctx)?.takeIf { it.isNotBlank() }
        val username = TokenStorage.getUsername(ctx)?.takeIf { it.isNotBlank() }

        if (!nombre.isNullOrEmpty() || !apellido.isNullOrEmpty()) {
            val fullName = listOfNotNull(nombre, apellido).joinToString(" ")
            tvFullName.text = fullName
            avatar.text = fullName.trim().firstOrNull()?.toString()?.uppercase() ?: getString(R.string.avatar_default)
        } else if (!username.isNullOrEmpty()) {
            tvFullName.text = username
            avatar.text = username.trim().firstOrNull()?.toString()?.uppercase() ?: getString(R.string.avatar_default)
        } else {
            tvFullName.text = getString(R.string.bienvenido)
            avatar.text = getString(R.string.avatar_default)
        }

        tvUsername.text = username ?: getString(R.string.usuario)

        // Mensaje personalizado (llenar views de la card)
        tvDashboardEmoji.text = "🎉"
        tvDashboardTitle.text = getString(R.string.dashboard_title)
        tvDashboardBody.text = getString(R.string.dashboard_body)

        // Intentar obtener perfil desde el servidor (endpoint GET /api/auth/me)
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val me = repo?.me()
                if (me != null) {
                    val usernameApi = (me["username"] ?: me["user"] ?: me["email"])?.toString()
                    val nombreApi = (me["nombre"] ?: me["name"] ?: me["firstName"])?.toString()
                    val apellidoApi = (me["apellido"] ?: me["lastName"] ?: me["surname"])?.toString()

                    // Guardar en almacenamiento local para futuras sesiones
                    TokenStorage.setUserInfo(ctx, usernameApi, nombreApi, apellidoApi)

                    // Actualizar UI (preferir nombre + apellido)
                    val fullNameApi = listOfNotNull(nombreApi?.takeIf { it.isNotBlank() }, apellidoApi?.takeIf { it.isNotBlank() }).joinToString(" ")
                    if (fullNameApi.isNotBlank()) {
                        tvFullName.text = fullNameApi
                        avatar.text = fullNameApi.trim().firstOrNull()?.toString()?.uppercase() ?: getString(R.string.avatar_default)
                    } else if (!usernameApi.isNullOrBlank()) {
                        tvFullName.text = usernameApi
                        avatar.text = usernameApi.trim().firstOrNull()?.toString()?.uppercase() ?: getString(R.string.avatar_default)
                    }

                    if (!usernameApi.isNullOrBlank()) tvUsername.text = usernameApi
                }
            } catch (ex: Exception) {
                // No bloqueamos la UI si falla la consulta al servidor
                Toast.makeText(ctx, "No se pudo obtener perfil del servidor", Toast.LENGTH_SHORT).show()
            }
        }

        val btnLogout = view.findViewById<MaterialButton>(R.id.btnLogout)
        btnLogout?.setOnClickListener {
            btnLogout.isEnabled = false
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    val refresh = TokenStorage.getRefreshToken(ctx)
                    if (!refresh.isNullOrEmpty()) {
                        // Prefiere /api/auth/logout cuando hay refreshToken del cliente
                        repo?.logout(refresh)
                    } else {
                        // Si no hay refreshToken local, recurre a /api/auth/revoke-all con Authorization
                        repo?.revokeAll()
                    }
                } catch (ex: Exception) {
                    // No bloquees el logout local por fallo de red/servidor
                    Toast.makeText(ctx, "No se pudo cerrar sesión en servidor: ${ex.message}", Toast.LENGTH_SHORT).show()
                } finally {
                    // Cerrar sesión en SDKs de terceros
                    try {
                        val serverClientId = getString(R.string.google_server_client_id)
                        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(serverClientId)
                            .requestEmail()
                            .build()
                        GoogleSignIn.getClient(ctx, gso).signOut()
                    } catch (_: Exception) { }
                    try { LoginManager.getInstance().logOut() } catch (_: Exception) { }

                    // Limpieza local y navegación a login siempre
                    TokenStorage.clear(ctx)
                    val options = NavOptions.Builder()
                        .setPopUpTo(R.id.dashboardFragment, true)
                        .build()
                    findNavController().navigate(R.id.loginFragment, null, options)
                    btnLogout.isEnabled = true
                }
            }
        }
    }
}