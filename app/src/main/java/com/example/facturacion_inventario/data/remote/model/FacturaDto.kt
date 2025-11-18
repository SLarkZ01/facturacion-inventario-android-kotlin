package com.example.facturacion_inventario.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * DTO para el modelo Factura del backend
 * Coincide con la estructura de Factura.java del backend Spring Boot
 */
data class FacturaDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("numeroFactura")
    val numeroFactura: String,

    @SerializedName("cliente")
    val cliente: ClienteEmbebidoDto? = null,

    @SerializedName("clienteId")
    val clienteId: String? = null,

    @SerializedName("items")
    val items: List<FacturaItemDto> = emptyList(),

    @SerializedName("total")
    val total: Double,

    @SerializedName("realizadoPor")
    val realizadoPor: String? = null,

    @SerializedName("estado")
    val estado: String? = "PENDIENTE", // Nullable con valor por defecto

    @SerializedName("creadoEn")
    val creadoEn: String? = null
)

/**
 * DTO para ClienteEmbebido del backend
 * Snapshot del usuario para mantener coherencia histórica en facturas
 * NO incluye datos sensibles de autorización (roles, activo, etc.)
 */
data class ClienteEmbebidoDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("apellido")
    val apellido: String,

    @SerializedName("fechaCreacion")
    val fechaCreacion: String  // ISO 8601 format: "2024-10-01T12:34:56.789Z"
)

/**
 * DTO para FacturaItem del backend
 * Incluye todos los campos que el backend envía y que Next.js espera
 */
data class FacturaItemDto(
    @SerializedName("productoId")
    val productoId: String,

    @SerializedName("nombreProducto")
    val nombreProducto: String? = null,

    @SerializedName("codigoProducto")
    val codigoProducto: String? = null,

    @SerializedName("cantidad")
    val cantidad: Int,

    @SerializedName("precioUnitario")
    val precioUnitario: Double,

    @SerializedName("descuento")
    val descuento: Double = 0.0,

    @SerializedName("baseImponible")
    val baseImponible: Double? = null,

    @SerializedName("tasaIva")
    val tasaIva: Double = 19.0,

    @SerializedName("valorIva")
    val valorIva: Double? = null,

    @SerializedName("subtotal")
    val subtotal: Double? = null,

    @SerializedName("totalItem")
    val totalItem: Double? = null
)

/**
 * Request para crear factura desde carrito (POST /api/facturas/checkout)
 *
 * @param carritoId ID del carrito a convertir en factura
 * @param estado Estado de la factura: "BORRADOR" (cotización sin descontar stock) o "EMITIDA" (descuenta stock)
 *               Por defecto es "BORRADOR" para permitir cotizaciones
 */
data class CheckoutRequest(
    @SerializedName("carritoId")
    val carritoId: String,

    @SerializedName("estado")
    val estado: String = "BORRADOR"
)

/**
 * Request para crear factura en BORRADOR (POST /api/facturas/borrador)
 * Permite crear cotizaciones sin descontar stock
 */
data class FacturaRequest(
    @SerializedName("items")
    val items: List<FacturaItemRequest>,

    @SerializedName("numeroFactura")
    val numeroFactura: String? = null,

    @SerializedName("clienteId")
    val clienteId: String? = null,

    @SerializedName("cliente")
    val cliente: ClienteEmbebidoDto? = null,

    @SerializedName("total")
    val total: Double? = null,

    @SerializedName("realizadoPor")
    val realizadoPor: String? = null,

    @SerializedName("estado")
    val estado: String = "BORRADOR"
)

/**
 * Item de factura para el request de creación de borrador
 */
data class FacturaItemRequest(
    @SerializedName("productoId")
    val productoId: String,

    @SerializedName("cantidad")
    val cantidad: Int
)

/**
 * Respuesta del endpoint POST /api/facturas/checkout
 */
data class FacturaResponse(
    @SerializedName("factura")
    val factura: FacturaDto
)

/**
 * Respuesta del endpoint GET /api/facturas
 */
data class FacturasResponse(
    @SerializedName("facturas")
    val facturas: List<FacturaDto> = emptyList()
)
