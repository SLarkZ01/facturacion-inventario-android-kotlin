package com.example.facturacion_inventario.data.remote.mapper

import com.example.facturacion_inventario.data.remote.model.MedioDto
import com.example.facturacion_inventario.data.remote.model.ProductoDto
import com.example.facturacion_inventario.domain.model.MediaType
import com.example.facturacion_inventario.domain.model.Product
import com.example.facturacion_inventario.domain.model.ProductMedia
import com.example.facturacion_inventario.R

/**
 * Mapper para convertir ProductoDto (de la API) a Product (modelo de dominio)
 */
object ProductoMapper {

    /**
     * Convierte ProductoDto a Product del dominio
     */
    fun toDomain(dto: ProductoDto): Product {
        return Product(
            id = dto.id ?: dto.idString ?: "",
            name = dto.nombre,
            description = dto.descripcion ?: "",
            imageRes = dto.imagenRecurso ?: R.drawable.ic_motorcycle_animated,
            categoryId = dto.categoriaId ?: "",
            price = dto.precio,
            stock = dto.stock,
            mediaList = dto.listaMedios?.map { it.toDomain() } ?: emptyList(),
            specifications = emptyList(), // Puede expandirse en el futuro
            features = emptyList() // Puede expandirse en el futuro
        )
    }

    /**
     * Convierte una lista de ProductoDto a lista de Product
     */
    fun toDomainList(dtoList: List<ProductoDto>): List<Product> {
        return dtoList.map { toDomain(it) }
    }

    /**
     * Extensión para convertir MedioDto a ProductMedia
     */
    private fun MedioDto.toDomain(): ProductMedia {
        return ProductMedia(
            resourceId = this.idRecurso,
            type = when (this.tipo.uppercase()) {
                "VIDEO" -> MediaType.VIDEO
                else -> MediaType.IMAGE
            }
        )
    }
}

