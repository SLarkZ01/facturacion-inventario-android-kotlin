package com.example.facturacion_inventario.data.remote.api

import com.example.facturacion_inventario.data.remote.model.CheckoutRequest
import com.example.facturacion_inventario.data.remote.model.FacturaResponse
import com.example.facturacion_inventario.data.remote.model.FacturasResponse
import retrofit2.Response
import retrofit2.http.*

/**
 * API Service para consumir los endpoints de facturas del backend Spring Boot
 * Base URL: /api/facturas
 *
 * Endpoints principales:
 * - POST /api/facturas/checkout: Convertir carrito en factura (requiere autenticación)
 * - GET /api/facturas: Listar facturas por usuario
 * - GET /api/facturas/{id}: Obtener detalle de factura
 */
interface FacturaApiService {

    /**
     * POST /api/facturas/checkout
     * Convierte un carrito en factura automáticamente
     * Requiere autenticación JWT
     *
     * @param request Contiene el carritoId
     * @return Response con la factura creada
     *
     * Errores posibles:
     * - 401: No autenticado
     * - 400: Carrito inválido o vacío
     * - 409: Stock insuficiente
     */
    @POST("api/facturas/checkout")
    suspend fun checkout(
        @Body request: CheckoutRequest
    ): Response<FacturaResponse>

    /**
     * GET /api/facturas?userId={id}
     * Lista todas las facturas de un usuario
     *
     * @param userId ID del usuario
     * @return Response con lista de facturas
     */
    @GET("api/facturas")
    suspend fun listarFacturas(
        @Query("userId") userId: String? = null
    ): Response<FacturasResponse>

    /**
     * GET /api/facturas/{id}
     * Obtiene el detalle de una factura específica
     *
     * @param id ID de la factura
     * @return Response con la factura completa
     */
    @GET("api/facturas/{id}")
    suspend fun obtenerFactura(
        @Path("id") id: String
    ): Response<FacturaResponse>
}

