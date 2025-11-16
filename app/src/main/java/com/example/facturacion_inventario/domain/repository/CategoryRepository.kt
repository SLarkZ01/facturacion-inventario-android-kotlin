package com.example.facturacion_inventario.domain.repository

import com.example.facturacion_inventario.domain.model.Category

/**
 * Interfaz del repositorio de categorías
 * Define los métodos que deben implementar los repositorios (local/remoto)
 */
interface CategoryRepository {
    /**
     * Obtiene todas las categorías
     */
    fun getCategories(): List<Category>

    /**
     * Obtiene una categoría por su ID
     */
    fun getCategoryById(id: String): Category?
}

