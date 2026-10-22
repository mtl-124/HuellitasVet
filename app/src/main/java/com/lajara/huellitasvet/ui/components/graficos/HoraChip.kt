package com.lajara.huellitasvet.ui.components.graficos

import com.lajara.huellitasvet.ui.theme.tarjeta
import com.lajara.huellitasvet.ui.theme.bordeSuave
import com.lajara.huellitasvet.ui.theme.fondoInactivo
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
        deshabilitada -> colores.fondoInactivo
        seleccionada -> colores.primary
        else -> colores.tarjeta
    }
    Box(
        modifier = modifier
            .height(38.dp)
            .clip(forma)
            .background(fondo)
            .border(
                border = if (deshabilitada || seleccionada) BorderStroke(0.dp, Color.Transparent) else BorderStroke(1.dp, colores.bordeSuave),
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
                seleccionada -> colores.onPrimary
                else -> colores.onBackground
            }
        )
    }
}