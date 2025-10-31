package com.example.facturacion_inventario.ui.store

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.facturacion_inventario.domain.model.Product
import com.example.facturacion_inventario.domain.model.CartItem

class CartViewModel : ViewModel() {
    var cartItems by mutableStateOf<List<CartItem>>(emptyList())
        private set

    val totalItemCount: Int
        get() = cartItems.sumOf { it.quantity }

    @Suppress("unused") // Función de utilidad para componentes
    fun getItemCount(): Int = totalItemCount

    fun addToCart(product: Product, quantity: Int = 1) {
        val existingItem = cartItems.find { it.product.id == product.id }

        cartItems = if (existingItem != null) {
            cartItems.map { item ->
                if (item.product.id == product.id) {
                    item.copy(quantity = item.quantity + quantity)
                } else {
                    item
                }
            }
        } else {
            cartItems + CartItem(product, quantity)
        }
    }

    fun removeFromCart(productId: String) {
        cartItems = cartItems.filter { it.product.id != productId }
    }

    @Suppress("unused") // Función útil para gestión de carrito
    fun updateQuantity(productId: String, quantity: Int) {
        if (quantity <= 0) {
            removeFromCart(productId)
        } else {
            cartItems = cartItems.map { item ->
                if (item.product.id == productId) {
                    item.copy(quantity = quantity)
                } else {
                    item
                }
            }
        }
    }

    @Suppress("unused") // Función útil para limpiar carrito
    fun clearCart() {
        cartItems = emptyList()
    }

    fun getTotalPrice(): Double {
        return cartItems.sumOf { it.product.price * it.quantity }
    }
}
