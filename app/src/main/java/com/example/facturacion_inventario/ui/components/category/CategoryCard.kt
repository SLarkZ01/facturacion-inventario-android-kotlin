package com.example.facturacion_inventario.ui.components.category

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
 import androidx.compose.ui.layout.ContentScale
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.facturacion_inventario.domain.model.Category
import com.example.facturacion_inventario.ui.theme.Dimens
import com.example.facturacion_inventario.R

@Composable
fun CategoryCard(
    category: Category,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.s)
            .clickable { onClick() },
        elevation = 4.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .padding(Dimens.lg)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Prioridad: 1) Imagen desde URL, 2) Icono de Ermotos por defecto
            if (!category.imageUrl.isNullOrBlank()) {
                // Cargar imagen desde URL usando Coil
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(category.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = category.name,
                    modifier = Modifier.size(100.dp),
                    contentScale = ContentScale.Fit,
                    loading = {
                        // Mostrar indicador de carga
                        Box(
                            modifier = Modifier.size(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(40.dp))
                        }
                    },
                    error = {
                        // Si falla, mostrar ícono de Ermotos
                        Image(
                            painter = painterResource(id = R.drawable.ermotoshd),
                            contentDescription = category.name,
                            modifier = Modifier.size(100.dp)
                        )
                    }
                )
            } else {
                // No hay URL, usar ícono de Ermotos por defecto
                Image(
                    painter = painterResource(id = R.drawable.ermotoshd),
                    contentDescription = category.name,
                    modifier = Modifier.size(100.dp)
                )
            }

            Spacer(modifier = Modifier.width(Dimens.md))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.onSurface
                )
                Spacer(modifier = Modifier.height(Dimens.s))
                Text(
                    text = category.description,
                    fontSize = 13.sp,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
            }

            TextButton(onClick = onClick) {
                Text(text = "Ver", color = MaterialTheme.colors.primary)
            }
        }
    }
}
