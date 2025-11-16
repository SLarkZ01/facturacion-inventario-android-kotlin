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
                Log.d(TAG, "🔍 Fetching products - categoriaId: $categoriaId, query: $query")
                val response = apiService.listarProductos(categoriaId, query)

                Log.d(TAG, "📡 Response code: ${response.code()}")
                Log.d(TAG, "📡 Response success: ${response.isSuccessful}")

                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d(TAG, "📦 Response body: $body")
                    
                    val productos = body?.productos ?: emptyList()
                    Log.d(TAG, "✅ Successfully fetched ${productos.size} products")
                    
                    if (productos.isEmpty()) {
                        Log.w(TAG, "⚠️ Empty product list received from API")
                    } else {
                        productos.forEachIndexed { index, prod ->
                            Log.d(TAG, "  [$index] ID: ${prod.id}, Name: ${prod.nombre}, CatId: ${prod.categoriaId}")
                        }
                    }
                    
                    Result.success(ProductoMapper.toDomainList(productos))
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMsg = "Error ${response.code()}: ${response.message()}"
                    Log.e(TAG, "❌ $errorMsg")
                    Log.e(TAG, "❌ Error body: $errorBody")
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Exception fetching products: ${e.message}", e)
                Log.e(TAG, "❌ Exception type: ${e.javaClass.simpleName}")
                Log.e(TAG, "❌ Stack trace: ${e.stackTraceToString()}")
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
                Log.d(TAG, "🔍 Fetching product with id: $id")
                val response = apiService.obtenerProducto(id)

                Log.d(TAG, "📡 Response code: ${response.code()}")

                if (response.isSuccessful) {
                    val producto = response.body()?.producto
                    if (producto != null) {
                        Log.d(TAG, "✅ Successfully fetched product: ${producto.nombre}")
                        Result.success(ProductoMapper.toDomain(producto))
                    } else {
                        val errorMsg = "Producto no encontrado"
                        Log.e(TAG, "❌ $errorMsg")
                        Result.failure(Exception(errorMsg))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMsg = "Error ${response.code()}: ${response.message()}"
                    Log.e(TAG, "❌ $errorMsg")
                    Log.e(TAG, "❌ Error body: $errorBody")
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Exception fetching product by id: ${e.message}", e)
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

    /**
     * Obtiene productos públicos (con stock agregado) de forma asíncrona
     * @param categoriaId Filtro opcional por categoría
     * @param query Búsqueda opcional por nombre
     * @param page Número de página (default: 0)
     * @param size Tamaño de página (default: 20)
     */
    suspend fun getPublicProductosAsync(
        categoriaId: String? = null,
        query: String? = null,
        page: Int = 0,
        size: Int = 20
    ): Result<List<Product>> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "🔍 Fetching PUBLIC products - categoriaId: $categoriaId, query: $query")
                val response = apiService.getPublicProductos(query, categoriaId, page, size)

                Log.d(TAG, "📡 Response code: ${response.code()}")

                if (response.isSuccessful) {
                    val body = response.body()
                    val productos = body?.productos ?: emptyList()
                    Log.d(TAG, "✅ Successfully fetched ${productos.size} PUBLIC products")

                    // Log stock information
                    productos.forEachIndexed { index, prod ->
                        Log.d(TAG, "  [$index] ${prod.nombre} - stock=${prod.stock}, totalStock=${prod.totalStock}")
                        prod.stockByAlmacen?.forEach { stockAlmacen ->
                            Log.d(TAG, "    Almacén ${stockAlmacen.almacenId}: ${stockAlmacen.cantidad}")
                        }
                    }

                    Result.success(ProductoMapper.toDomainList(productos))
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMsg = "Error ${response.code()}: ${response.message()} - $errorBody"
                    Log.e(TAG, "❌ API Error (public): $errorMsg")
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Exception fetching public products: ${e.javaClass.simpleName}", e)
                Result.failure(e)
            }
        }
    }

    /**
     * Obtiene un producto público por ID de forma asíncrona
     */
    suspend fun getPublicProductoByIdAsync(id: String): Result<Product> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "🔍 Fetching PUBLIC product with id: $id")
                val response = apiService.getPublicProducto(id)

                Log.d(TAG, "📡 Response code: ${response.code()}")

                if (response.isSuccessful) {
                    val producto = response.body()?.producto
                    if (producto != null) {
                        Log.d(TAG, "✅ Successfully fetched PUBLIC product: ${producto.nombre}")
                        Log.d(TAG, "   Stock: ${producto.stock}, TotalStock: ${producto.totalStock}")
                        Result.success(ProductoMapper.toDomain(producto))
                    } else {
                        val errorMsg = "Producto no encontrado"
                        Log.e(TAG, "❌ $errorMsg")
                        Result.failure(Exception(errorMsg))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMsg = "Error ${response.code()}: ${response.message()} - $errorBody"
                    Log.e(TAG, "❌ API Error (public): $errorMsg")
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Exception fetching public product by id: ${e.javaClass.simpleName}", e)
                Result.failure(e)
            }
        }
    }
}