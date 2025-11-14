package com.example.facturacion_inventario.ui.screens

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.facturacion_inventario.ui.viewmodel.FacturaViewModel

/**
 * Pantalla de Checkout - PLACEHOLDER
 *
 * Esta es una versión simplificada. Implementa tu propia UI según necesites.
 * El ViewModel está listo para usar:
 *
 * ```
 * val facturaViewModel: FacturaViewModel = viewModel()
 * facturaViewModel.realizarCheckout(carritoId)
 *
 * val checkoutState by facturaViewModel.checkoutState.collectAsState()
 * when (checkoutState) {
 *     is FacturaViewModel.CheckoutState.Success -> { /* Éxito */ }
 *     is FacturaViewModel.CheckoutState.Error -> { /* Error */ }
 *     is FacturaViewModel.CheckoutState.Loading -> { /* Cargando */ }
 * }
 * ```
 */
@Suppress("unused", "UNUSED_PARAMETER")
@Composable
fun CheckoutScreen(
    carritoId: String,
    cartItems: List<Any>,
    onNavigateBack: () -> Unit,
    onCheckoutSuccess: (facturaId: String) -> Unit,
    viewModel: FacturaViewModel = viewModel()
) {
    val checkoutState by viewModel.checkoutState.collectAsState()

    // TO DO: Implementar tu UI aquí usando tus componentes de UI existentes

    LaunchedEffect(checkoutState) {
        if (checkoutState is FacturaViewModel.CheckoutState.Success) {
            val factura = (checkoutState as FacturaViewModel.CheckoutState.Success).factura
            onCheckoutSuccess(factura.id)
            viewModel.resetCheckoutState()
        }
    }
}
