package com.example.facturacion_inventario.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.facturacion_inventario.domain.model.Category
import com.example.facturacion_inventario.ui.components.category.CategoryCard
import com.example.facturacion_inventario.ui.store.CategoryListState
import com.example.facturacion_inventario.ui.store.CategoryViewModel
import com.example.facturacion_inventario.ui.theme.Dimens

/**
 * Pantalla de categorías que consume datos REALES de la API de Spring Boot
 */
@Composable
fun CategoriesScreenRemote(
    onCategoryClick: (String) -> Unit,
    viewModel: CategoryViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is CategoryListState.Loading -> {
            LoadingCategoriesContent()
        }
        is CategoryListState.Success -> {
            CategoriesSuccessContent(
                categories = state.categories,
                onCategoryClick = onCategoryClick
            )
        }
        is CategoryListState.Error -> {
            ErrorCategoriesContent(
                errorMessage = state.message,
                onRetry = { viewModel.retry() }
            )
        }
        is CategoryListState.Empty -> {
            EmptyCategoriesContent(
                onRetry = { viewModel.retry() }
            )
        }
    }
}

@Composable
private fun LoadingCategoriesContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colors.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Cargando categorías...",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun CategoriesSuccessContent(
    categories: List<Category>,
    onCategoryClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = Dimens.lg, vertical = Dimens.md),
        verticalArrangement = Arrangement.spacedBy(Dimens.md)
    ) {
        item(key = "header") {
            Spacer(modifier = Modifier.height(Dimens.lg))
            Text(
                text = "Todas las Categorías",
                style = MaterialTheme.typography.h6
            )
            Text(
                text = "${categories.size} categorías disponibles",
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(Dimens.md))
        }

        items(categories, key = { it.id }) { category ->
            CategoryCard(
                category = category,
                onClick = { onCategoryClick(category.id) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ErrorCategoriesContent(
    errorMessage: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "⚠️",
                style = MaterialTheme.typography.h3,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Error al cargar categorías",
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.body2,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onRetry,
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text("Reintentar")
            }
        }
    }
}

@Composable
private fun EmptyCategoriesContent(
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "📦",
                style = MaterialTheme.typography.h3,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "No hay categorías disponibles",
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "No se encontraron categorías en el sistema",
                style = MaterialTheme.typography.body2,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onRetry,
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text("Actualizar")
            }
        }
    }
}

