package com.example.facturacion_inventario.data.repository

import android.content.Context
import android.util.Log
import com.example.data.auth.TokenStorage
import com.example.facturacion_inventario.data.remote.api.RetrofitClient
import com.example.facturacion_inventario.data.remote.mapper.FacturaMapper
import com.example.facturacion_inventario.data.remote.model.CheckoutRequest
import com.example.facturacion_inventario.data.remote.model.FacturaRequest
import com.example.facturacion_inventario.data.remote.model.FacturaItemRequest
import com.example.facturacion_inventario.data.remote.model.CarritoItemDto
import com.example.facturacion_inventario.data.remote.model.ClienteEmbebidoDto
import com.example.facturacion_inventario.domain.model.Factura
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio para consumir la API de facturas
 * Implementa las operaciones principales: checkout, listar y obtener detalle
 */
class RemoteFacturaRepository(private val context: Context) {

    private val apiService = RetrofitClient.facturaApiService
    private val TAG = "RemoteFacturaRepo"

    /**
     * Obtiene el snapshot del cliente desde el usuario autenticado
     * Los datos se obtienen de TokenStorage (guardados al login/register)
     */
    private fun getClienteFromAuth(): ClienteEmbebidoDto? {
        val userId = TokenStorage.getUserId(context)
        val username = TokenStorage.getUsername(context)
        val email = TokenStorage.getEmail(context)
        val nombre = TokenStorage.getNombre(context)
        val apellido = TokenStorage.getApellido(context)
        val fechaCreacion = TokenStorage.getFechaCreacion(context)

        // Verificar que al menos tengamos los campos obligatorios
        if (userId.isNullOrEmpty() || username.isNullOrEmpty() || email.isNullOrEmpty()) {
            Log.w(TAG, "⚠️ No se pudo obtener datos completos del usuario autenticado")
            return null
        }

        return ClienteEmbebidoDto(
            id = userId,
            username = username,
            email = email,
            nombre = nombre ?: username, // Fallback si no hay nombre
            apellido = apellido ?: "",
            fechaCreacion = fechaCreacion ?: java.time.Instant.now().toString()
        )
    }

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

    // ═══════════════════════════════════════════════════════════════════
    // MÉTODO NUEVO: Crear Borrador (Cotización sin descontar stock)
    // ═══════════════════════════════════════════════════════════════════

    /**
     * POST /api/facturas/borrador
     * Crea una factura en estado BORRADOR desde los items del carrito
     *
     * VENTAJAS:
     * - NO descuenta stock automáticamente
     * - Permite cotizaciones y negociaciones
     * - Se puede emitir posteriormente desde Next.js
     *
     * IMPORTANTE:
     * - El cliente se obtiene automáticamente del usuario autenticado
     * - Se envía como snapshot histórico según diseño del backend
     *
     * @param carritoItems Items del carrito a convertir en factura borrador
     * @return Result con la factura en estado BORRADOR
     */
    suspend fun crearBorrador(carritoItems: List<CarritoItemDto>): Result<Factura> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "📝 Creando factura BORRADOR con ${carritoItems.size} items")

                // Obtener datos del usuario autenticado para el snapshot
                val cliente = getClienteFromAuth()
                if (cliente != null) {
                    Log.d(TAG, "   Cliente: ${cliente.nombre} ${cliente.apellido} (${cliente.email})")
                } else {
                    Log.w(TAG, "   ⚠️ No se pudo obtener cliente del usuario autenticado")
                }

                // Convertir items del carrito a FacturaItemRequest
                val items = carritoItems.map { item ->
                    FacturaItemRequest(
                        productoId = item.productoId,
                        cantidad = item.cantidad
                    )
                }

                val request = FacturaRequest(
                    items = items,
                    cliente = cliente,  // Snapshot del usuario autenticado
                    estado = "BORRADOR"
                )

                val response = apiService.crearBorrador(request)

                Log.d(TAG, "📡 Response code: ${response.code()}")

                if (response.isSuccessful) {
                    val facturaDto = response.body()?.factura
                    if (facturaDto != null) {
                        val factura = FacturaMapper.toDomain(facturaDto)
                        Log.d(TAG, "✅ Borrador creado exitosamente: ${factura.numeroFactura}")
                        Log.d(TAG, "   Estado: ${factura.estado}, Total: $${factura.total}")
                        Log.d(TAG, "   Cliente: ${factura.cliente?.nombre ?: "Sin datos"}")
                        Result.success(factura)
                    } else {
                        val errorMsg = "Respuesta vacía del servidor"
                        Log.e(TAG, "❌ $errorMsg")
                        Result.failure(Exception(errorMsg))
                    }
                } else {
                    val errorMsg = when (response.code()) {
                        401 -> "No autenticado. Inicia sesión nuevamente"
                        400 -> "Datos inválidos para crear el borrador"
                        else -> "Error ${response.code()}: ${response.message()}"
                    }
                    Log.e(TAG, "❌ $errorMsg")
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Exception creando borrador: ${e.message}", e)
                Result.failure(e)
            }
        }
    }
}

