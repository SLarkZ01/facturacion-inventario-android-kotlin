package com.example.facturacion_inventario.data.repository

import android.util.Log
import com.example.facturacion_inventario.data.remote.api.RetrofitClient
import com.example.facturacion_inventario.data.remote.mapper.ProductoMapper
import com.example.facturacion_inventario.domain.model.Product
import com.example.facturacion_inventario.domain.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Implementación del repositorio que consume la API real de Spring Boot
 * Para productos del backend
 */
class RemoteProductRepository : ProductRepository {

    private val apiService = RetrofitClient.productoApiService
    private val TAG = "RemoteProductRepo"

    /**
     * Obtiene todos los productos (sin filtros)
     * Nota: El backend devuelve lista vacía si no hay query o categoriaId,
     * por lo que agregamos una búsqueda por defecto con query vacío
     */
    override fun getProducts(): List<Product> {
        // Este mét odo es síncrono según la interfaz, pero idealmente debería ser suspend
        // Por ahora retornamos lista vacía y usamos getProductsAsync
        Log.w(TAG, "getProducts() síncrono no recomendado, usar getProductsAsync()")
        return emptyList()
    }

    /**
     * Obtiene un producto por ID
     */
    override fun getProductById(id: String): Product? {
        // Mét odo síncrono, usar getProductByIdAsync en su lugar
        Log.w(TAG, "getProductById() síncrono no recomendado, usar getProductByIdAsync()")
        return null
    }

    /**
     * Obtiene todos los productos de forma asíncrona
     * @param categoriaId Filtro opcional por categoría
     * @param query Búsqueda opcional por nombre
     */
    suspend fun getProductsAsync(categoriaId: String? = null, query: String? = null): Result<List<Product>> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Fetching products - categoriaId: $categoriaId, query: $query")
                val response = apiService.listarProductos(categoriaId, query)

                if (response.isSuccessful) {
                    val productos = response.body()?.productos ?: emptyList()
                    Log.d(TAG, "Successfully fetched ${productos.size} products")
                    Result.success(ProductoMapper.toDomainList(productos))
                } else {
                    val errorMsg = "Error ${response.code()}: ${response.message()}"
                    Log.e(TAG, errorMsg)
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception fetching products", e)
                Result.failure(e)
            }
        }
    }

    /**
     * Obtiene un producto por ID de forma asíncrona
     */
    suspend fun getProductByIdAsync(id: String): Result<Product> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Fetching product with id: $id")
                val response = apiService.obtenerProducto(id)

                if (response.isSuccessful) {
                    val producto = response.body()?.producto
                    if (producto != null) {
                        Log.d(TAG, "Successfully fetched product: ${producto.nombre}")
                        Result.success(ProductoMapper.toDomain(producto))
                    } else {
                        val errorMsg = "Product not found"
                        Log.e(TAG, errorMsg)
                        Result.failure(Exception(errorMsg))
                    }
                } else {
                    val errorMsg = "Error ${response.code()}: ${response.message()}"
                    Log.e(TAG, errorMsg)
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception fetching product by id", e)
                Result.failure(e)
            }
        }
    }

    /**
     * Busca productos por nombre
     */
    suspend fun searchProducts(query: String): Result<List<Product>> {
        return getProductsAsync(query = query)
    }

    /**
     * Obtiene productos por categoría
     */
    suspend fun getProductsByCategory(categoryId: String): Result<List<Product>> {
        return getProductsAsync(categoriaId = categoryId)
    }
}

