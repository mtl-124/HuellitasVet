package com.lajara.huellitasvet.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lajara.huellitasvet.ui.theme.fondoInactivo

@Composable
fun GraficoDona(
    datos: List<Pair<String, Int>>,
    colores: List<Color>,
    modifier: Modifier = Modifier,
    tamano: Dp = 96.dp,
    grosor: Dp = 14.dp,
    etiquetaCentro: String = "citas"
) {
    val total = datos.sumOf { it.second }
    val colorVacio = MaterialTheme.colorScheme.fondoInactivo

    Box(
        modifier = modifier.size(tamano),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val trazo = grosor.toPx()
            val diametro = size.minDimension - trazo
            val origen = Offset(trazo / 2f, trazo / 2f)
            val area = Size(diametro, diametro)

            if (total == 0) {
                drawArc(
                    color = colorVacio,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = origen,
                    size = area,
                    style = Stroke(width = trazo)
                )
            } else {
                var inicio = -90f
                datos.forEachIndexed { i, dato ->
                    if (dato.second > 0) {
                        val barrido = 360f * dato.second / total
                        drawArc(
                            color = colores[i % colores.size],
                            startAngle = inicio,
                            sweepAngle = barrido,
                            useCenter = false,
                            topLeft = origen,
                            size = area,
                            style = Stroke(width = trazo)
                        )
                        inicio += barrido
                    }
                }
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = total.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = etiquetaCentro,
                fontSize = 9.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}