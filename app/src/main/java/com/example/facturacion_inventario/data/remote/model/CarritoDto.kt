package com.example.facturacion_inventario.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * DTO para el modelo Carrito del backend
 * Coincide con la estructura de CarritoResponse del backend Spring Boot
 */
data class CarritoDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("usuarioId")
    val usuarioId: String? = null,

    @SerializedName("items")
    val items: List<CarritoItemDto> = emptyList(),

    @SerializedName("realizadoPor")
    val realizadoPor: String? = null,

    @SerializedName("creadoEn")
    val creadoEn: String? = null
)

/**
 * DTO para CarritoItem del backend
 */
data class CarritoItemDto(
    @SerializedName("productoId")
    val productoId: String,

    @SerializedName("cantidad")
    val cantidad: Int
)

/**
 * Respuesta del endpoint GET /api/carritos
 */
data class CarritosResponse(
    @SerializedName("carritos")
    val carritos: List<CarritoDto> = emptyList()
)

/**
 * Respuesta del endpoint GET /api/carritos/{id}
 */
data class CarritoResponse(
    @SerializedName("carrito")
    val carrito: CarritoDto
)

/**
 * Request para crear un carrito - POST /api/carritos
 */
data class CarritoRequest(
    @SerializedName("usuarioId")
    val usuarioId: String? = null,

    @SerializedName("items")
    val items: List<CarritoItemRequest> = emptyList()
)

/**
 * Request para agregar/modificar items del carrito
 */
data class CarritoItemRequest(
    @SerializedName("productoId")
    val productoId: String,

    @SerializedName("cantidad")
    val cantidad: Int,

    @SerializedName("precioUnitario")
    val precioUnitario: Double? = null
)

/**
 * Request para merge de carrito anónimo
 */
data class MergeCarritoRequest(
    @SerializedName("anonCartId")
    val anonCartId: String? = null,

    @SerializedName("items")
    val items: List<CarritoItemRequest>? = null
)

/**
 * Respuesta genérica con mensaje
 */
data class CarritoMessageResponse(
    @SerializedName("mensaje")
    val mensaje: String? = null,

    @SerializedName("carrito")
    val carrito: CarritoDto? = null,

    @SerializedName("merged")
    val merged: Boolean? = null,

    @SerializedName("carritoId")
    val carritoId: String? = null,

    @SerializedName("totalItems")
    val totalItems: Int? = null
)
