package com.lajara.huellitasvet.ui.veterinario

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lajara.huellitasvet.data.local.relation.MascotaConDueno
import com.lajara.huellitasvet.ui.components.BarraTitulo
import com.lajara.huellitasvet.ui.components.graficos.CirculoMascota
import com.lajara.huellitasvet.ui.components.graficos.formatoDiaMes
import com.lajara.huellitasvet.ui.theme.bordeSuave
import com.lajara.huellitasvet.ui.theme.tarjeta
import com.lajara.huellitasvet.viewmodel.FiltroEspecie
import com.lajara.huellitasvet.viewmodel.PacientesViewModel
import java.time.LocalDate

@Composable
fun PacientesScreen(
    viewModel: PacientesViewModel,
    onVolver: () -> Unit,
    onIrAFicha: (Int) -> Unit,
    onIrAPanel: () -> Unit,
    onIrAAgenda: () -> Unit,
    onIrAServicios: () -> Unit,
    onIrAAjustes: () -> Unit
) {
    val colores = MaterialTheme.colorScheme
    val pacientes by viewModel.pacientes.collectAsState()
    val ultimasVisitas by viewModel.ultimasVisitas.collectAsState()
    val busqueda by viewModel.busqueda.collectAsState()
    val filtro by viewModel.filtro.collectAsState()

    Scaffold(
        containerColor = colores.background,
        topBar = { BarraTitulo(titulo = "Pacientes", onVolver = onVolver) },
        bottomBar = {
            BarraInferiorVet(
                seleccion = 2,
                onPanel = onIrAPanel,
                onAgenda = onIrAAgenda,
                onPacientes = {},
                onServicios = onIrAServicios,
                onAjustes = onIrAAjustes
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Buscador(valor = busqueda, onCambio = viewModel::buscar)

            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FiltroEspecie.entries.forEach { opcion ->
                    ChipFiltro(
                        texto = opcion.etiqueta,
                        seleccionado = opcion == filtro,
                        onClick = { viewModel.elegirFiltro(opcion) }
                    )
                }
            }

            Spacer(Modifier.height(14.dp))
            if (pacientes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No se encontraron pacientes",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = colores.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(pacientes, key = { it.mascota.id }) { paciente ->
                        TarjetaPaciente(
                            paciente = paciente,
                            ultimaVisita = ultimasVisitas[paciente.mascota.id],
                            onClick = { onIrAFicha(paciente.mascota.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Buscador(valor: String, onCambio: (String) -> Unit) {
    val colores = MaterialTheme.colorScheme
    val forma = RoundedCornerShape(12.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .clip(forma)
            .background(colores.tarjeta)
            .border(1.dp, colores.bordeSuave, forma)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = null,
            tint = colores.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(8.dp))
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
            if (valor.isEmpty()) {
                Text(
                    text = "Buscar mascota o dueño…",
                    fontSize = 13.sp,
                    color = colores.onSurfaceVariant
                )
            }
            BasicTextField(
                value = valor,
                onValueChange = onCambio,
                singleLine = true,
                textStyle = TextStyle(fontSize = 14.sp, color = colores.onSurface),
                cursorBrush = SolidColor(colores.primary),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ChipFiltro(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    val colores = MaterialTheme.colorScheme
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
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (seleccionado) colores.onPrimary else colores.onSurfaceVariant
        )
    }
}

@Composable
private fun TarjetaPaciente(
    paciente: MascotaConDueno,
    ultimaVisita: String?,
    onClick: () -> Unit
) {
    val colores = MaterialTheme.colorScheme
    val forma = RoundedCornerShape(14.dp)
    val m = paciente.mascota
    val detalle = if (m.raza.isBlank()) textoEdad(m.anioNacimiento)
    else "${m.raza} · ${textoEdad(m.anioNacimiento)}"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(forma)
            .background(colores.tarjeta)
            .border(1.dp, colores.bordeSuave, forma)
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CirculoMascota(especie = m.especie, tamano = 56.dp)
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = m.nombre,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = colores.onBackground
            )
            Text(text = detalle, fontSize = 11.sp, color = colores.onSurfaceVariant)
            Text(
                text = "Dueño: ${nombreCorto(paciente.dueno.nombre)}",
                fontSize = 11.sp,
                color = colores.onSurfaceVariant
            )
            Text(
                text = if (ultimaVisita != null) "Última visita: ${formatoDiaMes(ultimaVisita)}"
                else "Sin visitas",
                fontSize = 10.sp,
                color = colores.onSurfaceVariant.copy(alpha = 0.8f)
            )
        }
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = colores.onSurfaceVariant
        )
    }
}

private fun textoEdad(anioNacimiento: Int): String {
    val edad = LocalDate.now().year - anioNacimiento
    return when {
        edad <= 0 -> "Menos de 1 año"
        edad == 1 -> "1 año"
        else -> "$edad años"
    }
}

// "Ana Gómez" -> "Ana G."
private fun nombreCorto(nombre: String): String {
    val partes = nombre.trim().split(" ").filter { it.isNotBlank() }
    return if (partes.size >= 2) "${partes[0]} ${partes[1].first()}." else nombre
}