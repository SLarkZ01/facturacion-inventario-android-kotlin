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
 * ClienteEmbebido - Snapshot del usuario en la factura
 * Información histórica del cliente en el momento de crear la factura
 */
data class ClienteEmbebido(
    val id: String,
    val username: String,
    val email: String,
    val nombre: String,
    val apellido: String,
    val fechaCreacion: String
)

/**
 * FacturaItem - Item individual en una factura
 */
data class FacturaItem(
    val productoId: String,
    val nombreProducto: String? = null,
    val codigoProducto: String? = null,
    val cantidad: Int,
    val precioUnitario: Double,
    val descuento: Double = 0.0,
    val baseImponible: Double? = null,
    val tasaIva: Double = 19.0,
    val valorIva: Double? = null,
    val subtotal: Double? = null,
    val totalItem: Double? = null
) {
    /**
     * Calcula el subtotal de este item (si no viene del backend)
     */
    @Suppress("unused")
    fun getSubtotal(): Double = subtotal ?: (cantidad * precioUnitario)

    /**
     * Calcula el total del item incluyendo IVA (si no viene del backend)
     */
    @Suppress("unused")
    fun getTotalItem(): Double = totalItem ?: (getSubtotal() * (1 + tasaIva / 100))
}
