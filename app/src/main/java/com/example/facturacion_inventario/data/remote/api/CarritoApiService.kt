package com.example.facturacion_inventario.data.remote.api

import com.example.facturacion_inventario.data.remote.model.*
import retrofit2.Response
import retrofit2.http.*

/**
 * API Service para consumir los endpoints de carrito del backend Spring Boot
 * Base URL: /api/carritos
 *
 * Implementa el CRUD completo de carritos
 */
interface CarritoApiService {

    /**
     * GET /api/carritos
     * Listar carritos por usuario
     * @param usuarioId ID del usuario (opcional, si es null devuelve lista vacía)
     */
    @GET("api/carritos")
    suspend fun listarCarritos(
        @Query("usuarioId") usuarioId: String? = null
    ): Response<CarritosResponse>

    /**
     * GET /api/carritos/{id}
     * Obtener un carrito por ID
     * @param id ID del carrito
     */
    @GET("api/carritos/{id}")
    suspend fun obtenerCarrito(
        @Path("id") id: String
    ): Response<CarritoResponse>

    /**
     * POST /api/carritos
     * Crear un carrito nuevo
     * @param request Datos del carrito (usuarioId opcional, items opcionales)
     */
    @POST("api/carritos")
    suspend fun crearCarrito(
        @Body request: CarritoRequest
    ): Response<CarritoResponse>

    /**
     * POST /api/carritos/merge
     * Sincronizar carrito anónimo con usuario autenticado
     * @param request Datos para el merge (anonCartId y/o items)
     */
    @POST("api/carritos/merge")
    suspend fun mergeCarrito(
        @Body request: MergeCarritoRequest
    ): Response<CarritoMessageResponse>

    /**
     * POST /api/carritos/{id}/items
     * Agregar un item al carrito
     * @param id ID del carrito
     * @param item Item a agregar
     */
    @POST("api/carritos/{id}/items")
    suspend fun agregarItem(
        @Path("id") id: String,
        @Body item: CarritoItemRequest
    ): Response<CarritoResponse>

    /**
     * DELETE /api/carritos/{id}/items/{productoId}
     * Remover un item del carrito
     * @param id ID del carrito
     * @param productoId ID del producto a remover
     */
    @DELETE("api/carritos/{id}/items/{productoId}")
    suspend fun removerItem(
        @Path("id") id: String,
        @Path("productoId") productoId: String
    ): Response<CarritoResponse>

    /**
     * POST /api/carritos/{id}/clear
     * Vaciar carrito (eliminar todos los items)
     * @param id ID del carrito
     */
    @POST("api/carritos/{id}/clear")
    suspend fun vaciarCarrito(
        @Path("id") id: String
    ): Response<CarritoResponse>

    /**
     * DELETE /api/carritos/{id}
     * Eliminar un carrito completamente
     * @param id ID del carrito
     */
    @DELETE("api/carritos/{id}")
    suspend fun eliminarCarrito(
        @Path("id") id: String
    ): Response<CarritoMessageResponse>
}
