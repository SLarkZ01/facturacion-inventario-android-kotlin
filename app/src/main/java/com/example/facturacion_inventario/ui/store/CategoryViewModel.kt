package com.example.facturacion_inventario.ui.store

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facturacion_inventario.data.repository.RemoteCategoryRepository
import com.example.facturacion_inventario.data.repository.RemoteProductRepository
import com.example.facturacion_inventario.domain.model.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.async

/**
 * Estados posibles para la carga de categorías
 */
sealed class CategoryListState {
    object Loading : CategoryListState()
    data class Success(val categories: List<Category>) : CategoryListState()
    data class Error(val message: String) : CategoryListState()
    object Empty : CategoryListState()
}

/**
 * Estados para el detalle de una categoría
 */
sealed class CategoryDetailState {
    object Loading : CategoryDetailState()
    data class Success(val category: Category) : CategoryDetailState()
    data class Error(val message: String) : CategoryDetailState()
}

/**
 * ViewModel para gestionar categorías desde la API real de Spring Boot
 * SIN fallback - SOLO usa datos de la API
 */
class CategoryViewModel(
    private val repository: RemoteCategoryRepository = RemoteCategoryRepository(),
    private val productRepository: RemoteProductRepository = RemoteProductRepository()
) : ViewModel() {

    private val TAG = "CategoryViewModel"

    private val _uiState = MutableStateFlow<CategoryListState>(CategoryListState.Loading)
    val uiState: StateFlow<CategoryListState> = _uiState.asStateFlow()

    private val _categoryDetail = MutableStateFlow<CategoryDetailState>(CategoryDetailState.Loading)
    @Suppress("unused", "MemberVisibilityCanBePrivate")
    val categoryDetail: StateFlow<CategoryDetailState> = _categoryDetail.asStateFlow()

    init {
        Log.d(TAG, "CategoryViewModel initialized - loading categories from API...")
        loadCategories()
    }

    /**
     * Filtra categorías que tienen al menos un producto
     * @param categories Lista de categorías a filtrar
     * @return Lista de categorías que tienen productos
     */
    private suspend fun filterCategoriesWithProducts(categories: List<Category>): List<Category> {
        Log.d(TAG, "🔍 Filtrando categorías con productos...")

        // Usar async para consultar todas las categorías en paralelo
        val categoriesWithProducts = categories.mapNotNull { category ->
            viewModelScope.async {
                try {
                    // Consultar productos de esta categoría
                    val result = productRepository.getProductsAsync(categoriaId = category.id)
                    val hasProducts = result.getOrNull()?.isNotEmpty() == true

                    if (hasProducts) {
                        Log.d(TAG, "  ✅ ${category.name} tiene productos")
                        category
                    } else {
                        Log.d(TAG, "  ⚠️ ${category.name} NO tiene productos (oculta)")
                        null
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "  ❌ Error verificando productos de ${category.name}: ${e.message}")
                    // En caso de error, incluir la categoría por seguridad
                    category
                }
            }
        }.mapNotNull { it.await() }

        Log.d(TAG, "📊 Resultado: ${categoriesWithProducts.size} de ${categories.size} categorías tienen productos")
        return categoriesWithProducts
    }

    /**
     * Carga todas las categorías desde la API (sin fallback a datos locales)
     * Por defecto carga TODAS las categorías (globales + de talleres)
     * NUEVO: Filtra automáticamente categorías sin productos
     */
    fun loadCategories(
        query: String? = null,
        tallerId: String? = null,
        global: Boolean = false,
        todas: Boolean = true,
        page: Int = 0,
        size: Int = 100,
        filterEmpty: Boolean = true // ← NUEVO: Parámetro para filtrar categorías vacías
    ) {
        viewModelScope.launch {
            try {
                _uiState.value = CategoryListState.Loading
                Log.d(TAG, "🔍 Loading categories from API")
                Log.d(TAG, "  📋 Params: query=$query, tallerId=$tallerId, global=$global, todas=$todas, filterEmpty=$filterEmpty")

                repository.getCategoriesAsync(
                    query = query,
                    tallerId = tallerId,
                    global = global,
                    todas = todas,
                    page = page,
                    size = size
                ).fold(
                    onSuccess = { categories ->
                        Log.d(TAG, "✅ SUCCESS: Loaded ${categories.size} categories from API")

                        // Filtrar categorías sin productos si filterEmpty está activado
                        val finalCategories = if (filterEmpty) {
                            filterCategoriesWithProducts(categories)
                        } else {
                            categories
                        }

                        finalCategories.forEachIndexed { index, cat ->
                            val tipo = if (cat.tallerId == null) "GLOBAL" else "TALLER(${cat.tallerId})"
                            Log.d(TAG, "  [$index] ${cat.name} - $tipo - ID: ${cat.id}")
                        }

                        _uiState.value = if (finalCategories.isEmpty()) {
                            Log.w(TAG, "⚠️ No hay categorías con productos")
                            CategoryListState.Empty
                        } else {
                            CategoryListState.Success(finalCategories)
                        }
                    },
                    onFailure = { error ->
                        Log.e(TAG, "❌ ERROR loading categories from API: ${error.message}", error)
                        _uiState.value = CategoryListState.Error(
                            error.message ?: "Error al cargar categorías desde el servidor"
                        )
                    }
                )
            } catch (e: Exception) {
                Log.e(TAG, "❌ EXCEPTION in loadCategories: ${e.message}", e)
                _uiState.value = CategoryListState.Error(
                    "Error inesperado: ${e.message ?: "No se pudo conectar con el servidor"}"
                )
            }
        }
    }

    /**
     * Carga una categoría específica por ID
     */
    @Suppress("unused", "MemberVisibilityCanBePrivate")
    fun loadCategoryById(id: String) {
        viewModelScope.launch {
            try {
                _categoryDetail.value = CategoryDetailState.Loading
                Log.d(TAG, "Loading category by id: $id")

                repository.getCategoryByIdAsync(id).fold(
                    onSuccess = { category ->
                        Log.d(TAG, "Loaded category: ${category.name}")
                        _categoryDetail.value = CategoryDetailState.Success(category)
                    },
                    onFailure = { error ->
                        Log.e(TAG, "Error loading category from API", error)
                        _categoryDetail.value = CategoryDetailState.Error(
                            error.message ?: "Error al cargar categoría"
                        )
                    }
                )
            } catch (e: Exception) {
                Log.e(TAG, "Exception loading category", e)
                _categoryDetail.value = CategoryDetailState.Error(
                    "Error al cargar la categoría: ${e.message}"
                )
            }
        }
    }

    /**
     * Busca categorías por nombre
     * NUEVO: También filtra categorías sin productos
     */
    @Suppress("unused")
    fun searchCategories(query: String, page: Int = 0, size: Int = 100, filterEmpty: Boolean = true) {
        viewModelScope.launch {
            try {
                _uiState.value = CategoryListState.Loading
                Log.d(TAG, "Searching categories with query: $query")

                repository.searchCategories(query, page, size).fold(
                    onSuccess = { categories ->
                        Log.d(TAG, "Found ${categories.size} categories matching '$query'")

                        // Filtrar categorías sin productos si filterEmpty está activado
                        val finalCategories = if (filterEmpty) {
                            filterCategoriesWithProducts(categories)
                        } else {
                            categories
                        }

                        _uiState.value = if (finalCategories.isEmpty()) {
                            CategoryListState.Empty
                        } else {
                            CategoryListState.Success(finalCategories)
                        }
                    },
                    onFailure = { error ->
                        Log.e(TAG, "Error searching categories", error)
                        _uiState.value = CategoryListState.Error(
                            error.message ?: "Error al buscar categorías"
                        )
                    }
                )
            } catch (e: Exception) {
                Log.e(TAG, "Exception searching categories", e)
                _uiState.value = CategoryListState.Error("Error al buscar: ${e.message}")
            }
        }
    }

    /**
     * Reintentar carga
     */
    @Suppress("unused")
    fun retry() {
        loadCategories()
    }
}
