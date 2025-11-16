package com.example.facturacion_inventario.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * DTO para información de stock por almacén
 */
data class StockByAlmacenDto(
    @SerializedName("almacenId")
    val almacenId: String,

    @SerializedName("almacenNombre")
    val almacenNombre: String,

    @SerializedName("cantidad")
    val cantidad: Int
)

/**
 * DTO para respuesta de consulta de stock GET /api/stock?productoId={id}
 */
data class StockResponseDto(
    @SerializedName("stockByAlmacen")
    val stockByAlmacen: List<StockByAlmacenDto>,

    @SerializedName("total")
    val total: Int
)

/**
 * DTO para detalles de stock individual
 */
@Suppress("unused")
data class StockDto(
    @SerializedName("productoId")
    val productoId: String,

    @SerializedName("almacenId")
    val almacenId: String,

    @SerializedName("cantidad")
    val cantidad: Int,

    @SerializedName("actualizadoEn")
    val actualizadoEn: String? = null
)

/**
 * Request para ajustar stock POST /api/stock/adjust
 */
data class AdjustStockRequest(
    @SerializedName("productoId")
    val productoId: String,

    @SerializedName("almacenId")
    val almacenId: String,

    @SerializedName("delta")
    val delta: Int // Positivo para incrementar, negativo para decrementar
)

/**
 * Request para establecer stock POST /api/stock/set
 */
data class SetStockRequest(
    @SerializedName("productoId")
    val productoId: String,

    @SerializedName("almacenId")
    val almacenId: String,

    @SerializedName("cantidad")
    val cantidad: Int // Cantidad absoluta a establecer
)

/**
 * Response para operaciones de modificación de stock
 */
data class StockOperationResponse(
    @SerializedName("stock")
    val stock: StockDto,

    @SerializedName("total")
    val total: Int
)

/**
 * DTO para errores de stock
 */
@Suppress("unused")
data class StockErrorResponse(
    @SerializedName("error")
    val error: String
)
