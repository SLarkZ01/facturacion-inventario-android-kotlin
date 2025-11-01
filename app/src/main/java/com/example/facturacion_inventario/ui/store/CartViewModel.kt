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
 *
 * ANÁLISIS DE VISIBILIDAD:
 * - Todos los StateFlow son públicos para observación reactiva desde la UI
 * - Métodos de modificación (add/remove) son públicos según uso actual
 * - updateTotals() es privado (solo uso interno)
 * - Algunos métodos no tienen uso actual y están marcados para futura implementación
 */
class CartViewModel : ViewModel() {
    // ═══════════════════════════════════════════════════════════════
    // ESTADOS PÚBLICOS - Observables desde la UI
    // ═══════════════════════════════════════════════════════════════

    /** Lista de items en el carrito. Usado en: CartScreen */
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    /** Cantidad total de items (suma de cantidades). Usado en: StoreHost (badge), CartScreen */
    private val _totalItemCount = MutableStateFlow(0)
    val totalItemCount: StateFlow<Int> = _totalItemCount.asStateFlow()

    /** Precio total del carrito. Usado en: CartScreen */
    private val _totalPrice = MutableStateFlow(0.0)
    val totalPrice: StateFlow<Double> = _totalPrice.asStateFlow()

    // ═══════════════════════════════════════════════════════════════
    // MÉTODOS PÚBLICOS - Usados activamente en la UI
    // ═══════════════════════════════════════════════════════════════

    /**
     * Agrega un producto al carrito.
     * Si el producto ya existe, incrementa su cantidad.
     *
     * USADO EN: ProductDetailScreen (al hacer clic en "Agregar al carrito")
     *
     * @param product Producto a agregar
     * @param quantity Cantidad a agregar (por defecto 1)
     */
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

    /**
     * Elimina completamente un producto del carrito.
     *
     * USADO EN: CartScreen (botón de eliminar en cada item)
     *
     * @param productId ID del producto a eliminar
     */
    fun removeFromCart(productId: String) {
        _cartItems.update { currentItems ->
            currentItems.filter { it.product.id != productId }
        }
        updateTotals()
    }

    // ═══════════════════════════════════════════════════════════════
    // MÉTODOS PENDIENTES - Sin uso actual, disponibles para futuro
    // ═══════════════════════════════════════════════════════════════

    /**
     * Actualiza la cantidad de un producto en el carrito.
     * Si la cantidad es <= 0, elimina el producto.
     *
     * ⚠️ PENDIENTE DE USO: Este metodo está disponible pero no se usa actualmente.
     * PROPÓSITO FUTURO: Para implementar controles +/- directamente en CartScreen
     * o para sincronización con backend.
     *
     * @param productId ID del producto
     * @param quantity Nueva cantidad (si es <= 0, se elimina el item)
     */
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

    /**
     * Vacía completamente el carrito.
     *
     * ⚠️ PENDIENTE DE USO: Este metodo está disponible pero no se usa actualmente.
     * PROPÓSITO FUTURO: Para implementar botón "Vaciar carrito" en CartScreen
     * o después de completar una orden/factura.
     */
    fun clearCart() {
        _cartItems.value = emptyList()
        updateTotals()
    }

    // ═══════════════════════════════════════════════════════════════
    // MÉTODOS PRIVADOS - Solo para uso interno
    // ═══════════════════════════════════════════════════════════════

    /**
     * Recalcula los totales (cantidad y precio) basándose en los items actuales.
     * Se llama automáticamente después de cualquier modificación del carrito.
     *
     * PRIVADO: Solo se usa internamente después de add/remove/update.
     */
    private fun updateTotals() {
        val items = _cartItems.value
        _totalItemCount.value = items.sumOf { it.quantity }
        _totalPrice.value = items.sumOf { it.product.price * it.quantity }
    }

    // ═══════════════════════════════════════════════════════════════
    // MÉTODOS DEPRECADOS - Mantener por compatibilidad (opcional)
    // ═══════════════════════════════════════════════════════════════

    /**
     * @Deprecated("Usar directamente el StateFlow totalItemCount.collectAsState() en la UI")
     *
     * NOTA: Los StateFlow son más eficientes que estos getters.
     * NO SE USA actualmente. Se accede directamente al StateFlow.
     * Se mantiene por si hay código legacy que lo necesite.
     */
    @Deprecated(
        message = "Usar directamente totalItemCount StateFlow con collectAsState()",
        replaceWith = ReplaceWith("totalItemCount.collectAsState().value")
    )
    fun getItemCount(): Int = _totalItemCount.value

    /**
     * @Deprecated("Usar directamente el StateFlow totalPrice.collectAsState() en la UI")
     *
     * NOTA: Los StateFlow son más eficientes que estos getters.
     * NO SE USA actualmente. Se accede directamente al StateFlow.
     * Se mantiene por si hay código legacy que lo necesite.
     */
    @Deprecated(
        message = "Usar directamente totalPrice StateFlow con collectAsState()",
        replaceWith = ReplaceWith("totalPrice.collectAsState().value")
    )
    fun getTotalPrice(): Double = _totalPrice.value
}
