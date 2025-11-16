package com.example.facturacion_inventario.data.repository

import android.util.Log
import com.example.facturacion_inventario.data.remote.api.RetrofitClient
import com.example.facturacion_inventario.data.remote.mapper.CarritoMapper
import com.example.facturacion_inventario.data.remote.model.*
import com.example.facturacion_inventario.domain.model.CartItem
import com.example.facturacion_inventario.domain.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio para consumir la API de carritos
 * Implementa el CRUD completo de carritos
 */
class RemoteCarritoRepository {

    private val apiService = RetrofitClient.carritoApiService
    private val productoRepository = RemoteProductRepository()
    private val TAG = "RemoteCarritoRepo"

    // ═══════════════════════════════════════════════════════════════════
    // MÉTODOS DE LECTURA (GET)
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Obtiene los carritos de un usuario
     * @param usuarioId ID del usuario
     * @return Result con lista de carritos (cada carrito es una lista de CartItems)
     */
    suspend fun getCarritosPorUsuario(usuarioId: String?): Result<List<CarritoDto>> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "🔍 Fetching carritos for usuarioId: $usuarioId")
                val response = apiService.listarCarritos(usuarioId)

                Log.d(TAG, "📡 Response code: ${response.code()}")

                if (response.isSuccessful) {
                    val carritos = response.body()?.carritos ?: emptyList()
                    Log.d(TAG, "✅ Successfully fetched ${carritos.size} carritos")
                    Result.success(carritos)
                } else {
                    val errorMsg = "Error ${response.code()}: ${response.message()}"
                    Log.e(TAG, "❌ $errorMsg")
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Exception fetching carritos: ${e.message}", e)
                Result.failure(e)
            }
        }
    }

    /**
     * Obtiene un carrito por ID y lo convierte a CartItems
     * @param carritoId ID del carrito
     * @return Result con lista de CartItems listos para mostrar en UI
     */
    suspend fun getCarritoPorId(carritoId: String): Result<List<CartItem>> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "🔍 Fetching carrito with ID: $carritoId")
                val response = apiService.obtenerCarrito(carritoId)

                if (response.isSuccessful) {
                    val carritoDto = response.body()?.carrito
                    if (carritoDto != null) {
                        Log.d(TAG, "✅ Successfully fetched carrito with ${carritoDto.items.size} items")

                        // Obtener todos los productos necesarios
                        val productosResult = productoRepository.getProductsAsync()
                        if (productosResult.isSuccess) {
                            val productos = productosResult.getOrNull() ?: emptyList()
                            val productosMap = productos.associateBy { it.id }

                            val cartItems = CarritoMapper.toDomainCartItems(carritoDto, productosMap)
                            Log.d(TAG, "✅ Mapped to ${cartItems.size} CartItems")
                            Result.success(cartItems)
                        } else {
                            Result.failure(Exception("No se pudieron cargar los productos del carrito"))
                        }
                    } else {
                        Result.failure(Exception("Carrito no encontrado"))
                    }
                } else {
                    val errorMsg = if (response.code() == 404) {
                        "Carrito no encontrado"
                    } else {
                        "Error ${response.code()}: ${response.message()}"
                    }
                    Log.e(TAG, "❌ $errorMsg")
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Exception fetching carrito: ${e.message}", e)
                Result.failure(e)
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // MÉTODOS DE CREACIÓN (POST)
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Crea un carrito nuevo
     * @param usuarioId ID del usuario (opcional, null para carrito anónimo)
     * @param items Items iniciales (opcional)
     * @return Result con el CarritoDto creado
     */
    suspend fun crearCarrito(
        usuarioId: String? = null,
        items: List<CarritoItemRequest> = emptyList()
    ): Result<CarritoDto> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "🛒 Creating carrito for user: $usuarioId with ${items.size} items")
                val request = CarritoRequest(usuarioId = usuarioId, items = items)
                val response = apiService.crearCarrito(request)

                if (response.isSuccessful) {
                    val carrito = response.body()?.carrito
                    if (carrito != null) {
                        Log.d(TAG, "✅ Carrito created with ID: ${carrito.id}")
                        Result.success(carrito)
                    } else {
                        Result.failure(Exception("No se recibió el carrito creado"))
                    }
                } else {
                    val errorMsg = "Error ${response.code()}: ${response.message()}"
                    Log.e(TAG, "❌ $errorMsg")
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Exception creating carrito: ${e.message}", e)
                Result.failure(e)
            }
        }
    }

    /**
     * Agrega un item al carrito
     * @param carritoId ID del carrito
     * @param productoId ID del producto
     * @param cantidad Cantidad a agregar
     * @param precioUnitario Precio unitario (opcional)
     * @return Result con el carrito actualizado
     */
    suspend fun agregarItem(
        carritoId: String,
        productoId: String,
        cantidad: Int,
        precioUnitario: Double? = null
    ): Result<List<CartItem>> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "➕ Adding item $productoId (qty: $cantidad) to carrito $carritoId")
                val item = CarritoItemRequest(
                    productoId = productoId,
                    cantidad = cantidad,
                    precioUnitario = precioUnitario
                )
                val response = apiService.agregarItem(carritoId, item)

                if (response.isSuccessful) {
                    val carritoDto = response.body()?.carrito
                    if (carritoDto != null) {
                        Log.d(TAG, "✅ Item added successfully")
                        // Convertir a CartItems
                        convertirCarritoDtoACartItems(carritoDto)
                    } else {
                        Result.failure(Exception("No se recibió el carrito actualizado"))
                    }
                } else {
                    val errorMsg = "Error ${response.code()}: ${response.message()}"
                    Log.e(TAG, "❌ $errorMsg")
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Exception adding item: ${e.message}", e)
                Result.failure(e)
            }
        }
    }

    /**
     * Sincroniza un carrito anónimo con el usuario autenticado
     * @param anonCartId ID del carrito anónimo
     * @param items Items adicionales a sincronizar
     * @return Result con información del merge
     */
    suspend fun mergeCarrito(
        anonCartId: String? = null,
        items: List<CarritoItemRequest>? = null
    ): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "🔄 Merging carrito - anonCartId: $anonCartId")
                val request = MergeCarritoRequest(anonCartId = anonCartId, items = items)
                val response = apiService.mergeCarrito(request)

                if (response.isSuccessful) {
                    val body = response.body()
                    val carritoId = body?.carritoId
                    if (carritoId != null) {
                        Log.d(TAG, "✅ Merge successful - carritoId: $carritoId, totalItems: ${body.totalItems}")
                        Result.success(carritoId)
                    } else {
                        Result.failure(Exception("No se recibió el ID del carrito"))
                    }
                } else {
                    val errorMsg = "Error ${response.code()}: ${response.message()}"
                    Log.e(TAG, "❌ $errorMsg")
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Exception merging carrito: ${e.message}", e)
                Result.failure(e)
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // MÉTODOS DE ELIMINACIÓN (DELETE)
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Remueve un item del carrito
     * @param carritoId ID del carrito
     * @param productoId ID del producto a remover
     * @return Result con el carrito actualizado
     */
    suspend fun removerItem(carritoId: String, productoId: String): Result<List<CartItem>> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "➖ Removing item $productoId from carrito $carritoId")
                val response = apiService.removerItem(carritoId, productoId)

                if (response.isSuccessful) {
                    val carritoDto = response.body()?.carrito
                    if (carritoDto != null) {
                        Log.d(TAG, "✅ Item removed successfully")
                        convertirCarritoDtoACartItems(carritoDto)
                    } else {
                        Result.failure(Exception("No se recibió el carrito actualizado"))
                    }
                } else {
                    val errorMsg = "Error ${response.code()}: ${response.message()}"
                    Log.e(TAG, "❌ $errorMsg")
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Exception removing item: ${e.message}", e)
                Result.failure(e)
            }
        }
    }

    /**
     * Vacía el carrito (elimina todos los items)
     * @param carritoId ID del carrito
     * @return Result con el carrito vacío
     */
    suspend fun vaciarCarrito(carritoId: String): Result<List<CartItem>> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "🗑️ Clearing carrito $carritoId")
                val response = apiService.vaciarCarrito(carritoId)

                if (response.isSuccessful) {
                    Log.d(TAG, "✅ Carrito cleared successfully")
                    Result.success(emptyList())
                } else {
                    val errorMsg = "Error ${response.code()}: ${response.message()}"
                    Log.e(TAG, "❌ $errorMsg")
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Exception clearing carrito: ${e.message}", e)
                Result.failure(e)
            }
        }
    }

    /**
     * Elimina un carrito completamente
     * @param carritoId ID del carrito
     * @return Result indicando éxito o error
     */
    suspend fun eliminarCarrito(carritoId: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "🗑️ Deleting carrito $carritoId")
                val response = apiService.eliminarCarrito(carritoId)

                if (response.isSuccessful) {
                    Log.d(TAG, "✅ Carrito deleted successfully")
                    Result.success(true)
                } else {
                    val errorMsg = "Error ${response.code()}: ${response.message()}"
                    Log.e(TAG, "❌ $errorMsg")
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Exception deleting carrito: ${e.message}", e)
                Result.failure(e)
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // MÉTODOS AUXILIARES
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Convierte un CarritoDto a CartItems obteniendo los productos
     */
    private suspend fun convertirCarritoDtoACartItems(carritoDto: CarritoDto): Result<List<CartItem>> {
        val productosResult = productoRepository.getProductsAsync()
        return if (productosResult.isSuccess) {
            val productos = productosResult.getOrNull() ?: emptyList()
            val productosMap = productos.associateBy { it.id }
            val cartItems = CarritoMapper.toDomainCartItems(carritoDto, productosMap)
            Result.success(cartItems)
        } else {
            Result.failure(Exception("No se pudieron cargar los productos"))
        }
    }

    /**
     * Convierte un CarritoDto a CartItems usando productos ya cargados
     * Útil cuando ya tienes los productos en memoria
     */
    fun convertirCarritoACartItems(
        carritoDto: CarritoDto,
        productos: List<Product>
    ): List<CartItem> {
        val productosMap = productos.associateBy { it.id }
        return CarritoMapper.toDomainCartItems(carritoDto, productosMap)
    }
}
