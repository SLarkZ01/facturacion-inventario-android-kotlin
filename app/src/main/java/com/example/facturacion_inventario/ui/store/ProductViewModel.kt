package com.example.facturacion_inventario.ui.store

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facturacion_inventario.data.repository.RemoteProductRepository
import com.example.facturacion_inventario.domain.model.Product
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
class ProductListViewModel(
    private val repository: RemoteProductRepository = RemoteProductRepository()
) : ViewModel() {

    private val TAG = "ProductListViewModel"

    private val _uiState = MutableStateFlow<ProductListState>(ProductListState.Loading)
    val uiState: StateFlow<ProductListState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    /**
     * Carga todos los productos
     */
    fun loadProducts(categoryId: String? = null, query: String? = null) {
        viewModelScope.launch {
            _uiState.value = ProductListState.Loading

            repository.getProductsAsync(categoryId, query).fold(
                onSuccess = { products ->
                    Log.d(TAG, "Loaded ${products.size} products successfully")
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
    fun searchProducts(query: String) {
        loadProducts(query = query)
    }

    /**
     * Filtra productos por categoría
     */
    fun filterByCategory(categoryId: String) {
        loadProducts(categoryId = categoryId)
    }

    /**
     * Reintentar carga
     */
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
     * Carga un producto por ID
     */
    fun loadProduct(productId: String) {
        viewModelScope.launch {
            _uiState.value = ProductDetailState.Loading

            repository.getProductByIdAsync(productId).fold(
                onSuccess = { product ->
                    Log.d(TAG, "Loaded product: ${product.name}")
                    _uiState.value = ProductDetailState.Success(product)
                },
                onFailure = { error ->
                    Log.e(TAG, "Error loading product", error)
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

