package com.example.facturacion_inventario.ui.screens

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.facturacion_inventario.ui.viewmodel.FacturaViewModel

/**
 * Pantalla de Listado de Facturas - PLACEHOLDER
 *
 * Esta es una versión simplificada. Implementa tu propia UI según necesites.
 * El ViewModel está listo para usar:
 *
 * ```
 * val facturaViewModel: FacturaViewModel = viewModel()
 * facturaViewModel.cargarFacturas(userId)
 *
 * val facturasState by facturaViewModel.facturasState.collectAsState()
 * when (facturasState) {
 *     is FacturaViewModel.FacturasState.Success -> { /* Mostrar lista */ }
 *     is FacturaViewModel.FacturasState.Empty -> { /* Sin facturas */ }
 *     is FacturaViewModel.FacturasState.Error -> { /* Error */ }
 * }
 * ```
 */
@Suppress("unused", "UNUSED_PARAMETER", "UNUSED_VARIABLE")
@Composable
fun FacturasScreen(
    userId: String? = null,
    onNavigateBack: () -> Unit,
    onFacturaClick: (String) -> Unit,
    viewModel: FacturaViewModel = viewModel()
) {
    val facturasState by viewModel.facturasState.collectAsState()

    LaunchedEffect(userId) {
        viewModel.cargarFacturas(userId)
    }

    // TO DO: Implementar tu UI aquí usando tus componentes de UI existentes
}
