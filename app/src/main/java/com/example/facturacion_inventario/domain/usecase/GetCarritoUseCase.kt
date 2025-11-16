package com.example.facturacion_inventario.domain.usecase

import com.example.facturacion_inventario.data.remote.model.CarritoItemRequest
import com.example.facturacion_inventario.data.repository.RemoteCarritoRepository
import com.example.facturacion_inventario.domain.model.CartItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Casos de uso para gestionar carritos desde la API
 * Encapsula la lógica de negocio del carrito y facilita el testing
 */
class CarritoUseCases(
    private val repository: RemoteCarritoRepository = RemoteCarritoRepository()
) {

    // ═══════════════════════════════════════════════════════════════════
    // CASOS DE USO DE LECTURA
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Obtiene los items de un carrito por su ID
     * @param carritoId ID del carrito
     * @return Result con lista de CartItems listos para mostrar en UI
     */
    suspend fun obtenerCarrito(carritoId: String): Result<List<CartItem>> {
        return withContext(Dispatchers.IO) {
            repository.getCarritoPorId(carritoId)
        }
    }

    /**
     * Obtiene todos los carritos de un usuario
     * @param usuarioId ID del usuario
     * @return Result con lista de IDs de carritos
     */
    suspend fun obtenerCarritosPorUsuario(usuarioId: String): Result<List<String>> {
        return withContext(Dispatchers.IO) {
            val result = repository.getCarritosPorUsuario(usuarioId)
            if (result.isSuccess) {
                val carritos = result.getOrNull() ?: emptyList()
                Result.success(carritos.map { it.id })
            } else {
                Result.failure(result.exceptionOrNull() ?: Exception("Error desconocido"))
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // CASOS DE USO DE CREACIÓN
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Crea un carrito nuevo
     * @param usuarioId ID del usuario (null para carrito anónimo)
     * @return Result con el ID del carrito creado
     */
    suspend fun crearCarrito(usuarioId: String? = null): Result<String> {
        return withContext(Dispatchers.IO) {
            val result = repository.crearCarrito(usuarioId)
            if (result.isSuccess) {
                val carrito = result.getOrNull()
                if (carrito != null) {
                    Result.success(carrito.id)
                } else {
                    Result.failure(Exception("No se pudo crear el carrito"))
                }
            } else {
                Result.failure(result.exceptionOrNull() ?: Exception("Error desconocido"))
            }
        }
    }

    /**
     * Agrega un producto al carrito
     * @param carritoId ID del carrito
     * @param productoId ID del producto
     * @param cantidad Cantidad a agregar
     * @param precioUnitario Precio unitario (opcional)
     * @return Result con los items actualizados del carrito
     */
    suspend fun agregarProducto(
        carritoId: String,
        productoId: String,
        cantidad: Int = 1,
        precioUnitario: Double? = null
    ): Result<List<CartItem>> {
        return withContext(Dispatchers.IO) {
            repository.agregarItem(carritoId, productoId, cantidad, precioUnitario)
        }
    }

    /**
     * Sincroniza un carrito anónimo con el usuario autenticado
     * Útil después del login para combinar carritos
     * @param anonCartId ID del carrito anónimo
     * @return Result con el ID del carrito del usuario
     */
    suspend fun sincronizarCarritoAnonimo(anonCartId: String): Result<String> {
        return withContext(Dispatchers.IO) {
            repository.mergeCarrito(anonCartId = anonCartId)
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // CASOS DE USO DE ELIMINACIÓN
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Remueve un producto del carrito
     * @param carritoId ID del carrito
     * @param productoId ID del producto a remover
     * @return Result con los items actualizados del carrito
     */
    suspend fun removerProducto(carritoId: String, productoId: String): Result<List<CartItem>> {
        return withContext(Dispatchers.IO) {
            repository.removerItem(carritoId, productoId)
        }
    }

    /**
     * Vacía el carrito (elimina todos los items)
     * @param carritoId ID del carrito
     * @return Result con lista vacía
     */
    suspend fun vaciarCarrito(carritoId: String): Result<List<CartItem>> {
        return withContext(Dispatchers.IO) {
            repository.vaciarCarrito(carritoId)
        }
    }

    /**
     * Elimina el carrito completamente
     * @param carritoId ID del carrito
     * @return Result indicando éxito
     */
    suspend fun eliminarCarrito(carritoId: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            repository.eliminarCarrito(carritoId)
        }
    }
}
