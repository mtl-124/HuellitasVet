package com.lajara.huellitasvet.ui.components.graficos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lajara.huellitasvet.data.local.relation.CitaConDetalle
import java.time.LocalDate
import java.util.Locale

private val RojoCorazon = Color(0xFFE11D48)

fun formatoDiaMes(fecha: String): String = runCatching {
    val d = LocalDate.parse(fecha)
    val meses = listOf("ene", "feb", "mar", "abr", "may", "jun", "jul", "ago", "sep", "oct", "nov", "dic")
    String.format(Locale.US, "%02d %s", d.dayOfMonth, meses[d.monthValue - 1])
}.getOrDefault(fecha)

fun formatoPeso(peso: Double): String = String.format(Locale.US, "%.1f kg", peso)

@Composable
fun HistorialItem(
    item: CitaConDetalle,
    esUltimo: Boolean,
    modifier: Modifier = Modifier
) {
    val colores = MaterialTheme.colorScheme
    val colorLinea = colores.primary.copy(alpha = 0.25f)
    val forma = RoundedCornerShape(12.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .width(18.dp)
                .fillMaxHeight()
                .drawBehind {
                    if (!esUltimo) {
                        val x = size.width / 2
                        drawLine(
                            color = colorLinea,
                            start = Offset(x, 26.dp.toPx()),
                            end = Offset(x, size.height + 22.dp.toPx()),
                            strokeWidth = 2.dp.toPx()
                        )
                    }
                }
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(colores.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(12.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .clip(forma)
                .background(Color.White)
                .border(1.dp, colores.outlineVariant, forma)
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    item.servicio.nombre,
                    modifier = Modifier.weight(1f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    formatoDiaMes(item.cita.fecha),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = colores.primary
                )
            }
            Spacer(Modifier.height(2.dp))
            Text(item.cita.nota, fontSize = 10.5.sp, color = colores.onSurfaceVariant)
            item.cita.pesoRegistrado?.let { peso ->
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = null,
                        tint = RojoCorazon,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "Peso: ${formatoPeso(peso)}",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}