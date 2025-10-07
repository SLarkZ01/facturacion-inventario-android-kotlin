package com.example.facturacion_inventario.data.repository

import com.example.facturacion_inventario.domain.model.Product
import com.example.facturacion_inventario.domain.repository.ProductRepository

class FakeProductRepository : ProductRepository {
    private val items = listOf(
        Product(id = "0", name = "Cadena de transmisión", description = "Cadena para transmisión de motos"),
        Product(id = "1", name = "Filtro de aire", description = "Filtro para admisión de aire"),
        Product(id = "2", name = "Bujía", description = "Bujía de alta performance"),
        Product(id = "3", name = "Neumático trasero", description = "Neumático trasero 120/70")
    )

    override fun getProducts(): List<Product> = items

    override fun getProductById(id: String): Product? = items.find { it.id == id }
}

