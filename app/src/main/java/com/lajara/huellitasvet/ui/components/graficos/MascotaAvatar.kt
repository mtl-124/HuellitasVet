package com.lajara.huellitasvet.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun MascotaAvatar(
    nombre: String,
    especie: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(8.dp)
    ) {
        CirculoMascota(especie = especie)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = nombre, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun CirculoMascota(
    especie: String,
    modifier: Modifier = Modifier,
    tamano: Dp = 56.dp
) {
    Box(
        modifier = modifier
            .size(tamano)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        if (especie.equals("GATO", true) || especie.equals("PERRO", true)) {
            MascotaCara(especie, Modifier.size(tamano * 0.68f))
        } else {
            Icon(
                imageVector = Icons.Default.Pets,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(tamano * 0.5f)
            )
        }
    }
}

@Composable
fun MascotaCara(especie: String, modifier: Modifier = Modifier) {
    val esGato = especie.equals("GATO", true)
    Canvas(modifier = modifier) {
        if (esGato) dibujarGato() else dibujarPerro()
    }
}

private fun DrawScope.dibujarGato() {
    val w = size.width
    val h = size.height
    val gris = Color(0xFF8C91A5)
    val orejaIzq = Path().apply {
        moveTo(0.14f * w, 0.06f * h)
        lineTo(0.12f * w, 0.52f * h)
        lineTo(0.46f * w, 0.30f * h)
        close()
    }
    val orejaDer = Path().apply {
        moveTo(0.86f * w, 0.06f * h)
        lineTo(0.88f * w, 0.52f * h)
        lineTo(0.54f * w, 0.30f * h)
        close()
    }
    drawPath(orejaIzq, gris)
    drawPath(orejaDer, gris)
    val internaIzq = Path().apply {
        moveTo(0.19f * w, 0.20f * h)
        lineTo(0.18f * w, 0.42f * h)
        lineTo(0.34f * w, 0.31f * h)
        close()
    }
    val internaDer = Path().apply {
        moveTo(0.81f * w, 0.20f * h)
        lineTo(0.82f * w, 0.42f * h)
        lineTo(0.66f * w, 0.31f * h)
        close()
    }
    drawPath(internaIzq, Color(0xFFE7A5B5))
    drawPath(internaDer, Color(0xFFE7A5B5))
    drawOval(gris, Offset(0.08f * w, 0.22f * h), Size(0.84f * w, 0.72f * h))
    drawOval(Color(0xFFC9CCD8), Offset(0.30f * w, 0.56f * h), Size(0.40f * w, 0.30f * h))
    drawCircle(Color(0xFF2B2F3A), 0.055f * w, Offset(0.33f * w, 0.52f * h))
    drawCircle(Color(0xFF2B2F3A), 0.055f * w, Offset(0.67f * w, 0.52f * h))
    drawCircle(Color(0xFFE58FA6), 0.045f * w, Offset(0.50f * w, 0.62f * h))
}

private fun DrawScope.dibujarPerro() {
    val w = size.width
    val h = size.height
    val oreja = Color(0xFF6B3A1F)
    drawOval(oreja, Offset(0.02f * w, 0.16f * h), Size(0.26f * w, 0.52f * h))
    drawOval(oreja, Offset(0.72f * w, 0.16f * h), Size(0.26f * w, 0.52f * h))
    drawOval(Color(0xFFA86232), Offset(0.14f * w, 0.10f * h), Size(0.72f * w, 0.82f * h))
    drawOval(Color(0xFFE9C9A0), Offset(0.30f * w, 0.50f * h), Size(0.40f * w, 0.34f * h))
    drawCircle(Color(0xFF2B2F3A), 0.05f * w, Offset(0.36f * w, 0.44f * h))
    drawCircle(Color(0xFF2B2F3A), 0.05f * w, Offset(0.64f * w, 0.44f * h))
    drawOval(Color(0xFF3A2416), Offset(0.42f * w, 0.54f * h), Size(0.16f * w, 0.11f * h))
}