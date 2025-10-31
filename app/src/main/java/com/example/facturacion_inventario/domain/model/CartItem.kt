package com.example.facturacion_inventario.domain.model

/**
 * CartItem - Representa un item en el carrito de compras
 * Combina un producto con la cantidad seleccionada
 */
data class CartItem(
    val product: Product,
    val quantity: Int
) {
    /**
     * Calcula el subtotal para este item (precio * cantidad)
     */
    @Suppress("unused") // Función útil para futuros cálculos
    fun getSubtotal(): Double = product.price * quantity
}
