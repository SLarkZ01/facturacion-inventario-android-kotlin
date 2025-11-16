package com.example.facturacion_inventario.data.repository

import android.util.Log
import com.example.facturacion_inventario.data.remote.api.RetrofitClient
import com.example.facturacion_inventario.data.remote.mapper.FacturaMapper
import com.example.facturacion_inventario.data.remote.model.CheckoutRequest
import com.example.facturacion_inventario.domain.model.Factura
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio para consumir la API de facturas
 * Implementa las operaciones principales: checkout, listar y obtener detalle
 */
class RemoteFacturaRepository {

    private val apiService = RetrofitClient.facturaApiService
    private val TAG = "RemoteFacturaRepo"

    // ═══════════════════════════════════════════════════════════════════
    // MÉT ODO PRINCIPAL: CHECKOUT (Convertir carrito en factura)
    // ═══════════════════════════════════════════════════════════════════

    /**
     * POST /api/facturas/checkout
     * Convierte un carrito en factura automáticamente
     *
     * @param carritoId ID del carrito a convertir
     * @return Result con la factura creada
     *
     * Proceso del backend:
     * 1. Valida que el carrito exista y no esté vacío
     * 2. Valida que haya stock suficiente para todos los productos
     * 3. Crea la factura con los items del carrito
     * 4. Actualiza el stock (resta las cantidades)
     * 5. Devuelve la factura creada
     *
     * Errores posibles:
     * - 401: Usuario no autenticado (falta token JWT)
     * - 400: Carrito inválido o vacío
     * - 409: Stock insuficiente para algún producto
     */
    suspend fun checkout(carritoId: String): Result<Factura> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "🛒 Iniciando checkout para carrito: $carritoId")
                val request = CheckoutRequest(carritoId = carritoId)
                val response = apiService.checkout(request)

                Log.d(TAG, "📡 Response code: ${response.code()}")

                if (response.isSuccessful) {
                    val facturaDto = response.body()?.factura
                    if (facturaDto != null) {
                        val factura = FacturaMapper.toDomain(facturaDto)
                        Log.d(TAG, "✅ Factura creada exitosamente: ${factura.numeroFactura}")
                        Log.d(TAG, "   Total: $${factura.total}, Items: ${factura.items.size}")
                        Result.success(factura)
                    } else {
                        val errorMsg = "Respuesta vacía del servidor"
                        Log.e(TAG, "❌ $errorMsg")
                        Result.failure(Exception(errorMsg))
                    }
                } else {
                    val errorMsg = when (response.code()) {
                        401 -> "No autenticado. Inicia sesión nuevamente"
                        400 -> "Carrito inválido o vacío"
                        409 -> "Stock insuficiente para completar la compra"
                        else -> "Error ${response.code()}: ${response.message()}"
                    }
                    Log.e(TAG, "❌ $errorMsg")
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Exception en checkout: ${e.message}", e)
                Result.failure(e)
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // MÉT ODO DE LECTURA: Listar facturas de un usuario
    // ═══════════════════════════════════════════════════════════════════

    /**
     * GET /api/facturas?userId={id}
     * Lista todas las facturas de un usuario
     *
     * @param userId ID del usuario
     * @return Result con lista de facturas
     */
    suspend fun listarFacturasPorUsuario(userId: String?): Result<List<Factura>> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "🔍 Buscando facturas para userId: $userId")
                val response = apiService.listarFacturas(userId)

                Log.d(TAG, "📡 Response code: ${response.code()}")

                if (response.isSuccessful) {
                    val facturas = response.body()?.facturas ?: emptyList()
                    val facturasDomain = FacturaMapper.toDomainList(facturas)
                    Log.d(TAG, "✅ Encontradas ${facturasDomain.size} facturas")
                    Result.success(facturasDomain)
                } else {
                    val errorMsg = "Error ${response.code()}: ${response.message()}"
                    Log.e(TAG, "❌ $errorMsg")
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Exception listando facturas: ${e.message}", e)
                Result.failure(e)
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // MÉT ODO DE LECTURA: Obtener detalle de una factura
    // ═══════════════════════════════════════════════════════════════════

    /**
     * GET /api/facturas/{id}
     * Obtiene el detalle completo de una factura específica
     *
     * @param facturaId ID de la factura
     * @return Result con la factura completa
     */
    suspend fun obtenerFacturaPorId(facturaId: String): Result<Factura> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "🔍 Obteniendo factura: $facturaId")
                val response = apiService.obtenerFactura(facturaId)

                Log.d(TAG, "📡 Response code: ${response.code()}")

                if (response.isSuccessful) {
                    val facturaDto = response.body()?.factura
                    if (facturaDto != null) {
                        val factura = FacturaMapper.toDomain(facturaDto)
                        Log.d(TAG, "✅ Factura obtenida: ${factura.numeroFactura}")
                        Result.success(factura)
                    } else {
                        val errorMsg = "Factura no encontrada"
                        Log.e(TAG, "❌ $errorMsg")
                        Result.failure(Exception(errorMsg))
                    }
                } else {
                    val errorMsg = if (response.code() == 404) {
                        "Factura no encontrada"
                    } else {
                        "Error ${response.code()}: ${response.message()}"
                    }
                    Log.e(TAG, "❌ $errorMsg")
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Exception obteniendo factura: ${e.message}", e)
                Result.failure(e)
            }
        }
    }
}

