package com.example.facturacion_inventario.data.remote.model

import com.google.gson.annotations.SerializedName
import java.util.Date

/**
 * DTO que coincide exactamente con el modelo de Spring Boot MongoDB
 */
data class ProductoDto(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("idString")
    val idString: String? = null,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("descripcion")
    val descripcion: String? = null,

    @SerializedName("precio")
    val precio: Double,

    @SerializedName("stock")
    val stock: Int,

    @SerializedName("categoriaId")
    val categoriaId: String? = null,

    @SerializedName("imagenRecurso")
    val imagenRecurso: Int? = null,

    @SerializedName("listaMedios")
    val listaMedios: List<MedioDto>? = null,

    @SerializedName("creadoEn")
    val creadoEn: Date? = null
)

/**
 * DTO para los elementos de medios (imágenes/videos)
 */
data class MedioDto(
    @SerializedName("idRecurso")
    val idRecurso: Int,

    @SerializedName("tipo")
    val tipo: String // "IMAGE" o "VIDEO"
)

/**
 * Response wrapper para lista de productos
 */
data class ProductosResponse(
    @SerializedName("productos")
    val productos: List<ProductoDto>
)

/**
 * Response wrapper para un solo producto
 */
data class ProductoResponse(
    @SerializedName("producto")
    val producto: ProductoDto
)

/**
 * Request para crear/actualizar productos
 */
data class ProductoRequest(
    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("descripcion")
    val descripcion: String? = null,

    @SerializedName("precio")
    val precio: Double,

    @SerializedName("stock")
    val stock: Int,

    @SerializedName("categoriaId")
    val categoriaId: String? = null,

    @SerializedName("imagenRecurso")
    val imagenRecurso: Int? = null,

    @SerializedName("listaMedios")
    val listaMedios: List<MedioDto>? = null
)

/**
 * Response genérica con error
 */
data class ErrorResponse(
    @SerializedName("error")
    val error: String
)

