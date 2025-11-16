package com.example.facturacion_inventario.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * DTO que coincide exactamente con el modelo de Spring Boot MongoDB
 * Incluye campos adicionales de la API pública (totalStock, stockByAlmacen, thumbnailUrl, specs)
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
    val creadoEn: String? = null,  // Cambiado de Date a String

    // Campos adicionales de la API pública
    @SerializedName("totalStock")
    val totalStock: Int? = null,

    @SerializedName("stockByAlmacen")
    val stockByAlmacen: List<StockByAlmacenDto>? = null,

    @SerializedName("thumbnailUrl")
    val thumbnailUrl: String? = null,

    @SerializedName("specs")
    val specs: Map<String, String>? = null,

    @SerializedName("ownerId")
    val ownerId: String? = null
)

/**
 * DTO para los elementos de medios (imágenes/videos)
 * Soporta ambos formatos:
 * - Legacy: { idRecurso, tipo, url, order }
 * - Nuevo: { publicId, secure_url, format, type, order }
 */
data class MedioDto(
    @SerializedName("idRecurso")
    val idRecurso: Int? = null,

    // Nombre enviado por backend en la nueva versión
    @SerializedName("publicId")
    val publicId: String? = null,

    @SerializedName("type")
    val tipo: String? = null, // "IMAGE" o "VIDEO"

    // Legacy field
    @SerializedName("url")
    val url: String? = null,

    // New secure url field from Cloudinary examples
    @SerializedName("secure_url")
    val secureUrl: String? = null,

    @SerializedName("format")
    val format: String? = null,

    @SerializedName("order")
    val order: Int = 0
)

/**
 * Response wrapper para lista de productos
 * Ahora incluye paginación
 */
data class ProductosResponse(
    @SerializedName("productos")
    val productos: List<ProductoDto>,

    @SerializedName("total")
    val total: Long? = null,

    @SerializedName("page")
    val page: Int? = null,

    @SerializedName("size")
    val size: Int? = null
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