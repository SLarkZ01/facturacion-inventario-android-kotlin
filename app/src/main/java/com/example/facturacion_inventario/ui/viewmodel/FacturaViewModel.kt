package com.example.facturacion_inventario.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.facturacion_inventario.data.repository.RemoteFacturaRepository
import com.example.facturacion_inventario.domain.model.Factura
import com.example.facturacion_inventario.data.remote.model.CarritoItemDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para manejar el estado de las facturas
 * Gestiona el checkout, listado y detalle de facturas
 * Ahora obtiene automáticamente los datos del cliente del usuario autenticado
 */
class FacturaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RemoteFacturaRepository(application.applicationContext)

    // ═══════════════════════════════════════════════════════════════════
    // ESTADO PARA CHECKOUT
    // ═══════════════════════════════════════════════════════════════════

    private val _checkoutState = MutableStateFlow<CheckoutState>(CheckoutState.Idle)
    val checkoutState: StateFlow<CheckoutState> = _checkoutState.asStateFlow()

    // ═══════════════════════════════════════════════════════════════════
    // ESTADO PARA LISTADO DE FACTURAS
    // ═══════════════════════════════════════════════════════════════════

    private val _facturasState = MutableStateFlow<FacturasState>(FacturasState.Loading)
    val facturasState: StateFlow<FacturasState> = _facturasState.asStateFlow()

    // ═══════════════════════════════════════════════════════════════════
    // ESTADO PARA DETALLE DE FACTURA
    // ═══════════════════════════════════════════════════════════════════

    private val _facturaDetalleState = MutableStateFlow<FacturaDetalleState>(FacturaDetalleState.Idle)
    val facturaDetalleState: StateFlow<FacturaDetalleState> = _facturaDetalleState.asStateFlow()

    // ═══════════════════════════════════════════════════════════════════
    // FUNCIONES PÚBLICAS
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Realiza el checkout: convierte un carrito en factura
     * @param carritoId ID del carrito a procesar
     */
    fun realizarCheckout(carritoId: String) {
        viewModelScope.launch {
            _checkoutState.value = CheckoutState.Loading

            val result = repository.checkout(carritoId)

            _checkoutState.value = if (result.isSuccess) {
                CheckoutState.Success(result.getOrNull()!!)
            } else {
                CheckoutState.Error(result.exceptionOrNull()?.message ?: "Error desconocido")
            }
        }
    }

    /**
     * Carga las facturas de un usuario
     * @param userId ID del usuario (puede ser null para obtener todas)
     */
    fun cargarFacturas(userId: String? = null) {
        viewModelScope.launch {
            _facturasState.value = FacturasState.Loading

            val result = repository.listarFacturasPorUsuario(userId)

            _facturasState.value = if (result.isSuccess) {
                val facturas = result.getOrNull() ?: emptyList()
                if (facturas.isEmpty()) {
                    FacturasState.Empty
                } else {
                    FacturasState.Success(facturas)
                }
            } else {
                FacturasState.Error(result.exceptionOrNull()?.message ?: "Error desconocido")
            }
        }
    }

    /**
     * Carga el detalle de una factura específica
     * @param facturaId ID de la factura
     */
    fun cargarDetalleFactura(facturaId: String) {
        viewModelScope.launch {
            _facturaDetalleState.value = FacturaDetalleState.Loading

            val result = repository.obtenerFacturaPorId(facturaId)

            _facturaDetalleState.value = if (result.isSuccess) {
                FacturaDetalleState.Success(result.getOrNull()!!)
            } else {
                FacturaDetalleState.Error(result.exceptionOrNull()?.message ?: "Error desconocido")
            }
        }
    }

    /**
     * Resetea el estado del checkout (útil después de navegar)
     */
    fun resetCheckoutState() {
        _checkoutState.value = CheckoutState.Idle
    }

    /**
     * Resetea el estado del detalle
     */
    fun resetDetalleState() {
        _facturaDetalleState.value = FacturaDetalleState.Idle
    }

    /**
     * Crea una factura en estado BORRADOR (cotización sin descontar stock)
     *
     * DIFERENCIAS con realizarCheckout:
     * - NO descuenta stock automáticamente
     * - Crea factura con estado "BORRADOR"
     * - Ideal para cotizaciones y negociaciones
     * - Se puede emitir posteriormente desde Next.js
     *
     * IMPORTANTE:
     * - El cliente se obtiene AUTOMÁTICAMENTE del usuario autenticado
     * - Se envía como snapshot histórico al backend
     *
     * @param carritoItems Items del carrito a convertir en borrador
     */
    fun crearBorrador(carritoItems: List<CarritoItemDto>) {
        viewModelScope.launch {
            _checkoutState.value = CheckoutState.Loading

            val result = repository.crearBorrador(carritoItems)

            _checkoutState.value = if (result.isSuccess) {
                CheckoutState.Success(result.getOrNull()!!)
            } else {
                CheckoutState.Error(result.exceptionOrNull()?.message ?: "Error desconocido")
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // ESTADOS SELLADOS
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Estados posibles del proceso de checkout
     */
    sealed class CheckoutState {
        object Idle : CheckoutState()
        object Loading : CheckoutState()
        data class Success(val factura: Factura) : CheckoutState()
        data class Error(val message: String) : CheckoutState()
    }

    /**
     * Estados posibles del listado de facturas
     */
    sealed class FacturasState {
        object Loading : FacturasState()
        object Empty : FacturasState()
        data class Success(val facturas: List<Factura>) : FacturasState()
        data class Error(val message: String) : FacturasState()
    }

    /**
     * Estados posibles del detalle de factura
     */
    sealed class FacturaDetalleState {
        object Idle : FacturaDetalleState()
        object Loading : FacturaDetalleState()
        data class Success(val factura: Factura) : FacturaDetalleState()
        data class Error(val message: String) : FacturaDetalleState()
    }
}

