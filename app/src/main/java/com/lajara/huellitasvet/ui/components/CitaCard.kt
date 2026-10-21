package com.lajara.huellitasvet.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lajara.huellitasvet.data.local.relation.CitaConDetalle
import com.lajara.huellitasvet.model.Horarios
import com.lajara.huellitasvet.ui.components.graficos.EstadoCitaChip
import com.lajara.huellitasvet.ui.theme.GrisBorde
import com.lajara.huellitasvet.ui.theme.MentaTarjeta
import java.time.LocalDate

@Composable
fun CitaCard(
    citaConDetalle: CitaConDetalle,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    onCancelar: (() -> Unit)? = null
) {
    val colores = MaterialTheme.colorScheme
    val forma = RoundedCornerShape(14.dp)
    val fecha = LocalDate.parse(citaConDetalle.cita.fecha)
    val icono: ImageVector = when (citaConDetalle.servicio.icono) {
        "syringe" -> Icons.Filled.Vaccines
        "scissors" -> Icons.Filled.ContentCut
        else -> Icons.Filled.Favorite
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(forma)
            .background(Color.White)
            .then(
                if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MentaTarjeta),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = colores.primary,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = citaConDetalle.servicio.nombre,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = colores.onBackground
                )
                Text(
                    text = "${citaConDetalle.mascota.mascota.nombre} · " +
                            "${Horarios.nombreDia(fecha)} ${fecha.dayOfMonth} · ${citaConDetalle.cita.hora}",
                    fontSize = 12.sp,
                    color = colores.onSurfaceVariant
                )
                Spacer(Modifier.height(8.dp))
                EstadoCitaChip(estado = citaConDetalle.cita.estado)
            }
        }
        if (onCancelar != null) {
            Text(
                text = "Cancelar",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .clickable(onClick = onCancelar)
                    .padding(end = 14.dp, bottom = 10.dp, start = 8.dp, top = 6.dp),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFDC2626)
            )
        }
    }
}