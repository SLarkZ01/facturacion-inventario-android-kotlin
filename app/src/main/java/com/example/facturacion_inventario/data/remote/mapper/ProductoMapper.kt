package com.example.facturacion_inventario.data.remote.mapper

import android.util.Log
import com.example.facturacion_inventario.data.remote.ApiConfig
import com.example.facturacion_inventario.data.remote.model.MedioDto
import com.example.facturacion_inventario.data.remote.model.ProductoDto
import com.example.facturacion_inventario.domain.model.MediaType
import com.example.facturacion_inventario.domain.model.Product
import com.example.facturacion_inventario.domain.model.ProductMedia
import com.example.facturacion_inventario.domain.model.ProductSpecification
import com.example.facturacion_inventario.R

/**
 * Mapper para convertir ProductoDto (de la API) a Product (modelo de dominio)
 */
object ProductoMapper {

    private const val TAG = "ProductoMapper"

    /**
     * Convierte ProductoDto a Product del dominio
     * Prioriza totalStock sobre stock si está disponible (API pública)
     */
    fun toDomain(dto: ProductoDto): Product {
        // Usar totalStock si está disponible (suma de todos los almacenes), sino usar stock
        val stockFinal = dto.totalStock ?: dto.stock

        Log.d(
            TAG,
            "Mapping producto='${dto.nombre}' id=${dto.id} stock=${dto.stock} totalStock=${dto.totalStock} -> stockFinal=$stockFinal"
        )

        return Product(
            id = dto.id ?: dto.idString ?: "",
            name = dto.nombre,
            description = dto.descripcion ?: "",
            imageRes = dto.imagenRecurso ?: R.drawable.ic_motorcycle_animated,
            categoryId = dto.categoriaId ?: "",
            price = dto.precio,
            stock = stockFinal,
            mediaList = dto.listaMedios?.map { it.toDomain() } ?: emptyList(),
            specifications = dto.specs?.map { ProductSpecification(it.key, it.value) } ?: emptyList(),
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
     * Prefiere secureUrl/publicId cuando existan (nueva API). Si no, usa campos legacy.
     */
    private fun MedioDto.toDomain(): ProductMedia {
        // Resolver URL preferida: secureUrl -> url -> construir desde publicId si es posible
        val resolvedUrl = when {
            !this.secureUrl.isNullOrBlank() -> this.secureUrl
            !this.url.isNullOrBlank() -> this.url
            !this.publicId.isNullOrBlank() -> "${ApiConfig.CLOUDINARY_BASE_URL}/${this.publicId}"
            else -> null
        }

        return ProductMedia(
            resourceId = this.idRecurso ?: 0,
            type = when (this.tipo?.uppercase()) {
                "VIDEO" -> MediaType.VIDEO
                else -> MediaType.IMAGE
            },
            url = resolvedUrl
        )
    }
}