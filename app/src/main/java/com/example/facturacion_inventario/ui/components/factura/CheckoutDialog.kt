package com.example.facturacion_inventario.ui.components.factura

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.facturacion_inventario.ui.viewmodel.FacturaViewModel

/**
 * Diálogo de Checkout/Generar Factura
 * Maneja to do el proceso de conversión de carrito a factura
 *
 * USO:
 * ```kotlin
 * var showCheckoutDialog by remember { mutableStateOf(false) }
 *
 * Button(onClick = { showCheckoutDialog = true }) {
 *     Text("Generar Factura")
 * }
 *
 * if (showCheckoutDialog) {
 *     CheckoutDialog(
 *         carritoId = "tu-carrito-id",
 *         onDismiss = { showCheckoutDialog = false },
 *         onSuccess = { facturaId ->
 *             // Navegar a detalle de factura o mostrar éxito
 *             showCheckoutDialog = false
 *         }
 *     )
 * }
 * ```
 */
@Composable
fun CheckoutDialog(
    carritoId: String,
    onDismiss: () -> Unit,
    onSuccess: (String) -> Unit,
    viewModel: FacturaViewModel = viewModel()
) {
    val checkoutState by viewModel.checkoutState.collectAsState()

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colors.surface,
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (checkoutState) {
                    is FacturaViewModel.CheckoutState.Idle -> {
                        // Confirmación inicial
                        Text(
                            text = "¿Generar Factura?",
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Se creará una factura con los productos del carrito y se actualizará el inventario.",
                            style = MaterialTheme.typography.body2
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedButton(
                                onClick = onDismiss,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Cancelar")
                            }
                            Button(
                                onClick = { viewModel.realizarCheckout(carritoId) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Confirmar")
                            }
                        }
                    }

                    is FacturaViewModel.CheckoutState.Loading -> {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Generando factura...",
                            style = MaterialTheme.typography.body1
                        )
                    }

                    is FacturaViewModel.CheckoutState.Success -> {
                        val factura = (checkoutState as FacturaViewModel.CheckoutState.Success).factura

                        LaunchedEffect(Unit) {
                            kotlinx.coroutines.delay(500)
                            onSuccess(factura.id)
                            viewModel.resetCheckoutState()
                        }

                        Text(
                            text = "✓ Factura Creada",
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "N° ${factura.numeroFactura}",
                            style = MaterialTheme.typography.body1
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Total: $${String.format("%.2f", factura.total)}",
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    is FacturaViewModel.CheckoutState.Error -> {
                        val error = (checkoutState as FacturaViewModel.CheckoutState.Error).message

                        Text(
                            text = "Error",
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = error,
                            style = MaterialTheme.typography.body2
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedButton(
                                onClick = {
                                    viewModel.resetCheckoutState()
                                    onDismiss()
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Cerrar")
                            }
                            Button(
                                onClick = {
                                    viewModel.resetCheckoutState()
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Snackbar para mostrar resultado del checkout (alternativa más simple)
 */
@Suppress("unused")
@Composable
fun CheckoutSnackbar(
    carritoId: String,
    onSuccess: (String) -> Unit,
    onError: (String) -> Unit,
    viewModel: FacturaViewModel = viewModel()
) {
    val checkoutState by viewModel.checkoutState.collectAsState()

    LaunchedEffect(checkoutState) {
        when (checkoutState) {
            is FacturaViewModel.CheckoutState.Success -> {
                val factura = (checkoutState as FacturaViewModel.CheckoutState.Success).factura
                onSuccess(factura.id)
                viewModel.resetCheckoutState()
            }
            is FacturaViewModel.CheckoutState.Error -> {
                val error = (checkoutState as FacturaViewModel.CheckoutState.Error).message
                onError(error)
                viewModel.resetCheckoutState()
            }
            else -> {}
        }
    }
}
