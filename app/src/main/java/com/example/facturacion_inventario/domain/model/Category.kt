package com.example.facturacion_inventario.domain.model

import androidx.annotation.DrawableRes
import com.example.facturacion_inventario.R

data class Category(
    val id: String,
    val name: String,
    val description: String,
    @DrawableRes val iconRes: Int = R.drawable.ic_motorcycle_animated,
    val subcategories: List<String> = emptyList()
)

