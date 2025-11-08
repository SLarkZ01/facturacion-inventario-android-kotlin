package com.example.facturacion_inventario.data.remote.api

import com.example.facturacion_inventario.data.remote.model.CategoriaResponse
import com.example.facturacion_inventario.data.remote.model.CategoriasResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Servicio Retrofit para consumir la API de categorías
 * Base URL: /api/categorias
 */
interface CategoriaApiService {

    /**
     * GET /api/categorias
     * Lista todas las categorías con filtros opcionales y paginación
     *
     * @param query Búsqueda por nombre (opcional)
     * @param page Número de página (default: 0)
     * @param size Tamaño de página (default: 20)
     * @param tallerId Filtro por taller (opcional, null = todas)
     * @param global Si true, solo categorías globales (tallerId == null)
     */
    @GET("api/categorias")
    suspend fun listarCategorias(
        @Query("query") query: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("tallerId") tallerId: String? = null,
        @Query("global") global: Boolean = false
    ): Response<CategoriasResponse>

    /**
     * GET /api/categorias/{id}
     * Obtiene una categoría específica por ID
     *
     * @param id ID de la categoría (MongoDB ObjectId)
     */
    @GET("api/categorias/{id}")
    suspend fun obtenerCategoria(
        @Path("id") id: String
    ): Response<CategoriaResponse>
}

