package com.example.facturacion_inventario.data.remote.mapper

import android.util.Log
import com.example.facturacion_inventario.data.remote.ApiConfig
import com.example.facturacion_inventario.data.remote.model.CategoriaDto
import com.example.facturacion_inventario.domain.model.Category
import com.example.facturacion_inventario.R

/**
 * Mapper para convertir CategoriaDto (de la API) a Category (modelo de dominio)
 */
object CategoriaMapper {

    private const val TAG = "CategoriaMapper"

    /**
     * Mapeo de iconos por defecto basados en el nombre de la categoría
     * Se usa cuando iconoRecurso es null o no está disponible
     */
    private val defaultIcons = mapOf(
        "motor" to R.drawable.ic_engine,
        "transmision" to R.drawable.ic_gear,
        "transmisión" to R.drawable.ic_gear,
        "frenos" to R.drawable.ic_brake,
        "suspension" to R.drawable.ic_suspension,
        "suspensión" to R.drawable.ic_suspension,
        "electrico" to R.drawable.ic_electric,
        "eléctrico" to R.drawable.ic_electric,
        "carroceria" to R.drawable.ic_body,
        "carrocería" to R.drawable.ic_body,
        "ruedas" to R.drawable.ic_tire,
        "neumáticos" to R.drawable.ic_tire,
        "accesorios" to R.drawable.ic_accessories,
        "lubricantes" to R.drawable.ic_oil,
        "herramientas" to R.drawable.ic_tools
    )

    /**
     * Convierte un CategoriaDto a Category (modelo de dominio)
     */
    fun toDomain(dto: CategoriaDto): Category {
        val resolved = resolveImageUrl(dto)
        Log.d(TAG, "Mapping category='${dto.nombre}' id=${dto.id} -> resolvedImageUrl=$resolved imageUrlField=${dto.imagenUrl}")

        return Category(
            id = dto.id,
            name = dto.nombre,
            description = dto.descripcion ?: "",
            iconRes = getIconRes(dto),
            imageUrl = resolved,
            tallerId = dto.tallerId // ← AGREGAR: Mapear el tallerId desde el DTO
        )
    }

    /**
     * Convierte una lista de CategoriaDto a lista de Category
     */
    fun toDomainList(dtos: List<CategoriaDto>): List<Category> {
        return dtos.map { toDomain(it) }
    }

    /**
     * Resuelve la URL de imagen preferida desde el DTO de categoría.
     * Prioridad: 1) imagenUrl (campo directo), 2) secure_url del primer medio,
     * 3) url del primer medio, 4) construir desde publicId del primer medio
     */
    private fun resolveImageUrl(dto: CategoriaDto): String? {
        // Si el backend envía imagenUrl directo, usarlo
        dto.imagenUrl?.let { if (it.isNotBlank()) return it }

        val firstMedio = dto.listaMedios?.firstOrNull() ?: return null

        // Preferir secure_url -> url -> construir desde publicId
        firstMedio.secureUrl?.let { if (it.isNotBlank()) return it }
        firstMedio.url?.let { if (it.isNotBlank()) return it }
        firstMedio.publicId?.let { if (it.isNotBlank()) return "${ApiConfig.CLOUDINARY_BASE_URL}/${it}" }

        return null
    }

    /**
     * Obtiene el recurso de icono apropiado
     * Prioridad: 1) iconoRecurso del backend, 2) mapeo por nombre, 3) icono de Ermotos por defecto
     */
    private fun getIconRes(dto: CategoriaDto): Int {
        // Si el backend envía un iconoRecurso, usarlo
        dto.iconoRecurso?.let { return it }

        // Buscar por nombre (case-insensitive)
        val nameLower = dto.nombre.lowercase().trim()
        defaultIcons.forEach { (key, icon) ->
            if (nameLower.contains(key)) {
                return icon
            }
        }

        // Icono de Ermotos por defecto
        return R.drawable.ermotoshd
    }
}
