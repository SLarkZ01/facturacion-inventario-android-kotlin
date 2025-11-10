package com.example.facturacion_inventario.ui.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facturacion_inventario.data.repository.RemoteProductRepository
import com.example.facturacion_inventario.domain.model.Product
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para manejar la búsqueda de productos desde el backend
 * Incluye debounce para evitar demasiadas llamadas a la API
 */
class SearchViewModel(
    private val repository: RemoteProductRepository = RemoteProductRepository()
) : ViewModel() {

    private val _searchResults = MutableStateFlow<List<Product>>(emptyList())
    val searchResults: StateFlow<List<Product>> = _searchResults.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private var searchJob: Job? = null

    /**
     * Busca productos en el backend con debounce
     * @param query Texto de búsqueda
     * @param debounceMs Tiempo de espera antes de buscar (ms)
     */
    fun searchProducts(query: String, debounceMs: Long = 300) {
        // Cancelar búsqueda anterior si existe
        searchJob?.cancel()

        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }

        searchJob = viewModelScope.launch {
            // Debounce: esperar antes de hacer la búsqueda
            delay(debounceMs)

            _isSearching.value = true

            try {
                // Buscar productos usando el query
                val result = repository.getProductsAsync(query = query)

                if (result.isSuccess) {
                    val productos = result.getOrNull() ?: emptyList()
                    // Tomar máximo 5 resultados
                    _searchResults.value = productos.take(5)
                } else {
                    _searchResults.value = emptyList()
                }
            } catch (_: Exception) {
                _searchResults.value = emptyList()
            } finally {
                _isSearching.value = false
            }
        }
    }

    /**
     * Limpia los resultados de búsqueda
     */
    fun clearResults() {
        searchJob?.cancel()
        _searchResults.value = emptyList()
        _isSearching.value = false
    }
}
