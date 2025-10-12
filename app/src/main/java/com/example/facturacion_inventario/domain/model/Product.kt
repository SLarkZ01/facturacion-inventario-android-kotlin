package com.example.facturacion_inventario.domain.model

import androidx.annotation.DrawableRes
import com.example.facturacion_inventario.R

// Enum para tipos de media
enum class MediaType {
    IMAGE,
    VIDEO
}

// Modelo para representar un medio (imagen o video)
data class ProductMedia(
    @DrawableRes val resourceId: Int,
    val type: MediaType = MediaType.IMAGE,
    val url: String? = null // Para uso futuro con URLs remotas
)

// Modelo para especificaciones técnicas del producto
data class ProductSpecification(
    val key: String,
    val value: String
)

// Modelo Product en la capa domain
data class Product(
    val id: String,
    val name: String,
    val description: String,
    @DrawableRes val imageRes: Int = R.drawable.ic_motorcycle_animated,
    val categoryId: String = "",
    val price: Double = 0.0,
    val stock: Int = 0,
    val mediaList: List<ProductMedia> = emptyList(), // Lista de medios del producto
    val specifications: List<ProductSpecification> = emptyList(), // Especificaciones técnicas
    val features: List<String> = emptyList() // Características destacadas
)
