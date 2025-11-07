package com.example.facturacion_inventario.data.remote.api

import com.example.facturacion_inventario.data.remote.model.*
import retrofit2.Response
import retrofit2.http.*

/**
 * API Service para consumir los endpoints de productos del backend Spring Boot
 * Base URL: /api/productos
 */
interface ProductoApiService {

    /**
     * GET /api/productos
     * Listar productos con filtros opcionales
     * @param categoriaId Filtrar por categoría (opcional)
     * @param q Buscar por nombre (opcional)
     */
    @GET("api/productos")
    suspend fun listarProductos(
        @Query("categoriaId") categoriaId: String? = null,
        @Query("q") query: String? = null
    ): Response<ProductosResponse>

    /**
     * GET /api/productos/{id}
     * Obtener un producto por ID
     */
    @GET("api/productos/{id}")
    suspend fun obtenerProducto(
        @Path("id") id: String
    ): Response<ProductoResponse>

    /**
     * POST /api/productos
     * Crear un nuevo producto
     */
    @POST("api/productos")
    suspend fun crearProducto(
        @Body request: ProductoRequest
    ): Response<Map<String, @JvmSuppressWildcards Any?>>

    /**
     * PUT /api/productos/{id}
     * Actualizar un producto existente
     */
    @PUT("api/productos/{id}")
    suspend fun actualizarProducto(
        @Path("id") id: String,
        @Body request: ProductoRequest
    ): Response<Map<String, @JvmSuppressWildcards Any?>>

    /**
     * PATCH /api/productos/{id}/stock
     * Ajustar el stock de un producto
     * @param body Mapa con "delta" (Int) para incrementar/decrementar stock
     */
    @PATCH("api/productos/{id}/stock")
    suspend fun ajustarStock(
        @Path("id") id: String,
        @Body body: Map<String, Int>
    ): Response<Map<String, @JvmSuppressWildcards Any?>>

    /**
     * DELETE /api/productos/{id}
     * Eliminar un producto
     */
    @DELETE("api/productos/{id}")
    suspend fun eliminarProducto(
        @Path("id") id: String
    ): Response<Map<String, @JvmSuppressWildcards Any?>>
}

