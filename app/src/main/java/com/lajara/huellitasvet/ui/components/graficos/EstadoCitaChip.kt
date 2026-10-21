package com.lajara.huellitasvet.ui.components.graficos

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
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
    val fondo: Color
    val borde: Color
    val texto: Color
    when (estado.uppercase()) {
        "CONFIRMADA" -> {
            fondo = VerdeContenedor; borde = MentaBorde; texto = Color(0xFF047857)
        }
        "PENDIENTE" -> {
            fondo = AmbarContenedor; borde = AmbarBorde; texto = AmbarTexto
        }
        "ATENDIDA" -> {
            fondo = LavandaSeleccion; borde = Color(0xFFD0C4EE); texto = LavandaIcono
        }
        else -> {
            fondo = Color(0xFFFEE2E2); borde = Color(0xFFFECACA); texto = Color(0xFFB91C1C)
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