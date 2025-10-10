package com.example.facturacion_inventario.ui.store

object Routes {
    const val HOME = "home"
    const val HOME_WITH_CATEGORY = "home/{categoryId}"
    const val CART = "cart"
    const val CATEGORIES = "categories"
    const val SEARCH = "search"
    private const val PRODUCT_BASE = "product"
    const val PRODUCT = "$PRODUCT_BASE/{id}"

    fun homeWithCategory(categoryId: String) = "home/$categoryId"
    fun productRoute(id: String) = "$PRODUCT_BASE/$id"
}
