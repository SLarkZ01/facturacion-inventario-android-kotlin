package com.example.facturacion_inventario.ui.store

import androidx.lifecycle.ViewModel
import com.example.facturacion_inventario.domain.model.Product
import com.example.facturacion_inventario.domain.model.CartItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * CartViewModel optimizado con StateFlow para máximo rendimiento.
 * StateFlow es más eficiente que mutableStateOf para ViewModels
 * y permite recomposiciones granulares solo cuando cambian los datos.
 */
class CartViewModel : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _totalItemCount = MutableStateFlow(0)
    val totalItemCount: StateFlow<Int> = _totalItemCount.asStateFlow()

    private val _totalPrice = MutableStateFlow(0.0)
    val totalPrice: StateFlow<Double> = _totalPrice.asStateFlow()

    fun addToCart(product: Product, quantity: Int = 1) {
        _cartItems.update { currentItems ->
            val existingItem = currentItems.find { it.product.id == product.id }

            if (existingItem != null) {
                currentItems.map { item ->
                    if (item.product.id == product.id) {
                        item.copy(quantity = item.quantity + quantity)
                    } else {
                        item
                    }
                }
            } else {
                currentItems + CartItem(product, quantity)
            }
        }
        updateTotals()
    }

    fun removeFromCart(productId: String) {
        _cartItems.update { currentItems ->
            currentItems.filter { it.product.id != productId }
        }
        updateTotals()
    }

    fun updateQuantity(productId: String, quantity: Int) {
        if (quantity <= 0) {
            removeFromCart(productId)
        } else {
            _cartItems.update { currentItems ->
                currentItems.map { item ->
                    if (item.product.id == productId) {
                        item.copy(quantity = quantity)
                    } else {
                        item
                    }
                }
            }
            updateTotals()
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
        updateTotals()
    }

    private fun updateTotals() {
        val items = _cartItems.value
        _totalItemCount.value = items.sumOf { it.quantity }
        _totalPrice.value = items.sumOf { it.product.price * it.quantity }
    }

    // Métodos de compatibilidad para código existente
    fun getItemCount(): Int = _totalItemCount.value

    fun getTotalPrice(): Double = _totalPrice.value
}
