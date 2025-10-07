package com.example.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Importar definiciones desde el módulo de datos
import com.example.data.auth.AuthDataSource
import com.example.data.auth.TokenStorage
import com.example.data.auth.RegisterRequest

// Estado simple de UI para auth
data class AuthUiState(
    val loading: Boolean = false,
    val errorMessage: String? = null,
    val success: Boolean = false
)

class AuthViewModel(application: Application, private val repo: AuthDataSource) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    // Estado sencillo que indica si hay token de acceso
    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    // Nuevo flag para indicar que el usuario decidió omitir el inicio de sesión
    private val _skippedLogin = MutableStateFlow(false)
    val skippedLogin: StateFlow<Boolean> = _skippedLogin.asStateFlow()

    init {
        // Inicializar estado autenticado desde almacenamiento local
        val token = TokenStorage.getAccessToken(getApplication())
        _isAuthenticated.value = !token.isNullOrEmpty()
        // Si ya hay token, aseguramos que skipped sea false
        _skippedLogin.value = false
    }

    fun login(usernameOrEmail: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(loading = true)
            try {
                val resp = repo.login(usernameOrEmail, password)
                val access = resp?.accessTokenNormalized
                val refresh = resp?.refreshTokenNormalized
                if (!access.isNullOrEmpty()) TokenStorage.setAccessToken(getApplication(), access)
                if (!refresh.isNullOrEmpty()) TokenStorage.setRefreshToken(getApplication(), refresh)
                // tratar user info
                TokenStorage.setUserFromMap(getApplication(), resp?.user)

                _isAuthenticated.value = true
                // login real, limpiar el flag de skip
                _skippedLogin.value = false
                _uiState.value = AuthUiState(success = true)
            } catch (ex: Exception) {
                _uiState.value = AuthUiState(errorMessage = ex.message ?: "Error desconocido")
            }
        }
    }

    fun register(req: RegisterRequest) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(loading = true)
            try {
                val resp = repo.register(req)
                val access = resp?.accessTokenNormalized
                val refresh = resp?.refreshTokenNormalized
                if (!access.isNullOrEmpty()) TokenStorage.setAccessToken(getApplication(), access)
                if (!refresh.isNullOrEmpty()) TokenStorage.setRefreshToken(getApplication(), refresh)
                TokenStorage.setUserFromMap(getApplication(), resp?.user)

                _isAuthenticated.value = true
                // registro real, limpiar skip
                _skippedLogin.value = false
                _uiState.value = AuthUiState(success = true)
            } catch (ex: Exception) {
                _uiState.value = AuthUiState(errorMessage = ex.message ?: "Error desconocido")
            }
        }
    }

    // Permite omitir el inicio de sesión y navegar como usuario anónimo
    fun skipLogin() {
        _skippedLogin.value = true
        _isAuthenticated.value = false
        // no guardamos tokens; es solo un modo anónimo
    }

    // Limpia errores/flags después de manejarlos desde la UI
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun reset() {
        _uiState.value = AuthUiState()
    }

    fun logout() {
        TokenStorage.clear(getApplication())
        _isAuthenticated.value = false
        _skippedLogin.value = false
        _uiState.value = AuthUiState()
    }

    // Helpers que UI puede usar para mostrar nombre/username
    fun getNombre(): String? = TokenStorage.getNombre(getApplication())
    fun getApellido(): String? = TokenStorage.getApellido(getApplication())
    fun getUsername(): String? = TokenStorage.getUsername(getApplication())
}
