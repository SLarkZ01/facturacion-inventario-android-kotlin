package com.example.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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

    init {
        // Inicializar estado autenticado desde almacenamiento local
        val token = TokenStorage.getAccessToken(getApplication())
        _isAuthenticated.value = !token.isNullOrEmpty()
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
                _uiState.value = AuthUiState(success = true)
            } catch (ex: Exception) {
                _uiState.value = AuthUiState(errorMessage = ex.message ?: "Error desconocido")
            }
        }
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
        _uiState.value = AuthUiState()
    }

    // Helpers que UI puede usar para mostrar nombre/username
    fun getNombre(): String? = TokenStorage.getNombre(getApplication())
    fun getApellido(): String? = TokenStorage.getApellido(getApplication())
    fun getUsername(): String? = TokenStorage.getUsername(getApplication())
}
