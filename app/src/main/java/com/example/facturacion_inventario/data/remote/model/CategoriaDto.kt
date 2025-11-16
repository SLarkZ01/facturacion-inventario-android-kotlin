package com.example.facturacion_inventario.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * DTO que representa una categoría desde la API de Spring Boot
 * Corresponde al modelo Categoria del backend MongoDB
 */
data class CategoriaDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("idString")
    val idString: String? = null,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("descripcion")
    val descripcion: String? = null,

    @SerializedName("iconoRecurso")
    val iconoRecurso: Int? = null,

    @SerializedName(value = "imageUrl", alternate = ["imagenUrl"])
    val imagenUrl: String? = null,

    @SerializedName("tallerId")
    val tallerId: String? = null,

    @SerializedName("mappedGlobalCategoryId")
    val mappedGlobalCategoryId: String? = null,

    @SerializedName("creadoEn")
    val creadoEn: String? = null, // Cambiado de Date a String

    @SerializedName("listaMedios")
    val listaMedios: List<MedioDto>? = null
)

/**
 * Respuesta paginada de categorías desde la API
 * Formato: {"categorias":[], "size":20, "total":0, "page":0}
 */
data class CategoriasResponse(
    @SerializedName("categorias")
    val categorias: List<CategoriaDto>,

    @SerializedName("total")
    val total: Long? = 0,

    @SerializedName("size")
    val size: Int? = 20,

    @SerializedName("page")
    val page: Int? = 0
)

/**
 * Respuesta para una categoría individual
 */
data class CategoriaResponse(
    @SerializedName("categoria")
    val categoria: CategoriaDto
)