package com.example.facturacion_inventario.domain.usecase

import com.example.facturacion_inventario.data.repository.RemoteProductRepository
import com.example.facturacion_inventario.domain.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Casos de uso para gestionar productos desde la API
 * Encapsula la lógica de negocio de productos y facilita el testing
 */
class ProductUseCases(
    private val repository: RemoteProductRepository = RemoteProductRepository()
) {

    /**
     * Obtiene todos los productos desde la API
     * @return Result con lista de productos
     */
    suspend fun obtenerProductos(): Result<List<Product>> {
        return withContext(Dispatchers.IO) {
            repository.getProductsAsync()
        }
    }

    /**
     * Obtiene un producto por su ID
     * @param productoId ID del producto
     * @return Result con el producto
     */
    suspend fun obtenerProductoPorId(productoId: String): Result<Product> {
        return withContext(Dispatchers.IO) {
            repository.getProductByIdAsync(productoId)
        }
    }

    /**
     * Obtiene productos por categoría
     * @param categoriaId ID de la categoría
     * @return Result con lista de productos de esa categoría
     */
    suspend fun obtenerProductosPorCategoria(categoriaId: String): Result<List<Product>> {
        return withContext(Dispatchers.IO) {
            repository.getProductsAsync(categoriaId = categoriaId)
        }
    }

    /**
     * Busca productos por nombre o descripción
     * @param query Texto de búsqueda
     * @return Result con lista de productos filtrados
     */
    suspend fun buscarProductos(query: String): Result<List<Product>> {
        return withContext(Dispatchers.IO) {
            repository.getProductsAsync(query = query)
        }
    }
}
