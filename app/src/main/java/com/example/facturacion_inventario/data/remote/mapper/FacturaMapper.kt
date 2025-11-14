package com.example.facturacion_inventario.data.remote.mapper

import com.example.facturacion_inventario.data.remote.model.ClienteEmbebidoDto
import com.example.facturacion_inventario.data.remote.model.FacturaDto
import com.example.facturacion_inventario.data.remote.model.FacturaItemDto
import com.example.facturacion_inventario.domain.model.ClienteEmbebido
import com.example.facturacion_inventario.domain.model.Factura
import com.example.facturacion_inventario.domain.model.FacturaItem

/**
 * Mapper para convertir FacturaDto (de la API) a Factura (modelo de dominio)
 */
object FacturaMapper {

    /**
     * Convierte un FacturaDto a Factura (modelo de dominio)
     */
    fun toDomain(dto: FacturaDto): Factura {
        return Factura(
            id = dto.id,
            numeroFactura = dto.numeroFactura,
            cliente = dto.cliente?.let { toDomainCliente(it) },
            clienteId = dto.clienteId,
            items = dto.items.map { toDomainItem(it) },
            total = dto.total,
            realizadoPor = dto.realizadoPor,
            estado = dto.estado ?: "PENDIENTE", // Valor por defecto si viene null
            creadoEn = dto.creadoEn
        )
    }

    /**
     * Convierte una lista de FacturaDto a lista de Factura
     */
    fun toDomainList(dtos: List<FacturaDto>): List<Factura> {
        return dtos.map { toDomain(it) }
    }

    /**
     * Convierte un ClienteEmbebidoDto a ClienteEmbebido
     */
    private fun toDomainCliente(dto: ClienteEmbebidoDto): ClienteEmbebido {
        return ClienteEmbebido(
            nombre = dto.nombre,
            documento = dto.documento,
            direccion = dto.direccion
        )
    }

    /**
     * Convierte un FacturaItemDto a FacturaItem
     */
    private fun toDomainItem(dto: FacturaItemDto): FacturaItem {
        return FacturaItem(
            productoId = dto.productoId,
            cantidad = dto.cantidad,
            precioUnitario = dto.precioUnitario
        )
    }
}
