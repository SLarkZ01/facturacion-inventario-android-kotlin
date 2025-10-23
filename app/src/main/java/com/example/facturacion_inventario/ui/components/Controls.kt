package com.example.facturacion_inventario.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.ColumnScope

// Tokens
import com.example.facturacion_inventario.ui.theme.Dimens

// PrimaryButton: botón principal reutilizable para acciones importantes
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.buttonHeight),
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(text = text, color = MaterialTheme.colors.onPrimary)
    }
}

// Secondary text button usado para links o acciones menos importantes
@Composable
fun SecondaryTextButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    TextButton(onClick = onClick, modifier = modifier.fillMaxWidth()) {
        Text(text = text, color = MaterialTheme.colors.primary)
    }
}

// SocialButton: botón estilizado para proveedores externos (Google, Facebook...), acepta un icono drawable
@Composable
fun SocialButton(
    text: String,
    @DrawableRes iconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    background: Color = MaterialTheme.colors.surface
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.buttonHeight),
        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = background),
        shape = MaterialTheme.shapes.medium
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = text,
            modifier = Modifier.size(Dimens.iconSize)
        )
        Spacer(Modifier.width(Dimens.md))
        Text(text = text, color = MaterialTheme.colors.onBackground)
    }
}

// AuthCard: envoltorio reutilizable para formularios de autenticación (login/registro)
@Composable
fun AuthCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 4.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(Dimens.lg), content = content)
    }
}
