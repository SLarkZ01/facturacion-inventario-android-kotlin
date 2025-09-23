package com.example.auth

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

/**
 * GoogleAuthManager
 * - Intenta sign-in silencioso (sin diálogo) y devuelve el ID token si está disponible.
 * - Provee el Intent para iniciar el flujo interactivo cuando se requiere.
 *
 * Uso:
 * val manager = GoogleAuthManager(context, YOUR_SERVER_CLIENT_ID)
 * manager.trySilentSignIn(onSuccess = { idToken -> /* enviar al backend */ }, onFailure = { /* fallback */ })
 * // para iniciar sign-in interactivo:
 * startActivityForResult(manager.getSignInIntent(), REQUEST_CODE)
 * // luego en onActivityResult -> manager.handleSignInResult(data, ...)
 *
 * Importante: reemplaza YOUR_SERVER_CLIENT_ID por el Client ID del tipo "Web application" (server) que está registrado en Google Cloud
 * y que tu backend usa para verificar el token. No incluyas el client ID de Android (SHA) aquí.
 */
class GoogleAuthManager(private val context: Context, private val serverClientId: String) {

    companion object {
        const val REQUEST_CODE_SIGN_IN = 9001
    }

    private fun buildGso(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(serverClientId)
            .requestEmail()
            .build()
    }

    /**
     * Intenta sign-in silencioso. Si éxito, llama a onSuccess con el idToken (String).
     * Si no puede, llama a onFailure con la excepción.
     */
    fun trySilentSignIn(onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val gso = buildGso()
        val client = GoogleSignIn.getClient(context, gso)
        val task = client.silentSignIn()
        task.addOnCompleteListener { t ->
            if (t.isSuccessful) {
                val account: GoogleSignInAccount? = t.result
                val idToken = account?.idToken
                if (!idToken.isNullOrEmpty()) {
                    onSuccess(idToken)
                } else {
                    onFailure(Exception("No ID token returned from silent sign-in"))
                }
            } else {
                // Silent sign-in not possible (no previously signed-in account)
                val ex = t.exception
                if (ex is ApiException) {
                    onFailure(Exception("Silent sign-in failed: statusCode=${ex.statusCode}, message=${ex.message}"))
                } else if (ex != null) {
                    onFailure(Exception("Silent sign-in failed: ${ex.message}"))
                } else {
                    onFailure(Exception("Silent sign-in failed"))
                }
            }
        }
    }

    /**
     * Devuelve el Intent para iniciar el flujo interactivo de sign-in.
     */
    fun getSignInIntent(): Intent {
        val gso = buildGso()
        val client = GoogleSignIn.getClient(context, gso)
        return client.signInIntent
    }

    /**
     * Procesa el resultado del Intent de sign-in interactivo. Llama a onSuccess con el idToken si lo obtiene.
     * Debes llamar a este método desde onActivityResult / ActivityResult callback.
     */
    fun handleSignInResult(data: Intent?, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            val idToken = account?.idToken
            if (!idToken.isNullOrEmpty()) {
                onSuccess(idToken)
            } else {
                onFailure(Exception("No ID token returned from interactive sign-in"))
            }
        } catch (e: Exception) {
            // Si es ApiException, añadimos el statusCode para facilitar diagnóstico
            if (e is ApiException) {
                onFailure(Exception("Google Sign-In failed: statusCode=${e.statusCode}, message=${e.message}"))
            } else {
                onFailure(e)
            }
        }
    }

    /**
     * Cierra sesión del cliente de Google.
     * La próxima vez que se intente sign-in, se mostrará el selector de cuentas si hay múltiples cuentas.
     */
    fun signOut() {
        val gso = buildGso()
        val client = GoogleSignIn.getClient(context, gso)
        client.signOut()
    }
}
