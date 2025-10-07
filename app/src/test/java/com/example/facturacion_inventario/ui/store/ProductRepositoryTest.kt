package com.example.facturacion_inventario.ui.store

import org.junit.Test
import org.junit.Assert.*

class ProductRepositoryTest {

    private val repo: ProductRepository = FakeProductRepository()

    @Test
    fun `getProducts returns non empty list`() {
        val products = repo.getProducts()
        assertNotNull(products)
        assertTrue(products.isNotEmpty())
    }

    @Test
    fun `getProductById returns correct product`() {
        val p = repo.getProductById("1")
        assertNotNull(p)
        assertEquals("1", p?.id)
        assertEquals("Filtro de aire", p?.name)
    }

    @Test
    fun `getProductById returns null for unknown`() {
        val p = repo.getProductById("unknown")
        assertNull(p)
    }
}
