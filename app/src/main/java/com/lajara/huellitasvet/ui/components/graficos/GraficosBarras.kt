package com.lajara.huellitasvet.ui.components.graficos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GraficoBarras(
    datos: List<Pair<String, Int>>,
    modifier: Modifier = Modifier,
    altoMaximo: Dp = 64.dp
) {
    val colores = MaterialTheme.colorScheme
    val maximo = (datos.maxOfOrNull { it.second } ?: 0).coerceAtLeast(1)

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        datos.forEach { dato ->
            val alto = maxOf(4.dp, altoMaximo * (dato.second.toFloat() / maximo))
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = dato.second.toString(),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = colores.onBackground
                )
                Spacer(Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .width(36.dp)
                        .height(alto)
                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                        .background(
                            if (dato.second == maximo) colores.primary
                            else colores.primary.copy(alpha = 0.35f)
                        )
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = dato.first,
                    fontSize = 10.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    color = colores.onSurfaceVariant
                )
            }
        }
    }
}