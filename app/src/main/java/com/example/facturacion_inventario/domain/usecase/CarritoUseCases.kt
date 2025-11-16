package com.example.facturacion_inventario.domain.usecase

import com.example.facturacion_inventario.data.repository.RemoteCarritoRepository
import com.example.facturacion_inventario.domain.model.CartItem
import com.example.facturacion_inventario.data.remote.model.CarritoDto

/**
 * Casos de uso para operaciones del carrito
 */
class CarritoUseCases(
    private val repository: RemoteCarritoRepository = RemoteCarritoRepository()
) {
    /**
     * Obtiene los carritos de un usuario
     */
    suspend fun obtenerCarritosPorUsuario(usuarioId: String?): Result<List<CarritoDto>> {
        return repository.getCarritosPorUsuario(usuarioId)
    }

    /**
     * Obtiene un carrito por ID
     */
    suspend fun obtenerCarrito(carritoId: String): Result<List<CartItem>> {
        return repository.getCarritoPorId(carritoId)
    }

    /**
     * Crea un carrito nuevo
     */
    suspend fun crearCarrito(usuarioId: String?): Result<CarritoDto> {
        return repository.crearCarrito(usuarioId = usuarioId)
    }

    /**
     * Agrega un producto al carrito
     */
    suspend fun agregarProducto(
        carritoId: String,
        productoId: String,
        cantidad: Int
    ): Result<List<CartItem>> {
        return repository.agregarItem(carritoId, productoId, cantidad)
    }

    /**
     * Elimina un producto del carrito
     */
    suspend fun eliminarProducto(
        carritoId: String,
        productoId: String
    ): Result<List<CartItem>> {
        return repository.removerItem(carritoId, productoId)
    }

    /**
     * Vacía el carrito (elimina todos los items)
     */
    suspend fun vaciarCarrito(carritoId: String): Result<List<CartItem>> {
        return repository.vaciarCarrito(carritoId)
    }

    /**
     * Elimina el carrito completamente
     */
    suspend fun eliminarCarrito(carritoId: String): Result<Boolean> {
        return repository.eliminarCarrito(carritoId)
    }

    /**
     * Sincroniza un carrito anónimo con el usuario autenticado
     */
    suspend fun sincronizarCarritoAnonimo(anonCartId: String): Result<String> {
        return repository.mergeCarrito(anonCartId = anonCartId)
    }
}
