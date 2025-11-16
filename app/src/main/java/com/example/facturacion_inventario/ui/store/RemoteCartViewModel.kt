package com.example.facturacion_inventario.ui.store

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.facturacion_inventario.data.repository.CarritoManager
import com.example.facturacion_inventario.domain.model.CartItem
import com.example.facturacion_inventario.domain.usecase.CarritoUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel completo para gestionar carritos con la API
 *
 * NUEVA FUNCIONALIDAD:
 * - Gestiona carritos dinámicamente (no hardcodeado)
 * - Crea carrito automáticamente si no existe
 * - Guarda el carrito actual en SharedPreferences
 * - Sincroniza con el backend
 */
class RemoteCartViewModel(
    application: Application,
    private val carritoUseCases: CarritoUseCases = CarritoUseCases()
) : AndroidViewModel(application) {

    private val TAG = "RemoteCartViewModel"
    private val carritoManager = CarritoManager(application)

    // Estados de la UI
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _carritoId = MutableStateFlow<String?>(null)
    val carritoId: StateFlow<String?> = _carritoId.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _totalPrice = MutableStateFlow(0.0)
    val totalPrice: StateFlow<Double> = _totalPrice.asStateFlow()

    // 🔥 NUEVO: Contador de items para el badge del carrito
    private val _totalItemCount = MutableStateFlow(0)
    val totalItemCount: StateFlow<Int> = _totalItemCount.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    init {
        // Cargar el carrito guardado si existe
        val savedCarritoId = carritoManager.getCarritoActual()
        if (savedCarritoId != null) {
            _carritoId.value = savedCarritoId
            Log.d(TAG, "📦 Carrito guardado encontrado: $savedCarritoId")
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // MÉTODOS NUEVOS - Gestión Dinámica de Carritos
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Obtiene o crea el carrito del usuario actual
     * @param usuarioId ID del usuario (null para carrito anónimo)
     */
    fun obtenerOCrearCarrito(usuarioId: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            // Verificar si ya hay un carrito guardado
            val savedCarritoId = carritoManager.getCarritoActual()
            if (savedCarritoId != null) {
                Log.d(TAG, "📦 Cargando carrito existente: $savedCarritoId")
                loadCarrito(savedCarritoId)
                return@launch
            }

            // Si el usuario tiene ID, buscar sus carritos
            if (usuarioId != null) {
                Log.d(TAG, "👤 Buscando carritos del usuario: $usuarioId")
                val result = carritoUseCases.obtenerCarritosPorUsuario(usuarioId)

                if (result.isSuccess) {
                    val carritos = result.getOrNull() ?: emptyList()
                    if (carritos.isNotEmpty()) {
                        // Usar el primer carrito encontrado y extraer su ID
                        val primerCarritoDto = carritos.first()
                        val primerCarritoId = primerCarritoDto.id
                        carritoManager.setCarritoActual(primerCarritoId, usuarioId)
                        _carritoId.value = primerCarritoId
                        Log.d(TAG, "✅ Carrito del usuario encontrado: $primerCarritoId")
                        loadCarrito(primerCarritoId)
                        return@launch
                    }
                }
            }

            // No hay carrito, crear uno nuevo
            Log.d(TAG, "➕ Creando nuevo carrito para usuario: $usuarioId")
            crearCarrito(usuarioId)
        }
    }

    /**
     * Carga los items del carrito actual
     */
    fun cargarCarritoActual() {
        val currentCarritoId = _carritoId.value ?: carritoManager.getCarritoActual()
        if (currentCarritoId != null) {
            loadCarrito(currentCarritoId)
        } else {
            Log.w(TAG, "⚠️ No hay carrito activo para cargar")
            obtenerOCrearCarrito()
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // MÉTODOS DE LECTURA
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Carga un carrito desde la API por su ID
     */
    fun loadCarrito(carritoId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            Log.d(TAG, "🛒 Loading carrito with ID: $carritoId")

            val result = carritoUseCases.obtenerCarrito(carritoId)

            if (result.isSuccess) {
                val items = result.getOrNull() ?: emptyList()
                _cartItems.value = items
                _carritoId.value = carritoId
                _totalPrice.value = calculateTotal(items)
                _totalItemCount.value = items.sumOf { it.quantity } // Actualizar contador de items

                Log.d(TAG, "✅ Successfully loaded ${items.size} items from carrito")
            } else {
                val error = result.exceptionOrNull()?.message ?: "Error desconocido"
                _errorMessage.value = error
                Log.e(TAG, "❌ Error loading carrito: $error")

                // Si el carrito no existe, limpiar y crear uno nuevo
                if (error.contains("404") || error.contains("no encontrado")) {
                    carritoManager.clearCarrito()
                    obtenerOCrearCarrito()
                }
            }

            _isLoading.value = false
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // MÉTODOS DE CREACIÓN
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Crea un carrito nuevo
     */
    fun crearCarrito(usuarioId: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            Log.d(TAG, "➕ Creating new carrito for user: $usuarioId")

            val result = carritoUseCases.crearCarrito(usuarioId)

            if (result.isSuccess) {
                val carritoDto = result.getOrNull()
                if (carritoDto != null) {
                    val newCarritoId = carritoDto.id
                    _carritoId.value = newCarritoId
                    _cartItems.value = emptyList()
                    _totalPrice.value = 0.0
                    _totalItemCount.value = 0 // Reiniciar contador de items

                    // Guardar el carrito en el gestor
                    carritoManager.setCarritoActual(newCarritoId, usuarioId ?: "anonimo")

                    _successMessage.value = "Carrito creado exitosamente"
                    Log.d(TAG, "✅ Carrito created with ID: $newCarritoId")
                }
            } else {
                val error = result.exceptionOrNull()?.message ?: "Error desconocido"
                _errorMessage.value = error
                Log.e(TAG, "❌ Error creating carrito: $error")
            }

            _isLoading.value = false
        }
    }

    /**
     * Agrega un producto al carrito actual
     */
    fun agregarProducto(productoId: String, cantidad: Int = 1) {
        val currentCarritoId = _carritoId.value
        if (currentCarritoId == null) {
            _errorMessage.value = "No hay carrito activo. Creando uno nuevo..."
            obtenerOCrearCarrito()
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            Log.d(TAG, "➕ Adding product $productoId (qty: $cantidad) to carrito")

            val result = carritoUseCases.agregarProducto(
                currentCarritoId,
                productoId,
                cantidad
            )

            if (result.isSuccess) {
                val items = result.getOrNull() ?: emptyList()
                _cartItems.value = items
                _totalPrice.value = calculateTotal(items)
                _totalItemCount.value = items.sumOf { it.quantity } // Actualizar contador de items
                _successMessage.value = "Producto agregado al carrito"
                Log.d(TAG, "✅ Product added successfully")
            } else {
                val error = result.exceptionOrNull()?.message ?: "Error desconocido"
                _errorMessage.value = error
                Log.e(TAG, "❌ Error adding product: $error")
            }

            _isLoading.value = false
        }
    }

    /**
     * Sincroniza un carrito anónimo con el usuario
     * Útil después del login
     */
    fun sincronizarCarritoAnonimo(anonCartId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            Log.d(TAG, "🔄 Syncing anonymous cart: $anonCartId")

            val result = carritoUseCases.sincronizarCarritoAnonimo(anonCartId)

            if (result.isSuccess) {
                val userCarritoId = result.getOrNull()
                if (userCarritoId != null) {
                    _successMessage.value = "Carrito sincronizado"
                    Log.d(TAG, "✅ Cart synced to user cart: $userCarritoId")
                    // Cargar el carrito del usuario
                    loadCarrito(userCarritoId)
                }
            } else {
                val error = result.exceptionOrNull()?.message ?: "Error desconocido"
                _errorMessage.value = error
                Log.e(TAG, "❌ Error syncing cart: $error")
            }

            _isLoading.value = false
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // MÉTODOS DE ELIMINACIÓN
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Remueve un producto del carrito
     * @param productoId ID del producto a remover
     */
    fun removerProducto(productoId: String) {
        val currentCarritoId = _carritoId.value
        if (currentCarritoId == null) {
            _errorMessage.value = "No hay carrito activo"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            Log.d(TAG, "➖ Removing product $productoId from carrito")

            val result = carritoUseCases.eliminarProducto(currentCarritoId, productoId)

            if (result.isSuccess) {
                val items = result.getOrNull() ?: emptyList()
                _cartItems.value = items
                _totalPrice.value = calculateTotal(items)
                _totalItemCount.value = items.sumOf { it.quantity } // Actualizar contador de items
                _successMessage.value = "Producto removido del carrito"
                Log.d(TAG, "✅ Product removed successfully")
            } else {
                val error = result.exceptionOrNull()?.message ?: "Error desconocido"
                _errorMessage.value = error
                Log.e(TAG, "❌ Error removing product: $error")
            }

            _isLoading.value = false
        }
    }

    /**
     * Vacía el carrito (elimina todos los items)
     */
    fun vaciarCarrito() {
        val currentCarritoId = _carritoId.value
        if (currentCarritoId == null) {
            _errorMessage.value = "No hay carrito activo"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            Log.d(TAG, "🗑️ Clearing carrito")

            val result = carritoUseCases.vaciarCarrito(currentCarritoId)

            if (result.isSuccess) {
                _cartItems.value = emptyList()
                _totalPrice.value = 0.0
                _totalItemCount.value = 0 // Reiniciar contador de items
                _successMessage.value = "Carrito vaciado"
                Log.d(TAG, "✅ Carrito cleared successfully")
            } else {
                val error = result.exceptionOrNull()?.message ?: "Error desconocido"
                _errorMessage.value = error
                Log.e(TAG, "❌ Error clearing carrito: $error")
            }

            _isLoading.value = false
        }
    }

    /**
     * Elimina el carrito completamente
     */
    fun eliminarCarrito() {
        val currentCarritoId = _carritoId.value
        if (currentCarritoId == null) {
            _errorMessage.value = "No hay carrito activo"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            Log.d(TAG, "🗑️ Deleting carrito")

            val result = carritoUseCases.eliminarCarrito(currentCarritoId)

            if (result.isSuccess) {
                _cartItems.value = emptyList()
                _totalPrice.value = 0.0
                _carritoId.value = null
                _successMessage.value = "Carrito eliminado"
                Log.d(TAG, "✅ Carrito deleted successfully")
            } else {
                val error = result.exceptionOrNull()?.message ?: "Error desconocido"
                _errorMessage.value = error
                Log.e(TAG, "❌ Error deleting carrito: $error")
            }

            _isLoading.value = false
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // MÉTODOS AUXILIARES
    // ═══════════════════════════════════════════════════════════════════

    private fun calculateTotal(items: List<CartItem>): Double {
        return items.sumOf { it.product.price * it.quantity }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun clearSuccessMessage() {
        _successMessage.value = null
    }

    /**
     * 🔥 NUEVO: Limpia el carrito local inmediatamente y crea uno nuevo
     * Útil después de generar una factura para que la UI se actualice instantáneamente
     */
    fun limpiarYCrearNuevoCarrito(usuarioId: String? = null) {
        viewModelScope.launch {
            Log.d(TAG, "🧹 Limpiando carrito actual y creando nuevo...")

            // 1. Limpiar estado local INMEDIATAMENTE (esto actualiza la UI al instante)
            _cartItems.value = emptyList()
            _totalPrice.value = 0.0
            _totalItemCount.value = 0
            _carritoId.value = null

            // 2. Limpiar SharedPreferences
            carritoManager.clearCarrito()

            // 3. Crear un nuevo carrito en el backend
            obtenerOCrearCarrito(usuarioId)

            Log.d(TAG, "✅ Carrito limpiado y nuevo carrito en proceso de creación")
        }
    }
}
