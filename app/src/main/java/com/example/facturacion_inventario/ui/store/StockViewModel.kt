package com.example.facturacion_inventario.ui.store

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facturacion_inventario.data.remote.model.StockByAlmacenDto
import com.example.facturacion_inventario.data.repository.RemoteStockRepository
import com.example.facturacion_inventario.data.repository.StockException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estados posibles para el stock de un producto
 */
sealed class StockState {
    object Loading : StockState()
    data class Success(
        val total: Int,
        val stockByAlmacen: List<StockByAlmacenDto>
    ) : StockState()
    data class Error(val message: String) : StockState()
}

/**
 * Nivel de stock para indicadores visuales
 */
enum class StockLevel {
    OUT_OF_STOCK,    // 0
    LOW_STOCK,       // 1-10
    IN_STOCK         // > 10
}

/**
 * ViewModel para gestionar el stock de productos desde la API
 */
class StockViewModel(
    private val repository: RemoteStockRepository = RemoteStockRepository()
) : ViewModel() {

    private val TAG = "StockViewModel"

    private val _stockState = MutableStateFlow<StockState>(StockState.Loading)
    val stockState: StateFlow<StockState> = _stockState.asStateFlow()

    /**
     * Carga el stock de un producto por su ID
     */
    fun loadStock(productoId: String) {
        viewModelScope.launch {
            _stockState.value = StockState.Loading
            Log.d(TAG, "Loading stock for product: $productoId")

            repository.obtenerStock(productoId).fold(
                onSuccess = { stockResponse ->
                    Log.d(TAG, "✅ Stock loaded successfully. Total: ${stockResponse.total}")
                    _stockState.value = StockState.Success(
                        total = stockResponse.total,
                        stockByAlmacen = stockResponse.stockByAlmacen
                    )
                },
                onFailure = { error ->
                    Log.e(TAG, "❌ Error loading stock: ${error.message}", error)
                    _stockState.value = StockState.Error(
                        error.message ?: "Error al cargar stock"
                    )
                }
            )
        }
    }

    /**
     * Ajusta el stock de un producto en un almacén
     * 🔐 Requiere autenticación
     */
    @Suppress("unused")
    fun adjustStock(
        productoId: String,
        almacenId: String,
        delta: Int,
        onSuccess: (Int) -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            Log.d(TAG, "Adjusting stock: producto=$productoId, almacen=$almacenId, delta=$delta")

            repository.ajustarStock(productoId, almacenId, delta).fold(
                onSuccess = { response ->
                    Log.d(TAG, "✅ Stock adjusted. New total: ${response.total}")
                    onSuccess(response.total)
                    // Recargar stock actualizado
                    loadStock(productoId)
                },
                onFailure = { error ->
                    val errorMsg = when (error) {
                        is StockException -> when (error.httpCode) {
                            403 -> "No tienes permisos para ajustar el stock"
                            409 -> "Stock insuficiente en el almacén"
                            else -> error.message ?: "Error al ajustar stock"
                        }
                        else -> error.message ?: "Error al ajustar stock"
                    }
                    Log.e(TAG, "❌ Error adjusting stock: $errorMsg", error)
                    onError(errorMsg)
                }
            )
        }
    }

    /**
     * Establece el stock absoluto de un producto en un almacén
     * 🔐 Requiere autenticación
     */
    @Suppress("unused")
    fun setStock(
        productoId: String,
        almacenId: String,
        cantidad: Int,
        onSuccess: (Int) -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            Log.d(TAG, "Setting stock: producto=$productoId, almacen=$almacenId, cantidad=$cantidad")

            repository.establecerStock(productoId, almacenId, cantidad).fold(
                onSuccess = { response ->
                    Log.d(TAG, "✅ Stock set. New total: ${response.total}")
                    onSuccess(response.total)
                    // Recargar stock actualizado
                    loadStock(productoId)
                },
                onFailure = { error ->
                    val errorMsg = when (error) {
                        is StockException -> when (error.httpCode) {
                            403 -> "No tienes permisos para establecer el stock"
                            else -> error.message ?: "Error al establecer stock"
                        }
                        else -> error.message ?: "Error al establecer stock"
                    }
                    Log.e(TAG, "❌ Error setting stock: $errorMsg", error)
                    onError(errorMsg)
                }
            )
        }
    }

    /**
     * Determina el nivel de stock para indicadores visuales
     */
    @Suppress("unused")
    fun getStockLevel(total: Int): StockLevel {
        return when {
            total == 0 -> StockLevel.OUT_OF_STOCK
            total <= 10 -> StockLevel.LOW_STOCK
            else -> StockLevel.IN_STOCK
        }
    }

    /**
     * Retorna si hay stock disponible
     */
    @Suppress("unused")
    fun hasStock(): Boolean {
        return when (val state = _stockState.value) {
            is StockState.Success -> state.total > 0
            else -> false
        }
    }

    /**
     * Obtiene el stock total actual
     */
    @Suppress("unused")
    fun getTotalStock(): Int {
        return when (val state = _stockState.value) {
            is StockState.Success -> state.total
            else -> 0
        }
    }
}
