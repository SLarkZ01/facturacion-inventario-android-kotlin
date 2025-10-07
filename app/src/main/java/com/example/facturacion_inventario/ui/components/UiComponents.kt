package com.example.facturacion_inventario.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppOutlinedTextField(value: String, onValueChange: (String) -> Unit, label: @Composable (() -> Unit)? = null, singleLine: Boolean = true) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = Modifier.fillMaxWidth(),
        singleLine = singleLine,
        colors = TextFieldDefaults.outlinedTextFieldColors()
    )
}

@Composable
fun LoadingCard() {
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(80.dp), elevation = 4.dp) {
        Surface(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colors.surface) {
            // Simple placeholder for loading state
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colors.primary)
            }
        }
    }
}
