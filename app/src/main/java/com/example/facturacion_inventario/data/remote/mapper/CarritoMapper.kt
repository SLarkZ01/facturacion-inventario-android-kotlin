package com.example.facturacion_inventario.data.remote.mapper

import com.example.facturacion_inventario.data.remote.model.CarritoDto
import com.example.facturacion_inventario.data.remote.model.CarritoItemDto
import com.example.facturacion_inventario.domain.model.CartItem
import com.example.facturacion_inventario.domain.model.Product

/**
 * Mapper para convertir DTOs de Carrito del backend a modelos de dominio
 */
object CarritoMapper {

    /**
     * Convierte un CarritoDto a una lista de CartItems del dominio
     * Requiere un mapa de productos para resolver los IDs
     *
     * @param carritoDto DTO del carrito del backend
     * @param productosMap Mapa de ID de producto a Product del dominio
     * @return Lista de CartItem para usar en la UI
     */
    fun toDomainCartItems(
        carritoDto: CarritoDto,
        productosMap: Map<String, Product>
    ): List<CartItem> {
        return carritoDto.items.mapNotNull { itemDto ->
            val product = productosMap[itemDto.productoId]
            if (product != null) {
                CartItem(
                    product = product,
                    quantity = itemDto.cantidad
                )
            } else {
                // Si no se encuentra el producto, se omite el item
                null
            }
        }
    }

    /**
     * Convierte una lista de CarritoItemDto a CartItems del dominio
     *
     * @param items Lista de items del carrito
     * @param productosMap Mapa de ID de producto a Product del dominio
     * @return Lista de CartItem
     */
    fun itemsToDomain(
        items: List<CarritoItemDto>,
        productosMap: Map<String, Product>
    ): List<CartItem> {
        return items.mapNotNull { itemDto ->
            val product = productosMap[itemDto.productoId]
            if (product != null) {
                CartItem(
                    product = product,
                    quantity = itemDto.cantidad
                )
            } else {
                null
            }
        }
    }
}

