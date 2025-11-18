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
import com.example.facturacion_inventario.data.remote.model.CarritoItemDto
import com.example.facturacion_inventario.ui.viewmodel.FacturaViewModel

/**
 * Diálogo para Crear Factura en Estado BORRADOR
 *
 * DIFERENCIAS con CheckoutDialog:
 * - Crea facturas con estado "BORRADOR"
 * - NO descuenta stock automáticamente
 * - Ideal para cotizaciones y negociaciones
 * - Se puede emitir posteriormente desde Next.js
 *
 * USO:
 * ```kotlin
 * var showBorradorDialog by remember { mutableStateOf(false) }
 *
 * Button(onClick = { showBorradorDialog = true }) {
 *     Text("Crear Cotización")
 * }
 *
 * if (showBorradorDialog) {
 *     BorradorDialog(
 *         carritoItems = carritoItems,
 *         onDismiss = { showBorradorDialog = false },
 *         onSuccess = { facturaId ->
 *             // Navegar o mostrar éxito
 *             showBorradorDialog = false
 *         }
 *     )
 * }
 * ```
 */
@Composable
fun BorradorDialog(
    carritoItems: List<CarritoItemDto>,
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
                        // Título
                        Text(
                            text = "¿Crear Cotización?",
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Información sobre el borrador
                        Text(
                            text = "Se creará una factura en estado BORRADOR para cotización. NO se descontará stock del inventario.",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Info: Los datos del cliente se obtienen automáticamente
                        Card(
                            backgroundColor = MaterialTheme.colors.secondary.copy(alpha = 0.1f),
                            elevation = 0.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Text(
                                    text = "ℹ️ Información",
                                    style = MaterialTheme.typography.subtitle2,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.secondary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Los datos del cliente se obtendrán automáticamente de tu perfil",
                                    style = MaterialTheme.typography.caption
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Ventajas del borrador
                        Card(
                            backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.1f),
                            elevation = 0.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Text(
                                    text = "💡 Ventajas:",
                                    style = MaterialTheme.typography.subtitle2,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "• No descuenta stock\n• Permite negociación\n• Se emite después desde Next.js",
                                    style = MaterialTheme.typography.caption
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))

                        // Botones
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
                                onClick = {
                                    // Crear borrador (el cliente se obtiene automáticamente)
                                    viewModel.crearBorrador(carritoItems)
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Crear")
                            }
                        }
                    }

                    is FacturaViewModel.CheckoutState.Loading -> {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Creando cotización...",
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
                            text = "✓ Cotización Creada",
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
                        Card(
                            backgroundColor = MaterialTheme.colors.secondary.copy(alpha = 0.1f),
                            elevation = 0.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Estado: BORRADOR",
                                    style = MaterialTheme.typography.subtitle2,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colors.secondary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Total: $${String.format("%.2f", factura.total)}",
                                    style = MaterialTheme.typography.h6,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
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

