package com.example.facturacion_inventario.ui.components.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.example.facturacion_inventario.domain.model.Product
import com.example.facturacion_inventario.domain.model.MediaType
import com.example.facturacion_inventario.R
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState

@Composable
fun ProductMediaCarousel(
    product: Product,
    modifier: Modifier = Modifier
) {
    // Si el producto tiene medios, usarlos, sino usar la imagen por defecto
    val mediaList = if (product.mediaList.isNotEmpty()) {
        product.mediaList
    } else {
        listOf(
            com.example.facturacion_inventario.domain.model.ProductMedia(
                resourceId = product.imageRes,
                type = MediaType.IMAGE
            )
        )
    }

    val pagerState = rememberPagerState(pageCount = { mediaList.size })

    Column(modifier = modifier) {
        // Carrusel de medios
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val media = mediaList[page]

                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    elevation = 0.dp,
                    backgroundColor = MaterialTheme.colors.surface
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        when (media.type) {
                            MediaType.IMAGE -> {
                                Image(
                                    painter = painterResource(id = media.resourceId),
                                    contentDescription = "Imagen ${page + 1} de ${product.name}",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(240.dp)
                                )
                            }
                            MediaType.VIDEO -> {
                                // Para videos, mostrar una imagen con un icono de play
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Image(
                                        painter = painterResource(id = media.resourceId),
                                        contentDescription = "Video ${page + 1} de ${product.name}",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(240.dp)
                                    )
                                    // Overlay con icono de play
                                    Surface(
                                        modifier = Modifier.size(60.dp),
                                        shape = CircleShape,
                                        color = Color.Black.copy(alpha = 0.6f)
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_motorcycle_animated),
                                                contentDescription = "Reproducir video",
                                                tint = Color.White,
                                                modifier = Modifier.size(30.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Contador de medios en la esquina superior derecha
            if (mediaList.size > 1) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp),
                    shape = MaterialTheme.shapes.small,
                    color = Color.Black.copy(alpha = 0.6f)
                ) {
                    Text(
                        text = "${pagerState.currentPage + 1}/${mediaList.size}",
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Indicadores de posición (puntos)
        if (mediaList.size > 1) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(mediaList.size) { index ->
                    Box(
                        modifier = Modifier
                            .size(if (index == pagerState.currentPage) 10.dp else 8.dp)
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(
                                if (index == pagerState.currentPage)
                                    Color(0xFFFF6F00)
                                else
                                    MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
                            )
                    )
                    if (index < mediaList.size - 1) {
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }
        }
    }
}
