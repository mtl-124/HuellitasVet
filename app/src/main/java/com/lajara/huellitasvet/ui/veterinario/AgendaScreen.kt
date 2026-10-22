package com.lajara.huellitasvet.ui.veterinario

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lajara.huellitasvet.data.local.relation.CitaConDetalle
import com.lajara.huellitasvet.model.EstadoCita
import com.lajara.huellitasvet.model.Horarios
import com.lajara.huellitasvet.ui.components.BarraTitulo
import com.lajara.huellitasvet.ui.components.graficos.CirculoMascota
import com.lajara.huellitasvet.ui.components.graficos.EstadoCitaChip
import com.lajara.huellitasvet.ui.theme.bordeSuave
import com.lajara.huellitasvet.ui.theme.tarjeta
import com.lajara.huellitasvet.viewmodel.AgendaViewModel
import java.time.LocalDate

@Composable
fun AgendaScreen(
    viewModel: AgendaViewModel,
    onVolver: () -> Unit,
    onAtender: (Int) -> Unit,
    onIrAPanel: () -> Unit,
    onIrAPacientes: () -> Unit,
    onIrAServicios: () -> Unit,
    onIrAAjustes: () -> Unit
) {
    val colores = MaterialTheme.colorScheme
    val citas by viewModel.citas.collectAsState()
    val fechaSeleccionada by viewModel.fechaSeleccionada.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()

    val snackbar = remember { SnackbarHostState() }
    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbar.showSnackbar(it)
            viewModel.limpiarMensaje()
        }
    }

    Scaffold(
        containerColor = colores.background,
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = { BarraTitulo(titulo = "Agenda", onVolver = onVolver) },
        bottomBar = {
            BarraInferiorVet(
                seleccion = 1,
                onPanel = onIrAPanel,
                onAgenda = {},
                onPacientes = onIrAPacientes,
                onServicios = onIrAServicios,
                onAjustes = onIrAAjustes
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                viewModel.dias.forEach { dia ->
                    ChipDia(
                        fecha = dia,
                        seleccionado = dia == fechaSeleccionada,
                        onClick = { viewModel.seleccionarFecha(dia) }
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            if (citas.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay citas para este día",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = colores.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp)
                ) {
                    items(citas, key = { it.cita.id }) { item ->
                        FilaAgenda(
                            item = item,
                            onConfirmar = { viewModel.confirmar(item) },
                            onAtender = { onAtender(item.cita.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChipDia(
    fecha: LocalDate,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    val colores = MaterialTheme.colorScheme
    val etiqueta = if (fecha == LocalDate.now()) {
        "Hoy ${fecha.dayOfMonth}"
    } else {
        "${Horarios.nombreDia(fecha)} ${fecha.dayOfMonth}"
    }
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(if (seleccionado) colores.primary else colores.tarjeta)
            .border(
                width = 1.dp,
                color = if (seleccionado) colores.primary else colores.bordeSuave,
                shape = CircleShape
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = etiqueta,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (seleccionado) colores.onPrimary else colores.onSurfaceVariant
        )
    }
}

@Composable
private fun FilaAgenda(
    item: CitaConDetalle,
    onConfirmar: () -> Unit,
    onAtender: () -> Unit
) {
    val colores = MaterialTheme.colorScheme
    val cita = item.cita
    val forma = RoundedCornerShape(14.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Text(
            text = cita.hora,
            modifier = Modifier
                .width(48.dp)
                .padding(top = 14.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = colores.primary
        )

        // Línea de tiempo
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
                .background(colores.bordeSuave)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp, bottom = 12.dp)
                .clip(forma)
                .background(colores.tarjeta)
                .border(1.dp, colores.bordeSuave, forma)
                .padding(12.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                CirculoMascota(
                    especie = item.mascota.mascota.especie,
                    tamano = 40.dp
                )
                Spacer(Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${item.mascota.mascota.nombre} · ${item.servicio.nombre}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = colores.onBackground
                    )
                    Text(
                        text = "Dueño: ${nombreCorto(item.mascota.dueno.nombre)}",
                        fontSize = 11.sp,
                        color = colores.onSurfaceVariant
                    )
                    Spacer(Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        EstadoCitaChip(estado = cita.estado)
                        when (cita.estado) {
                            EstadoCita.PENDIENTE.name -> BotonAccion("Confirmar", onConfirmar)
                            EstadoCita.CONFIRMADA.name -> BotonAccion("Atender", onAtender)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BotonAccion(texto: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.height(32.dp),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp)
    ) {
        Text(texto, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

// "Ana Gómez" -> "Ana G."
private fun nombreCorto(nombre: String): String {
    val partes = nombre.trim().split(" ").filter { it.isNotBlank() }
    return if (partes.size >= 2) "${partes[0]} ${partes[1].first()}." else nombre
}