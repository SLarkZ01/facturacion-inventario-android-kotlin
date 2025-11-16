package com.example.facturacion_inventario.ui.components.media

import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri

// Composable para mostrar imagen desde una URL (Cloudinary). Usa Coil (compose) y sigue buenas prácticas:
// - No contiene lógica de negocio
// - Permite pasar placeholder/error
// - No realiza transformaciones de URL (backend o Cloudinary deberían proveer la URL final)

@Composable
fun CloudinaryImage(
    url: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    placeholderRes: Int? = null,
    errorRes: Int? = null
) {
    val context = LocalContext.current
    if (url.isNullOrBlank()) {
        // Mostrar placeholder si no hay url
        if (placeholderRes != null) {
            Image(
                painter = painterResource(id = placeholderRes),
                contentDescription = contentDescription,
                modifier = modifier
            )
        }
        return
    }

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(url)
            .crossfade(true)
            .build(),
        contentDescription = contentDescription,
        modifier = modifier
    )
}

// Composable para reproducir video desde una URL pública (Cloudinary). Usa ExoPlayer y gestiona el lifecycle adecuadamente.
@Composable
fun CloudinaryVideoPlayer(
    url: String?,
    modifier: Modifier = Modifier,
    autoPlay: Boolean = false
) {
    val context = LocalContext.current
    var player: ExoPlayer? by remember { mutableStateOf(null) }

    DisposableEffect(key1 = url) {
        if (url.isNullOrBlank()) {
            onDispose { }
        } else {
            player = ExoPlayer.Builder(context).build().apply {
                val mediaItem = MediaItem.fromUri(url.toUri())
                setMediaItem(mediaItem)
                prepare()
                playWhenReady = autoPlay
            }
            onDispose {
                player?.release()
                player = null
            }
        }
    }

    if (player == null) {
        // Mostrar un indicador de carga o placeholder si no hay player
        Box(modifier = modifier) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp))
        }
    } else {
        AndroidView(factory = { ctx ->
            PlayerView(ctx).apply {
                this.player = player
                useController = true
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }, modifier = modifier)
    }
}
