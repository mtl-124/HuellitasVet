package com.lajara.huellitasvet.ui.dueno

import com.lajara.huellitasvet.ui.theme.tarjeta
import com.lajara.huellitasvet.ui.theme.menta
import com.lajara.huellitasvet.ui.theme.bordeSuave
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lajara.huellitasvet.data.local.entity.Servicio
import com.lajara.huellitasvet.model.Horarios
import com.lajara.huellitasvet.ui.components.BarraTitulo
import com.lajara.huellitasvet.ui.components.graficos.CirculoMascota
import com.lajara.huellitasvet.ui.components.graficos.FechaChip
import com.lajara.huellitasvet.ui.components.graficos.HoraChip
import com.lajara.huellitasvet.ui.theme.GrisBorde
import com.lajara.huellitasvet.ui.theme.MentaTarjeta
import com.lajara.huellitasvet.ui.theme.ambar
import com.lajara.huellitasvet.ui.theme.ambarTexto
import com.lajara.huellitasvet.viewmodel.PedirCitaViewModel
import java.time.LocalDate
import java.util.Locale

@Composable
fun PedirCitaScreen(
    viewModel: PedirCitaViewModel,
    onVolver: () -> Unit,
    onCitaAgendada: () -> Unit
) {
    val colores = MaterialTheme.colorScheme
    val mascotas by viewModel.mascotas.collectAsState()
    val servicios by viewModel.servicios.collectAsState()
    val mascotaId by viewModel.mascotaId.collectAsState()
    val servicioId by viewModel.servicioId.collectAsState()
    val fecha by viewModel.fecha.collectAsState()
    val hora by viewModel.hora.collectAsState()
    val ocupadas by viewModel.horasOcupadas.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()
    val aceptaCitas by viewModel.aceptaCitas.collectAsState()

    val snackbar = remember { SnackbarHostState() }
    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbar.showSnackbar(it)
            viewModel.limpiarMensaje()
        }
    }

    // Por defecto queda elegida la primera mascota
    LaunchedEffect(mascotas) {
        if (mascotaId == null && mascotas.isNotEmpty()) viewModel.elegirMascota(mascotas.first().id)
    }

    val mascotaElegida = mascotas.firstOrNull { it.id == mascotaId }
    val servicioElegido = servicios.firstOrNull { it.id == servicioId }
    val listo = mascotaElegida != null && servicioElegido != null && hora != null

    Scaffold(
        containerColor = colores.background,
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = { BarraTitulo(titulo = "Pedir cita", onVolver = onVolver) },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                if (!aceptaCitas) {
                    Text(
                        text = "La clínica no está recibiendo citas",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = colores.ambarTexto,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(colores.ambar)
                            .padding(12.dp)
                    )
                    Spacer(Modifier.height(10.dp))
                }
                if (listo) {
                    ResumenCita(
                        mascota = mascotaElegida.nombre,
                        servicio = servicioElegido.nombre,
                        fecha = LocalDate.parse(fecha),
                        hora = hora!!,
                        precio = servicioElegido.precio
                    )
                    Spacer(Modifier.height(12.dp))
                }
                Button(
                    onClick = { viewModel.confirmar(onCitaAgendada) },
                    enabled = listo && aceptaCitas,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colores.primary)
                ) {
                    Text("Confirmar cita", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Titulo("¿Quién viene?", primero = true)
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                mascotas.forEach { m ->
                    val sel = m.id == mascotaId
                    Row(
                        modifier = Modifier
                            .height(46.dp)
                            .clip(RoundedCornerShape(23.dp))
                            .background(if (sel) colores.menta else colores.tarjeta)
                            .border(
                                if (sel) BorderStroke(2.dp, colores.primary) else BorderStroke(1.dp, colores.bordeSuave),
                                RoundedCornerShape(23.dp)
                            )
                            .clickable { viewModel.elegirMascota(m.id) }
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CirculoMascota(especie = m.especie, tamano = 30.dp)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = m.nombre,
                            fontSize = 14.sp,
                            fontWeight = if (sel) FontWeight.Bold else FontWeight.Medium,
                            color = if (sel) colores.primary else colores.onBackground
                        )
                    }
                }
            }

            Titulo("Servicio")
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                servicios.forEach { s ->
                    TarjetaServicio(
                        servicio = s,
                        seleccionado = s.id == servicioId,
                        onClick = { viewModel.elegirServicio(s.id) }
                    )
                }
            }

            Titulo("Fecha")
            val separacion = 8.dp
            val ancho = (LocalConfiguration.current.screenWidthDp.dp - 40.dp - separacion * 4) / 5
            LazyRow(horizontalArrangement = Arrangement.spacedBy(separacion)) {
                items(viewModel.fechas) { f ->
                    FechaChip(
                        fecha = f,
                        seleccionada = f.toString() == fecha,
                        onClick = { viewModel.elegirFecha(f.toString()) },
                        modifier = Modifier.width(ancho)
                    )
                }
            }

            Spacer(Modifier.height(18.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Hora",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colores.onBackground
                )
                Text(
                    text = "Ocupado = gris",
                    fontSize = 11.sp,
                    color = colores.onSurfaceVariant
                )
            }
            Spacer(Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Horarios.todos.chunked(4).forEach { fila ->
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        fila.forEach { h ->
                            val bloqueada = h in ocupadas || Horarios.yaPaso(fecha, h)
                            HoraChip(
                                hora = h,
                                seleccionada = h == hora,
                                deshabilitada = bloqueada,
                                onClick = { viewModel.elegirHora(h) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        repeat(4 - fila.size) { Spacer(Modifier.weight(1f)) }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun Titulo(texto: String, primero: Boolean = false) {
    if (!primero) Spacer(Modifier.height(18.dp))
    Text(
        text = texto,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
    )
    Spacer(Modifier.height(8.dp))
}

@Composable
private fun TarjetaServicio(servicio: Servicio, seleccionado: Boolean, onClick: () -> Unit) {
    val colores = MaterialTheme.colorScheme
    val forma = RoundedCornerShape(12.dp)
    val icono: ImageVector = when (servicio.icono) {
        "syringe" -> Icons.Filled.Vaccines
        "scissors" -> Icons.Filled.ContentCut
        else -> Icons.Filled.Favorite
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .clip(forma)
            .background(if (seleccionado) colores.menta else colores.tarjeta)
            .border(
                if (seleccionado) BorderStroke(2.dp, colores.primary) else BorderStroke(1.dp, colores.bordeSuave),
                forma
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = if (seleccionado) colores.primary else colores.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = servicio.nombre,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = colores.onBackground
            )
            Text(
                text = "${servicio.duracionMin} min",
                fontSize = 11.sp,
                color = colores.onSurfaceVariant
            )
        }
        Text(
            text = "S/ ${servicio.precio.toInt()}",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = colores.primary
        )
    }
}

@Composable
private fun ResumenCita(
    mascota: String,
    servicio: String,
    fecha: LocalDate,
    hora: String,
    precio: Double
) {
    val colores = MaterialTheme.colorScheme
    val textoFecha = "${Horarios.nombreDia(fecha)} ${fecha.dayOfMonth} ${Horarios.nombreMes(fecha)}"
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(colores.menta.copy(alpha = 0.6f))
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Text(
            text = "$mascota · $servicio",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = colores.primary
        )
        Text(
            text = "$textoFecha · $hora · S/ ${String.format(Locale.US, "%.2f", precio)}",
            fontSize = 12.sp,
            color = colores.onSurfaceVariant
        )
    }
}