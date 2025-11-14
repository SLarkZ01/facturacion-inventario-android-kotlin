package com.example.facturacion_inventario.data.remote.api

import com.example.facturacion_inventario.data.remote.model.*
import retrofit2.Response
import retrofit2.http.*

/**
 * API Service para consumir los endpoints de stock del backend Spring Boot
 * Base URL: /api/stock
 *
 * Endpoints disponibles:
 * - GET /api/stock?productoId={id} - Obtiene stock total con desglose por almacén
 * - POST /api/stock/adjust - Ajusta stock (incrementa/decrementa) 🔐
 * - POST /api/stock/set - Establece stock absoluto 🔐
 */
interface StockApiService {

    /**
     * GET /api/stock?productoId={id}
     * Obtiene el stock total de un producto con desglose por almacén
     * ✅ ENDPOINT PRINCIPAL - No requiere autenticación
     *
     * @param productoId ID del producto a consultar
     * @return StockResponseDto con stockByAlmacen[] y total
     */
    @GET("api/stock")
    suspend fun obtenerStock(
        @Query("productoId") productoId: String
    ): Response<StockResponseDto>

    /**
     * POST /api/stock/adjust
     * Ajusta el stock en un almacén específico (incrementa o decrementa)
     * 🔐 Requiere autenticación + permisos
     *
     * @param request AdjustStockRequest con productoId, almacenId y delta
     * @return StockOperationResponse con stock actualizado y total
     *
     * Errores posibles:
     * - 400: productoId y almacenId requeridos
     * - 403: Permisos insuficientes
     * - 409: Stock insuficiente en almacén
     */
    @POST("api/stock/adjust")
    suspend fun ajustarStock(
        @Body request: AdjustStockRequest
    ): Response<StockOperationResponse>

    /**
     * POST /api/stock/set
     * Establece el stock absoluto en un almacén (setea cantidad exacta)
     * 🔐 Requiere autenticación + permisos
     *
     * @param request SetStockRequest con productoId, almacenId y cantidad
     * @return StockOperationResponse con stock actualizado y total
     *
     * Errores posibles:
     * - 400: productoId y almacenId requeridos
     * - 403: Permisos insuficientes
     */
    @POST("api/stock/set")
    suspend fun establecerStock(
        @Body request: SetStockRequest
    ): Response<StockOperationResponse>
}
