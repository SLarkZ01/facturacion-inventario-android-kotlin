package com.example.facturacion_inventario.domain.repository

import com.example.facturacion_inventario.domain.model.Product

interface ProductRepository {
    fun getProducts(): List<Product>
    fun getProductById(id: String): Product?
}

