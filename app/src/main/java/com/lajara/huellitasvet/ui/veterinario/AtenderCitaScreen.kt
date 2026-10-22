package com.lajara.huellitasvet.ui.veterinario

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lajara.huellitasvet.data.local.entity.Mascota
import com.lajara.huellitasvet.data.local.relation.CitaConDetalle
import com.lajara.huellitasvet.model.Horarios
import com.lajara.huellitasvet.ui.components.BarraTitulo
import com.lajara.huellitasvet.ui.components.graficos.CirculoMascota
import com.lajara.huellitasvet.ui.components.graficos.VetTextField
import com.lajara.huellitasvet.ui.components.graficos.formatoPeso
import com.lajara.huellitasvet.ui.theme.rojo
import com.lajara.huellitasvet.ui.theme.tarjeta
import com.lajara.huellitasvet.viewmodel.AtenderCitaViewModel
import java.time.LocalDate

private val RojoCorazon = Color(0xFFE11D48)

@Composable
fun AtenderCitaScreen(
    viewModel: AtenderCitaViewModel,
    citaId: Int,
    onVolver: () -> Unit,
    onSalir: () -> Unit
) {
    val colores = MaterialTheme.colorScheme
    val cita by viewModel.cita.collectAsState()
    val errores by viewModel.errores.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()

    var diagnostico by rememberSaveable { mutableStateOf("") }
    var tratamiento by rememberSaveable { mutableStateOf("") }
    var pesoTexto by rememberSaveable { mutableStateOf("") }
    var confirmarCancelar by remember { mutableStateOf(false) }

    LaunchedEffect(citaId) { viewModel.cargar(citaId) }

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
        topBar = { BarraTitulo(titulo = "Atender cita", onVolver = onVolver) }
    ) { padding ->
        val c = cita
        if (c == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Cargando…", color = colores.onSurfaceVariant)
            }
            return@Scaffold
        }

        val mascota = c.mascota.mascota
        val peso = pesoTexto.replace(',', '.').toDoubleOrNull()
        val atendible = viewModel.puedeAtender(c)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(horizontal = 20.dp)
        ) {
            CabeceraCita(c)

            if (!atendible) {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Esta cita ya está ${c.cita.estado.lowercase()}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = colores.rojo
                )
            }

            Spacer(Modifier.height(20.dp))
            Text(
                text = "Registro clínico",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = colores.onBackground
            )
            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.Top
            ) {
                VetTextField(
                    value = pesoTexto,
                    onValueChange = { pesoTexto = it },
                    label = "Peso (kg)",
                    modifier = Modifier.weight(1f),
                    error = errores.peso,
                    keyboardType = KeyboardType.Decimal
                )
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(colores.primaryContainer)
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = null,
                        tint = RojoCorazon,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text("Peso anterior", fontSize = 10.sp, color = colores.onSurfaceVariant)
                        Text(
                            text = formatoPeso(mascota.peso),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = colores.onBackground
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            CampoMultilinea(
                value = diagnostico,
                onValueChange = { diagnostico = it },
                label = "Diagnóstico",
                error = errores.diagnostico
            )

            Spacer(Modifier.height(12.dp))
            CampoMultilinea(
                value = tratamiento,
                onValueChange = { tratamiento = it },
                label = "Tratamiento"
            )

            if (peso != null && peso > 0.0) {
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(colores.primaryContainer.copy(alpha = 0.6f))
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = colores.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Se actualizará el peso de ${mascota.nombre} a ${formatoPeso(peso)}",
                        fontSize = 11.sp,
                        color = colores.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(20.dp))
            Button(
                onClick = { viewModel.registrar(diagnostico, tratamiento, peso, onSalir) },
                enabled = atendible,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Registrar atención", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(10.dp))
            OutlinedButton(
                onClick = { confirmarCancelar = true },
                enabled = atendible,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, colores.rojo),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = colores.rojo)
            ) {
                Text("Cancelar cita", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(24.dp))
        }

        if (confirmarCancelar) {
            AlertDialog(
                onDismissRequest = { confirmarCancelar = false },
                title = { Text("Cancelar cita") },
                text = { Text("¿Seguro que quieres cancelar la cita de ${mascota.nombre}?") },
                confirmButton = {
                    TextButton(onClick = {
                        confirmarCancelar = false
                        viewModel.cancelar(onSalir)
                    }) {
                        Text("Sí, cancelar", color = colores.rojo)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { confirmarCancelar = false }) {
                        Text("No")
                    }
                }
            )
        }
    }
}

@Composable
private fun CabeceraCita(c: CitaConDetalle) {
    val colores = MaterialTheme.colorScheme
    val forma = RoundedCornerShape(16.dp)
    val mascota = c.mascota.mascota

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(forma)
            .background(colores.tarjeta)
            .border(1.dp, colores.outlineVariant, forma)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CirculoMascota(especie = mascota.especie, tamano = 56.dp)
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${mascota.nombre} · ${descripcionMascota(mascota)}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = colores.onBackground
            )
            Text(
                text = "${textoEdad(mascota.anioNacimiento)} · ${capitalizar(mascota.sexo)} · " +
                        "Dueño: ${nombreCorto(c.mascota.dueno.nombre)}",
                fontSize = 11.sp,
                color = colores.onSurfaceVariant
            )
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.MedicalServices,
                    contentDescription = null,
                    tint = colores.primary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "${c.servicio.nombre} · ${diaCorto(c.cita.fecha)} · ${c.cita.hora}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = colores.primary
                )
            }
        }
    }
}

@Composable
private fun CampoMultilinea(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: String? = null
) {
    val colores = MaterialTheme.colorScheme
    var enfocado by remember { mutableStateOf(false) }
    val forma = RoundedCornerShape(10.dp)
    val colorBorde = when {
        error != null -> colores.error
        enfocado -> colores.primary
        else -> colores.outlineVariant
    }

    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(forma)
                .background(colores.tarjeta)
                .border(if (enfocado || error != null) 1.5.dp else 1.dp, colorBorde, forma)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(label, fontSize = 10.sp, color = colores.onSurfaceVariant)
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                minLines = 3,
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = colores.onSurface
                ),
                cursorBrush = SolidColor(colores.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { enfocado = it.isFocused }
            )
        }
        if (error != null) {
            Text(
                text = error,
                color = colores.error,
                fontSize = 11.sp,
                modifier = Modifier.padding(start = 4.dp, top = 3.dp)
            )
        }
    }
}

private fun descripcionMascota(m: Mascota): String {
    val hembra = m.sexo.equals("HEMBRA", ignoreCase = true)
    val base = when (m.especie.uppercase()) {
        "PERRO" -> if (hembra) "Perra" else "Perro"
        "GATO" -> if (hembra) "Gata" else "Gato"
        else -> "Mascota"
    }
    return if (m.raza.isBlank()) base else "$base ${m.raza.lowercase()}"
}

private fun textoEdad(anioNacimiento: Int): String {
    val edad = LocalDate.now().year - anioNacimiento
    return when {
        edad <= 0 -> "Menos de 1 año"
        edad == 1 -> "1 año"
        else -> "$edad años"
    }
}

private fun capitalizar(texto: String): String =
    texto.lowercase().replaceFirstChar { it.uppercase() }

// "2026-10-21" -> "Mié 21"
private fun diaCorto(fecha: String): String = runCatching {
    val d = LocalDate.parse(fecha)
    "${Horarios.nombreDia(d)} ${d.dayOfMonth}"
}.getOrDefault(fecha)

// "Ana Gómez" -> "Ana G."
private fun nombreCorto(nombre: String): String {
    val partes = nombre.trim().split(" ").filter { it.isNotBlank() }
    return if (partes.size >= 2) "${partes[0]} ${partes[1].first()}." else nombre
}