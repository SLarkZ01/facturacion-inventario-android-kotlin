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
 */
data class ClienteEmbebidoDto(
    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("documento")
    val documento: String? = null,

    @SerializedName("direccion")
    val direccion: String? = null
)

/**
 * DTO para FacturaItem del backend
 */
data class FacturaItemDto(
    @SerializedName("productoId")
    val productoId: String,

    @SerializedName("cantidad")
    val cantidad: Int,

    @SerializedName("precioUnitario")
    val precioUnitario: Double
)

/**
 * Request para crear factura desde carrito (POST /api/facturas/checkout)
 */
data class CheckoutRequest(
    @SerializedName("carritoId")
    val carritoId: String
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
