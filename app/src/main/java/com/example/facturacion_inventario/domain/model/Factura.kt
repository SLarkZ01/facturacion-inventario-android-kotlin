package com.example.facturacion_inventario.domain.model

/**
 * Factura - Modelo de dominio para facturas
 * Representa una factura generada desde un carrito
 */
data class Factura(
    val id: String,
    val numeroFactura: String,
    val cliente: ClienteEmbebido? = null,
    val clienteId: String? = null,
    val items: List<FacturaItem> = emptyList(),
    val total: Double,
    val realizadoPor: String? = null,
    val estado: String? = "PENDIENTE", // Nullable con valor por defecto
    val creadoEn: String? = null
) {
    /**
     * Calcula el total de items en la factura
     */
    @Suppress("unused")
    fun getTotalItems(): Int = items.sumOf { it.cantidad }

    /**
     * Verifica si la factura está pagada
     */
    @Suppress("unused")
    fun isPagada(): Boolean = estado?.equals("PAGADA", ignoreCase = true) ?: false
}

/**
 * ClienteEmbebido - Información del cliente en la factura
 */
data class ClienteEmbebido(
    val nombre: String,
    val documento: String? = null,
    val direccion: String? = null
)

/**
 * FacturaItem - Item individual en una factura
 */
data class FacturaItem(
    val productoId: String,
    val cantidad: Int,
    val precioUnitario: Double
) {
    /**
     * Calcula el subtotal de este item
     */
    @Suppress("unused")
    fun getSubtotal(): Double = cantidad * precioUnitario
}
