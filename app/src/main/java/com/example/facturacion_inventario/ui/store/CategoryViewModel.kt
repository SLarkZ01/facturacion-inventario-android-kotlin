package com.example.facturacion_inventario.ui.store

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facturacion_inventario.data.repository.RemoteCategoryRepository
import com.example.facturacion_inventario.domain.model.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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
    private val repository: RemoteCategoryRepository = RemoteCategoryRepository()
) : ViewModel() {

    private val TAG = "CategoryViewModel"

    private val _uiState = MutableStateFlow<CategoryListState>(CategoryListState.Loading)
    val uiState: StateFlow<CategoryListState> = _uiState.asStateFlow()

    private val _categoryDetail = MutableStateFlow<CategoryDetailState>(CategoryDetailState.Loading)
    val categoryDetail: StateFlow<CategoryDetailState> = _categoryDetail.asStateFlow()

    init {
        Log.d(TAG, "CategoryViewModel initialized - loading categories from API...")
        loadCategories()
    }

    /**
     * Carga todas las categorías desde la API (sin fallback a datos locales)
     */
    fun loadCategories(
        query: String? = null,
        tallerId: String? = null,
        global: Boolean = false,
        page: Int = 0,
        size: Int = 100
    ) {
        viewModelScope.launch {
            try {
                _uiState.value = CategoryListState.Loading
                Log.d(TAG, "🔍 Loading categories from API - query: $query, tallerId: $tallerId, global: $global")

                repository.getCategoriesAsync(
                    query = query,
                    tallerId = tallerId,
                    global = global,
                    page = page,
                    size = size
                ).fold(
                    onSuccess = { categories ->
                        Log.d(TAG, "✅ SUCCESS: Loaded ${categories.size} categories from API")
                        categories.forEachIndexed { index, cat ->
                            Log.d(TAG, "  [$index] ID: ${cat.id}, Name: ${cat.name}")
                        }
                        _uiState.value = if (categories.isEmpty()) {
                            Log.w(TAG, "⚠️ Empty list received from API")
                            CategoryListState.Empty
                        } else {
                            CategoryListState.Success(categories)
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
     */
    fun searchCategories(query: String, page: Int = 0, size: Int = 100) {
        viewModelScope.launch {
            try {
                _uiState.value = CategoryListState.Loading
                Log.d(TAG, "Searching categories with query: $query")

                repository.searchCategories(query, page, size).fold(
                    onSuccess = { categories ->
                        Log.d(TAG, "Found ${categories.size} categories")
                        _uiState.value = if (categories.isEmpty()) {
                            CategoryListState.Empty
                        } else {
                            CategoryListState.Success(categories)
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
                _uiState.value = CategoryListState.Error(
                    "Error en la búsqueda: ${e.message}"
                )
            }
        }
    }

    /**
     * Carga solo categorías globales
     */
    fun loadGlobalCategories(page: Int = 0, size: Int = 100) {
        loadCategories(global = true, page = page, size = size)
    }

    /**
     * Carga categorías de un taller específico
     */
    fun loadCategoriesByTaller(tallerId: String, page: Int = 0, size: Int = 100) {
        loadCategories(tallerId = tallerId, global = false, page = page, size = size)
    }

    /**
     * Reintentar carga
     */
    fun retry() {
        loadCategories()
    }
}

