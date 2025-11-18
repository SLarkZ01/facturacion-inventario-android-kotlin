package com.example.facturacion_inventario.ui.store

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facturacion_inventario.data.repository.RemoteProductRepository
import com.example.facturacion_inventario.domain.model.Product
import com.example.facturacion_inventario.BuildConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estados posibles para la carga de productos
 */
sealed class ProductListState {
    object Loading : ProductListState()
    data class Success(val products: List<Product>) : ProductListState()
    data class Error(val message: String) : ProductListState()
    object Empty : ProductListState()
}

/**
 * Estados para el detalle de un producto
 */
sealed class ProductDetailState {
    object Loading : ProductDetailState()
    data class Success(val product: Product) : ProductDetailState()
    data class Error(val message: String) : ProductDetailState()
}

/**
 * ViewModel para gestionar la lista de productos desde la API
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
class ProductListViewModel(
    private val repository: RemoteProductRepository = RemoteProductRepository()
) : ViewModel() {

    private val TAG = "ProductListViewModel"

    // Estado público mantenido por compatibilidad con pantallas que usan la API anterior
    private val _uiState = MutableStateFlow<ProductListState>(ProductListState.Loading)
    @Suppress("unused")
    val uiState: StateFlow<ProductListState> = _uiState.asStateFlow()

    // Cache por categoría: cada categoría tiene su MutableStateFlow con la lista de productos
    private val categoryFlows = mutableMapOf<String, MutableStateFlow<List<Product>>>()

    // Guardar timestamps opcionales para debugging si es necesario
    private val fetchedTimestamps = mutableMapOf<String, Long>()

    init {
        // Cargar productos generales inicialmente (comportamiento existente)
        loadProducts()
    }

    /**
     * Devuelve un StateFlow con la lista de productos para una categoría (crea si no existe)
     */
    fun productsForCategoryFlow(categoryId: String): StateFlow<List<Product>> {
        val key = categoryId
        synchronized(categoryFlows) {
            val existing = categoryFlows[key]
            if (existing != null) return existing.asStateFlow()
            val flow = MutableStateFlow<List<Product>>(emptyList())
            categoryFlows[key] = flow
            return flow.asStateFlow()
        }
    }

    /**
     * Carga productos de la API (general o por categoría). Si ya se cargaron para esa categoría,
     * la función simplemente retorna (no dispara otra petición). Esto evita fetch repetidos desde composables.
     */
    fun fetchProductsByCategory(categoryId: String) {
        val key = categoryId
        val existingFlow = synchronized(categoryFlows) { categoryFlows[key] }
        // Si existe y no está vacío, asumimos que ya lo cargamos recientemente
        if (existingFlow != null && existingFlow.value.isNotEmpty()) {
            if (BuildConfig.DEBUG) Log.d(TAG, "Skipping fetch for category=$key because cache exists (size=${existingFlow.value.size})")
            return
        }

        // Asegurar que el flow existe
        val flow = synchronized(categoryFlows) {
            categoryFlows.getOrPut(key) { MutableStateFlow(emptyList()) }
        }

        viewModelScope.launch {
            if (BuildConfig.DEBUG) Log.d(TAG, "📡 Fetching PUBLIC products for category=$key from ViewModel")
            repository.getPublicProductosAsync(categoriaId = categoryId).fold(
                onSuccess = { list ->
                    if (BuildConfig.DEBUG) Log.d(TAG, "✅ Fetched ${list.size} products for category=$key")
                    flow.value = list
                    fetchedTimestamps[key] = System.currentTimeMillis()
                },
                onFailure = { err ->
                    Log.e(TAG, "❌ Error fetching products for category=$key: ${err.message}")
                    // En caso de error dejamos la lista vacía para distinguir estado
                    flow.value = emptyList()
                }
            )
        }
    }

    /**
     * Carga todos los productos (comportamiento previo) — mantiene compatibilidad
     */
    fun loadProducts(categoryId: String? = null, query: String? = null) {
        viewModelScope.launch {
            _uiState.value = ProductListState.Loading

            repository.getProductsAsync(categoryId, query).fold(
                onSuccess = { products ->
                    if (BuildConfig.DEBUG) Log.d(TAG, "Loaded ${products.size} products successfully")
                    _uiState.value = if (products.isEmpty()) {
                        ProductListState.Empty
                    } else {
                        ProductListState.Success(products)
                    }
                },
                onFailure = { error ->
                    Log.e(TAG, "Error loading products", error)
                    _uiState.value = ProductListState.Error(
                        error.message ?: "Error al cargar productos"
                    )
                }
            )
        }
    }

    /**
     * Busca productos por nombre
     */
    @Suppress("unused")
    fun searchProducts(query: String) {
        // Función mantenida por compatibilidad con código legado
        loadProducts(query = query)
    }

    /**
     * Filtra productos por categoría (mantiene compatibilidad con API existente)
     */
    @Suppress("unused")
    fun filterByCategory(categoryId: String) {
        // Compatibilidad con llamadas directas a filtrar
        loadProducts(categoryId = categoryId)
    }

    /**
     * Reintentar carga
     */
    @Suppress("unused")
    fun retry() {
        loadProducts()
    }
}

/**
 * ViewModel para gestionar el detalle de un producto
 */
class ProductDetailViewModel(
    private val repository: RemoteProductRepository = RemoteProductRepository()
) : ViewModel() {

    private val TAG = "ProductDetailViewModel"

    private val _uiState = MutableStateFlow<ProductDetailState>(ProductDetailState.Loading)
    val uiState: StateFlow<ProductDetailState> = _uiState.asStateFlow()

    /**
     * Carga un producto por ID usando el endpoint público
     */
    fun loadProduct(productId: String) {
        viewModelScope.launch {
            _uiState.value = ProductDetailState.Loading

            repository.getPublicProductoByIdAsync(productId).fold(
                onSuccess = { product ->
                    if (BuildConfig.DEBUG) Log.d(TAG, "Loaded PUBLIC product: ${product.name} with stock=${product.stock}")
                    _uiState.value = ProductDetailState.Success(product)
                },
                onFailure = { error ->
                    Log.e(TAG, "Error loading public product", error)
                    _uiState.value = ProductDetailState.Error(
                        error.message ?: "Error al cargar el producto"
                    )
                }
            )
        }
    }

    /**
     * Reintentar carga
     */
    fun retry(productId: String) {
        loadProduct(productId)
    }
}
