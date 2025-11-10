package com.example.facturacion_inventario.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.facturacion_inventario.ui.store.*
import com.example.facturacion_inventario.ui.theme.Dimens

/**
 * Pantalla de detalle de producto que consume datos de la API
 */
@Composable
fun ProductDetailScreenRemote(
    productId: String,
    cartViewModel: RemoteCartViewModel,
    detailViewModel: ProductDetailViewModel = viewModel()
) {
    val uiState by detailViewModel.uiState.collectAsState()

    // Cargar producto cuando se monta la pantalla
    LaunchedEffect(productId) {
        detailViewModel.loadProduct(productId)
    }

    when (val state = uiState) {
        is ProductDetailState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(Dimens.md))
                    Text(
                        text = "Cargando producto...",
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }

        is ProductDetailState.Success -> {
            // Usar el ProductDetailContent existente
            ProductDetailContent(
                product = state.product,
                onAddToCart = { /* Mensaje de confirmación */ },
                cartViewModel = cartViewModel
            )
        }

        is ProductDetailState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(Dimens.xl)
                ) {
                    Text(
                        text = "Error al cargar producto",
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.error
                    )
                    Spacer(modifier = Modifier.height(Dimens.md))
                    Text(
                        text = state.message,
                        style = MaterialTheme.typography.body2,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(Dimens.lg))
                    Button(onClick = { detailViewModel.retry(productId) }) {
                        Text("Reintentar")
                    }
                }
            }
        }
    }
}
