package com.example.facturacion_inventario.domain.model

import androidx.annotation.DrawableRes
import com.example.facturacion_inventario.R

// Modelo Product en la capa domain
data class Product(
    val id: String,
    val name: String,
    val description: String,
    @DrawableRes val imageRes: Int = R.drawable.ic_motorcycle_animated
)

