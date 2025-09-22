package com.example.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.facturacion_inventario.R
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {
    private var repo: AuthRepository? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ctx = requireContext()
        repo = AuthRepository(ctx, "http://10.0.2.2:8080/")

        val btnLogout = view.findViewById<MaterialButton>(R.id.btnLogout)
        btnLogout?.setOnClickListener {
            btnLogout.isEnabled = false
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    val refresh = TokenStorage.getRefreshToken(ctx)
                    if (!refresh.isNullOrEmpty()) {
                        // Llama al endpoint /api/auth/logout que espera { refreshToken }
                        repo?.logout(refresh)
                    }
                } catch (ex: Exception) {
                    // No bloquees el logout local por fallo de red/servidor
                    Toast.makeText(ctx, "No se pudo cerrar sesión en servidor: ${ex.message}", Toast.LENGTH_SHORT).show()
                } finally {
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
