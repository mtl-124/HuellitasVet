package com.lajara.huellitasvet.ui.dueno

import com.lajara.huellitasvet.ui.theme.tarjeta
import com.lajara.huellitasvet.ui.theme.menta
import com.lajara.huellitasvet.ui.theme.bordeSuave
import com.lajara.huellitasvet.ui.theme.rojo
import com.lajara.huellitasvet.ui.theme.ambar
import com.lajara.huellitasvet.ui.theme.ambarBorde
import com.lajara.huellitasvet.ui.theme.ambarTexto
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lajara.huellitasvet.data.local.relation.CitaConDetalle
import com.lajara.huellitasvet.ui.components.BarraTitulo
import com.lajara.huellitasvet.ui.components.CitaCard
import com.lajara.huellitasvet.ui.theme.AmbarBorde
import com.lajara.huellitasvet.ui.theme.AmbarContenedor
import com.lajara.huellitasvet.ui.theme.AmbarTexto
import com.lajara.huellitasvet.ui.theme.GrisBorde
import com.lajara.huellitasvet.ui.theme.MentaTarjeta
import com.lajara.huellitasvet.viewmodel.MisCitasViewModel

@Composable
fun MisCitasScreen(
    viewModel: MisCitasViewModel,
    onVolver: () -> Unit,
    onNuevaCita: () -> Unit = {},
    onIrAInicio: () -> Unit = {},
    onIrAMascotas: () -> Unit = {},
    onIrAPerfil: () -> Unit = {}
) {
    val colores = MaterialTheme.colorScheme
    val proximas by viewModel.proximas.collectAsState()
    val historial by viewModel.historial.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()
    // Se lee para recomponer cuando pasa el tiempo
    viewModel.ahora.collectAsState().value

    var pestana by rememberSaveable { mutableStateOf(0) }
    var citaACancelar by remember { mutableStateOf<CitaConDetalle?>(null) }

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
        topBar = { BarraTitulo(titulo = "Mis citas", onVolver = onVolver) },
        bottomBar = {
            BarraInferiorInicio(
                seleccion = 2,
                onInicio = onIrAInicio,
                onMascotas = onIrAMascotas,
                onCitas = {},
                onPerfil = onIrAPerfil
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNuevaCita,
                shape = RoundedCornerShape(18.dp),
                containerColor = colores.primary,
                contentColor = colores.onPrimary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Pedir cita", modifier = Modifier.size(28.dp))
            }
        }
    ) { padding ->
        androidx.compose.foundation.layout.Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            SelectorPestanas(
                seleccionada = pestana,
                onElegir = { pestana = it }
            )
            Spacer(Modifier.height(14.dp))

            val lista = if (pestana == 0) proximas else historial
            if (lista.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (pestana == 0) "No tienes citas próximas" else "Aún no tienes historial de citas",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = colores.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(lista, key = { it.cita.id }) { cita ->
                        val cancelable = pestana == 0 &&
                                (cita.cita.estado == "PENDIENTE" || cita.cita.estado == "CONFIRMADA")
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .border(BorderStroke(1.dp, colores.bordeSuave), RoundedCornerShape(14.dp))
                        ) {
                            CitaCard(
                                citaConDetalle = cita,
                                onCancelar = if (cancelable) {
                                    {
                                        if (viewModel.puedeCancelar(cita)) citaACancelar = cita
                                        else viewModel.cancelar(cita)
                                    }
                                } else null
                            )
                        }
                    }
                    if (pestana == 0) {
                        item { AvisoCancelacion() }
                    }
                    item { Spacer(Modifier.height(88.dp)) }
                }
            }
        }
    }

    citaACancelar?.let { cita ->
        AlertDialog(
            onDismissRequest = { citaACancelar = null },
            title = { Text("Cancelar cita", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "¿Seguro que quieres cancelar la cita de ${cita.mascota.mascota.nombre} " +
                            "(${cita.servicio.nombre}) del ${cita.cita.fecha} a las ${cita.cita.hora}?"
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.cancelar(cita)
                    citaACancelar = null
                }) {
                    Text("Sí, cancelar", color = colores.rojo, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { citaACancelar = null }) { Text("No") }
            }
        )
    }
}

@Composable
private fun SelectorPestanas(seleccionada: Int, onElegir: (Int) -> Unit) {
    val colores = MaterialTheme.colorScheme
    val forma = RoundedCornerShape(14.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(forma)
            .background(colores.menta)
            .padding(4.dp)
    ) {
        listOf("Próximas", "Historial").forEachIndexed { indice, texto ->
            val activa = indice == seleccionada
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(38.dp)
                    .clip(RoundedCornerShape(11.dp))
                    .background(if (activa) colores.tarjeta else Color.Transparent)
                    .clickable { onElegir(indice) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = texto,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (activa) colores.primary else colores.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun AvisoCancelacion() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.ambar)
            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.ambarBorde), RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Schedule,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.ambarTexto,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = "Puedes cancelar hasta 2 horas antes",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.ambarTexto
        )
    }
}