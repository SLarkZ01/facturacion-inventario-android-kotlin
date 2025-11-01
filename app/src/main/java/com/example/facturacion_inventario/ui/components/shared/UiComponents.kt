package com.example.facturacion_inventario.ui.components.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun AppOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable (() -> Unit)? = null,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = Modifier.fillMaxWidth().then(modifier),
        singleLine = singleLine,
        colors = TextFieldDefaults.outlinedTextFieldColors(),
        visualTransformation = visualTransformation
    )
}

/**
 * InputField: wrapper ligero alrededor de OutlinedTextField para inputs reutilizables.
 */
@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    isPassword: Boolean = false,
    modifier: Modifier = Modifier
) {
    AppOutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = labelText, color = MaterialTheme.colors.secondary) },
        singleLine = true,
        visualTransformation = if (isPassword) androidx.compose.ui.text.input.PasswordVisualTransformation() else VisualTransformation.None,
        modifier = modifier
    )
}

@Composable
fun LoadingCard() {
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(80.dp)) {
        Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colors.surface) {
            // Simple placeholder for loading state
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colors.primary)
            }
        }
    }
}

/**
 * AuthHeader: cabecera simple utilizada en pantallas de autenticación
 */
@Composable
fun AuthHeader(title: String, subtitle: String? = null) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = com.example.facturacion_inventario.R.drawable.ic_motorcycle_animated), contentDescription = "logo", modifier = Modifier.size(80.dp))
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = title, color = MaterialTheme.colors.onBackground, style = MaterialTheme.typography.h5)
        subtitle?.let { Text(text = it, color = MaterialTheme.colors.secondary, style = MaterialTheme.typography.body2) }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

/**
 * AppTopBar: barra superior reutilizable con botón back y título
 */
@Composable
fun AppTopBar(title: String, onBack: () -> Unit) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(painter = painterResource(id = com.example.facturacion_inventario.R.drawable.ic_arrow_back), contentDescription = "Atrás")
            }
        },
        backgroundColor = MaterialTheme.colors.surface,
        modifier = Modifier.height(56.dp)
    )
}

/**
 * SimpleSearchBar: campo de búsqueda compacto reutilizable
 */
@Composable
fun SimpleSearchBar(query: String, onQueryChange: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        label = { Text(text = "Buscar", color = MaterialTheme.colors.secondary) },
        singleLine = true,
        modifier = modifier
    )
}

/**
 * AppTopBar y helpers adicionales (existing helpers)
 */
