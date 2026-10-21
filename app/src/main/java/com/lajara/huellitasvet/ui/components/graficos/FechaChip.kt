package com.lajara.huellitasvet.ui.components.graficos

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lajara.huellitasvet.model.Horarios
import com.lajara.huellitasvet.ui.theme.GrisBorde
import java.time.LocalDate

@Composable
fun FechaChip(
    fecha: LocalDate,
    seleccionada: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(12.dp)
    val colores = MaterialTheme.colorScheme
    Column(
        modifier = modifier
            .height(52.dp)
            .clip(forma)
            .background(if (seleccionada) colores.primary else Color.White)
            .border(
                border = if (seleccionada) BorderStroke(0.dp, Color.Transparent) else BorderStroke(1.dp, GrisBorde),
                shape = forma
            )
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        Text(
            text = Horarios.nombreDia(fecha),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (seleccionada) Color.White else colores.onSurfaceVariant
        )
        Text(
            text = fecha.dayOfMonth.toString(),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (seleccionada) Color.White else colores.onBackground
        )
    }
}