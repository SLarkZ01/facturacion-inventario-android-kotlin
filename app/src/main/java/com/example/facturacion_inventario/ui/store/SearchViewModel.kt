package com.example.facturacion_inventario.ui.store

import android.util.Log
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
import java.text.Normalizer

/**
 * ViewModel para manejar la búsqueda de productos desde el backend
 * Incluye debounce para evitar demasiadas llamadas a la API
 * Búsqueda insensible a tildes/acentos
 */
class SearchViewModel(
    private val repository: RemoteProductRepository = RemoteProductRepository()
) : ViewModel() {

    private val TAG = "SearchViewModel"

    private val _searchResults = MutableStateFlow<List<Product>>(emptyList())
    val searchResults: StateFlow<List<Product>> = _searchResults.asStateFlow()

    // Nuevo: Estado para sugerencias de autocompletado
    private val _suggestions = MutableStateFlow<List<Product>>(emptyList())
    val suggestions: StateFlow<List<Product>> = _suggestions.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    // Nuevo: Estado para indicar si se están cargando sugerencias
    private val _isLoadingSuggestions = MutableStateFlow(false)
    val isLoadingSuggestions: StateFlow<Boolean> = _isLoadingSuggestions.asStateFlow()

    private var searchJob: Job? = null
    private var suggestionsJob: Job? = null

    /**
     * Normaliza texto removiendo tildes/acentos y convirtiéndolo a minúsculas
     * Ejemplos:
     * - "Bujía" → "bujia"
     * - "ÑOÑO" → "nono"
     * - "Aceité" → "aceite"
     */
    private fun normalizeText(text: String): String {
        // Convertir a minúsculas
        val lowercase = text.lowercase()

        // Remover tildes y acentos usando NFD (Normalization Form Canonical Decomposition)
        val normalized = Normalizer.normalize(lowercase, Normalizer.Form.NFD)

        // Remover marcas diacríticas (acentos)
        return normalized.replace("\\p{M}".toRegex(), "")
    }

    /**
     * Busca productos en el backend con debounce
     * Búsqueda insensible a tildes, acentos y mayúsculas
     * @param query Texto de búsqueda
     * @param debounceMs Tiempo de espera antes de buscar (ms)
     */
    fun searchProducts(query: String, debounceMs: Long = 300) {
        // Cancelar búsqueda anterior si existe
        searchJob?.cancel()

        if (query.isBlank()) {
            Log.d(TAG, "Query vacío, limpiando resultados")
            _searchResults.value = emptyList()
            return
        }

        searchJob = viewModelScope.launch {
            // Debounce: esperar antes de hacer la búsqueda
            delay(debounceMs)

            _isSearching.value = true
            val searchQuery = query.trim()
            val normalizedQuery = normalizeText(searchQuery)
            Log.d(TAG, "🔍 Iniciando búsqueda")
            Log.d(TAG, "   Original: '$searchQuery'")
            Log.d(TAG, "   Normalizado: '$normalizedQuery'")

            try {
                // MÉTO DO 1: Intentar buscar con el parámetro q del backend
                Log.d(TAG, "📡 Método 1: Intentando búsqueda en backend")
                var result = repository.getProductsAsync(categoriaId = null, query = searchQuery)

                if (result.isSuccess) {
                    var productos = result.getOrNull() ?: emptyList()
                    Log.d(TAG, "✅ Backend retornó: ${productos.size} productos")

                    // Si el backend no retorna resultados, intentar filtrar todos los productos
                    if (productos.isEmpty()) {
                        Log.w(TAG, "⚠️ Backend no retornó productos")
                        Log.d(TAG, "📡 Método 2: Obteniendo TODOS los productos para filtrar localmente")

                        // Obtener TODOS los productos sin filtros
                        result = repository.getProductsAsync(categoriaId = null, query = null)

                        if (result.isSuccess) {
                            val todosLosProductos = result.getOrNull() ?: emptyList()
                            Log.d(TAG, "✅ Obtenidos ${todosLosProductos.size} productos totales")

                            // Filtrar en el cliente por nombre (INSENSIBLE A TILDES)
                            productos = todosLosProductos.filter { producto ->
                                val nombreNormalizado = normalizeText(producto.name)
                                nombreNormalizado.contains(normalizedQuery)
                            }
                            Log.d(TAG, "🔍 Filtro local encontró: ${productos.size} productos")
                        }
                    } else {
                        // El backend retornó resultados, pero también aplicar filtro local
                        // para asegurar que sea insensible a tildes
                        Log.d(TAG, "🔍 Aplicando filtro adicional insensible a tildes")
                        productos = productos.filter { producto ->
                            val nombreNormalizado = normalizeText(producto.name)
                            nombreNormalizado.contains(normalizedQuery)
                        }
                        Log.d(TAG, "✅ Después del filtro: ${productos.size} productos")
                    }

                    if (productos.isEmpty()) {
                        Log.w(TAG, "⚠️ No se encontraron productos para '$searchQuery'")
                    } else {
                        Log.d(TAG, "📋 Productos encontrados:")
                        productos.forEachIndexed { index, producto ->
                            Log.d(TAG, "  [${index + 1}] ${producto.name} (Normalizado: ${normalizeText(producto.name)})")
                        }
                    }

                    // Mostrar TODOS los resultados
                    _searchResults.value = productos
                } else {
                    val error = result.exceptionOrNull()
                    Log.e(TAG, "❌ Error en búsqueda: ${error?.message}")
                    _searchResults.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Excepción en búsqueda: ${e.message}", e)
                _searchResults.value = emptyList()
            } finally {
                _isSearching.value = false
                Log.d(TAG, "🏁 Búsqueda finalizada. Resultados: ${_searchResults.value.size}")
            }
        }
    }

    /**
     * Obtiene sugerencias de autocompletado en tiempo real
     * Similar a Amazon, muestra hasta 8 productos mientras el usuario escribe
     * @param query Texto de búsqueda
     * @param debounceMs Tiempo de espera antes de buscar (ms)
     */
    fun getSuggestions(query: String, debounceMs: Long = 200) {
        // Cancelar búsqueda anterior si existe
        suggestionsJob?.cancel()

        if (query.isBlank() || query.length < 2) {
            Log.d(TAG, "Query muy corto, limpiando sugerencias")
            _suggestions.value = emptyList()
            return
        }

        suggestionsJob = viewModelScope.launch {
            // Debounce: esperar antes de hacer la búsqueda
            delay(debounceMs)

            _isLoadingSuggestions.value = true
            val searchQuery = query.trim()
            val normalizedQuery = normalizeText(searchQuery)
            Log.d(TAG, "💡 Buscando sugerencias para: '$searchQuery'")

            try {
                // Obtener productos del backend
                val result = repository.getProductsAsync(categoriaId = null, query = searchQuery)

                if (result.isSuccess) {
                    var productos = result.getOrNull() ?: emptyList()

                    // Si el backend no retorna resultados, filtrar todos los productos
                    if (productos.isEmpty()) {
                        val allResult = repository.getProductsAsync(categoriaId = null, query = null)
                        if (allResult.isSuccess) {
                            val todosLosProductos = allResult.getOrNull() ?: emptyList()
                            productos = todosLosProductos.filter { producto ->
                                val nombreNormalizado = normalizeText(producto.name)
                                nombreNormalizado.contains(normalizedQuery)
                            }
                        }
                    } else {
                        // Aplicar filtro local insensible a tildes
                        productos = productos.filter { producto ->
                            val nombreNormalizado = normalizeText(producto.name)
                            nombreNormalizado.contains(normalizedQuery)
                        }
                    }

                    // Limitar a 8 sugerencias (estilo Amazon)
                    val sugerencias = productos.take(8)
                    Log.d(TAG, "💡 ${sugerencias.size} sugerencias encontradas")
                    _suggestions.value = sugerencias
                } else {
                    Log.e(TAG, "❌ Error obteniendo sugerencias")
                    _suggestions.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Excepción obteniendo sugerencias: ${e.message}", e)
                _suggestions.value = emptyList()
            } finally {
                _isLoadingSuggestions.value = false
            }
        }
    }

    /**
     * Limpia las sugerencias de autocompletado
     */
    fun clearSuggestions() {
        suggestionsJob?.cancel()
        _suggestions.value = emptyList()
        _isLoadingSuggestions.value = false
    }

    /**
     * Limpia los resultados de búsqueda
     */
    fun clearResults() {
        searchJob?.cancel()
        _searchResults.value = emptyList()
        _isSearching.value = false
        Log.d(TAG, "Resultados limpiados")
    }

    /**
     * Limpia to do (sugerencias y resultados)
     */
    fun clearAll() {
        clearSuggestions()
        clearResults()
    }
}