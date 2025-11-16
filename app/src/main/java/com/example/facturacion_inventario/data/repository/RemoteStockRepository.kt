package com.example.facturacion_inventario.data.repository

import android.util.Log
import com.example.facturacion_inventario.data.remote.api.RetrofitClient
import com.example.facturacion_inventario.data.remote.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Excepción personalizada para errores de Stock
 */
class StockException(message: String, val httpCode: Int? = null) : Exception(message)

/**
 * Repositorio para gestionar operaciones de stock con el backend
 * Implementa manejo robusto de errores y logging detallado
 */
class RemoteStockRepository {

    private val apiService = RetrofitClient.stockApiService
    private val TAG = "RemoteStockRepo"

    /**
     * Obtiene el stock total de un producto con desglose por almacén
     * GET /api/stock?productoId={id}
     *
     * @param productoId ID del producto
     * @return Result con StockResponseDto o excepción
     */
    suspend fun obtenerStock(productoId: String): Result<StockResponseDto> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "🔍 Fetching stock for producto: $productoId")
                val response = apiService.obtenerStock(productoId)

                Log.d(TAG, "📡 Response code: ${response.code()}")

                if (response.isSuccessful) {
                    val stockResponse = response.body()
                    if (stockResponse != null) {
                        Log.d(TAG, "✅ Stock total: ${stockResponse.total}")
                        stockResponse.stockByAlmacen.forEach { almacen ->
                            Log.d(TAG, "  📦 ${almacen.almacenNombre}: ${almacen.cantidad} unidades")
                        }
                        Result.success(stockResponse)
                    } else {
                        val error = "Response body is null"
                        Log.e(TAG, "❌ $error")
                        Result.failure(Exception(error))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMsg = when (response.code()) {
                        404 -> "Producto no encontrado"
                        400 -> "Solicitud inválida: $errorBody"
                        else -> "Error ${response.code()}: ${response.message()}"
                    }
                    Log.e(TAG, "❌ $errorMsg")
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Exception fetching stock: ${e.message}", e)
                Result.failure(e)
            }
        }
    }

    /**
     * Ajusta el stock en un almacén (incrementa o decrementa)
     * POST /api/stock/adjust
     * 🔐 Requiere autenticación
     *
     * @param productoId ID del producto
     * @param almacenId ID del almacén
     * @param delta Cantidad a ajustar (positivo: incrementar, negativo: decrementar)
     * @return Result con StockOperationResponse o excepción
     */
    suspend fun ajustarStock(
        productoId: String,
        almacenId: String,
        delta: Int
    ): Result<StockOperationResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val request = AdjustStockRequest(
                    productoId = productoId,
                    almacenId = almacenId,
                    delta = delta
                )

                Log.d(TAG, "🔧 Adjusting stock: productoId=$productoId, almacenId=$almacenId, delta=$delta")
                val response = apiService.ajustarStock(request)

                Log.d(TAG, "📡 Response code: ${response.code()}")

                if (response.isSuccessful) {
                    val operationResponse = response.body()
                    if (operationResponse != null) {
                        Log.d(TAG, "✅ Stock adjusted successfully. New total: ${operationResponse.total}")
                        Result.success(operationResponse)
                    } else {
                        val error = "Response body is null"
                        Log.e(TAG, "❌ $error")
                        Result.failure(Exception(error))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMsg = when (response.code()) {
                        400 -> "Datos inválidos: productoId y almacenId son requeridos"
                        403 -> "Permisos insuficientes para ajustar stock"
                        409 -> parseStockError(errorBody) ?: "Stock insuficiente en el almacén"
                        else -> "Error ${response.code()}: ${response.message()}"
                    }
                    Log.e(TAG, "❌ $errorMsg")
                    Log.e(TAG, "❌ Error body: $errorBody")
                    Result.failure(StockException(errorMsg, response.code()))
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Exception adjusting stock: ${e.message}", e)
                Result.failure(e)
            }
        }
    }

    /**
     * Establece el stock absoluto en un almacén
     * POST /api/stock/set
     * 🔐 Requiere autenticación
     *
     * @param productoId ID del producto
     * @param almacenId ID del almacén
     * @param cantidad Cantidad absoluta a establecer
     * @return Result con StockOperationResponse o excepción
     */
    suspend fun establecerStock(
        productoId: String,
        almacenId: String,
        cantidad: Int
    ): Result<StockOperationResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val request = SetStockRequest(
                    productoId = productoId,
                    almacenId = almacenId,
                    cantidad = cantidad
                )

                Log.d(TAG, "🔧 Setting stock: productoId=$productoId, almacenId=$almacenId, cantidad=$cantidad")
                val response = apiService.establecerStock(request)

                Log.d(TAG, "📡 Response code: ${response.code()}")

                if (response.isSuccessful) {
                    val operationResponse = response.body()
                    if (operationResponse != null) {
                        Log.d(TAG, "✅ Stock set successfully. New total: ${operationResponse.total}")
                        Result.success(operationResponse)
                    } else {
                        val error = "Response body is null"
                        Log.e(TAG, "❌ $error")
                        Result.failure(Exception(error))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMsg = when (response.code()) {
                        400 -> "Datos inválidos: productoId y almacenId son requeridos"
                        403 -> "Permisos insuficientes para establecer stock"
                        else -> "Error ${response.code()}: ${response.message()}"
                    }
                    Log.e(TAG, "❌ $errorMsg")
                    Log.e(TAG, "❌ Error body: $errorBody")
                    Result.failure(StockException(errorMsg, response.code()))
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Exception setting stock: ${e.message}", e)
                Result.failure(e)
            }
        }
    }

    /**
     * Parsea el mensaje de error del backend para obtener información detallada
     */
    private fun parseStockError(errorBody: String?): String? {
        return try {
            errorBody?.let {
                // Intenta parsear el JSON de error
                val errorPattern = """"error"\s*:\s*"([^"]+)"""".toRegex()
                errorPattern.find(it)?.groupValues?.get(1)
            }
        } catch (@Suppress("SwallowedException") e: Exception) {
            null
        }
    }
}
