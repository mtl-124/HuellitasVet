package com.lajara.huellitasvet.ui.components.graficos

import com.lajara.huellitasvet.ui.theme.esClaro
import com.lajara.huellitasvet.ui.theme.ambar
import com.lajara.huellitasvet.ui.theme.ambarBorde
import com.lajara.huellitasvet.ui.theme.ambarTexto
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lajara.huellitasvet.ui.theme.AmbarBorde
import com.lajara.huellitasvet.ui.theme.AmbarContenedor
import com.lajara.huellitasvet.ui.theme.AmbarTexto
import com.lajara.huellitasvet.ui.theme.LavandaIcono
import com.lajara.huellitasvet.ui.theme.LavandaSeleccion
import com.lajara.huellitasvet.ui.theme.MentaBorde
import com.lajara.huellitasvet.ui.theme.VerdeContenedor
import com.lajara.huellitasvet.ui.theme.VerdeSobreContenedor

@Composable
fun EstadoCitaChip(
    estado: String,
    modifier: Modifier = Modifier
) {
    val claro = MaterialTheme.colorScheme.esClaro
    val fondo: Color
    val borde: Color
    val texto: Color
    when (estado.uppercase()) {
        "CONFIRMADA" -> {
            fondo = if (claro) VerdeContenedor else Color(0xFF14382B)
            borde = if (claro) MentaBorde else Color(0xFF245C46)
            texto = if (claro) Color(0xFF047857) else Color(0xFF6EE7B7)
        }
        "PENDIENTE" -> {
            fondo = MaterialTheme.colorScheme.ambar
            borde = MaterialTheme.colorScheme.ambarBorde
            texto = MaterialTheme.colorScheme.ambarTexto
        }
        "ATENDIDA" -> {
            fondo = if (claro) LavandaSeleccion else Color(0xFF2E2A4A)
            borde = if (claro) Color(0xFFD0C4EE) else Color(0xFF4B4478)
            texto = if (claro) LavandaIcono else Color(0xFFD9D0F5)
        }
        else -> {
            fondo = if (claro) Color(0xFFFEE2E2) else Color(0xFF3B1A1A)
            borde = if (claro) Color(0xFFFECACA) else Color(0xFF6B2B2B)
            texto = if (claro) Color(0xFFB91C1C) else Color(0xFFFCA5A5)
        }
    }
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = fondo,
        border = BorderStroke(1.dp, borde)
    ) {
        Text(
            text = estado.lowercase().replaceFirstChar { it.uppercase() },
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            color = texto,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}