package com.lajara.huellitasvet.ui.components.graficos

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lajara.huellitasvet.ui.theme.GrisBorde

@Composable
fun HoraChip(
    hora: String,
    seleccionada: Boolean,
    deshabilitada: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(10.dp)
    val colores = MaterialTheme.colorScheme
    val fondo = when {
        deshabilitada -> Color(0xFFEEF0F2)
        seleccionada -> colores.primary
        else -> Color.White
    }
    Box(
        modifier = modifier
            .height(38.dp)
            .clip(forma)
            .background(fondo)
            .border(
                border = if (deshabilitada || seleccionada) BorderStroke(0.dp, Color.Transparent) else BorderStroke(1.dp, GrisBorde),
                shape = forma
            )
            .clickable(enabled = !deshabilitada, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = hora,
            fontSize = 14.sp,
            fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Medium,
            textDecoration = if (deshabilitada) TextDecoration.LineThrough else TextDecoration.None,
            color = when {
                deshabilitada -> Color(0xFF9CA3AF)
                seleccionada -> Color.White
                else -> colores.onBackground
            }
        )
    }
}