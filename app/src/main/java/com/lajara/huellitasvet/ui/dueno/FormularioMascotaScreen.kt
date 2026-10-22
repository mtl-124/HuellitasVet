package com.lajara.huellitasvet.ui.dueno

import com.lajara.huellitasvet.ui.theme.tarjeta
import com.lajara.huellitasvet.ui.theme.menta
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lajara.huellitasvet.data.local.entity.Mascota
import com.lajara.huellitasvet.model.Especie
import com.lajara.huellitasvet.model.Sexo
import com.lajara.huellitasvet.ui.components.BarraTitulo
import com.lajara.huellitasvet.ui.components.graficos.CirculoMascota
import com.lajara.huellitasvet.ui.components.graficos.VetTextField
import com.lajara.huellitasvet.viewmodel.MascotaViewModel

private val MentaSeleccion = Color(0xFFD1FAE5)

@Composable
fun FormularioMascotaScreen(
    viewModel: MascotaViewModel,
    mascotaId: Int? = null,
    onVolver: () -> Unit,
    onGuardado: () -> Unit,
    onEliminada: () -> Unit = onGuardado
) {
    val errores by viewModel.errores.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()
    val existente by remember(mascotaId) { viewModel.mascota(mascotaId ?: 0) }.collectAsState()
    val esEdicion = mascotaId != null
    val colores = MaterialTheme.colorScheme

    var nombre by rememberSaveable { mutableStateOf("") }
    var especie by rememberSaveable { mutableStateOf<String?>(null) }
    var raza by rememberSaveable { mutableStateOf("") }
    var anio by rememberSaveable { mutableStateOf("") }
    var peso by rememberSaveable { mutableStateOf("") }
    var sexo by rememberSaveable { mutableStateOf<String?>(null) }
    var cargado by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(existente) {
        val m = existente
        if (m != null && !cargado) {
            nombre = m.nombre
            especie = m.especie
            raza = m.raza
            anio = m.anioNacimiento.toString()
            peso = m.peso.toString()
            sexo = m.sexo
            cargado = true
        }
    }

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
        topBar = {
            BarraTitulo(
                titulo = if (esEdicion) "Editar mascota" else "Nueva mascota",
                onVolver = onVolver
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 4.dp)
            ) {
                CirculoMascota(especie = especie ?: "", tamano = 96.dp)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 4.dp, y = 4.dp)
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(colores.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
            VetTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = "Nombre",
                error = errores.nombre
            )

            Spacer(Modifier.height(14.dp))
            Text("Especie", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Especie.entries.forEach { opcion ->
                    OpcionChip(
                        texto = opcion.name.lowercase().replaceFirstChar { it.uppercase() },
                        seleccionado = especie == opcion.name,
                        solido = true,
                        modifier = Modifier.weight(1f),
                        onClick = { especie = opcion.name }
                    )
                }
            }
            errores.especie?.let { ErrorCampo(it) }

            Spacer(Modifier.height(14.dp))
            VetTextField(
                value = raza,
                onValueChange = { raza = it },
                label = "Raza"
            )

            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                VetTextField(
                    value = anio,
                    onValueChange = { anio = it.filter(Char::isDigit).take(4) },
                    label = "Año de nacimiento",
                    modifier = Modifier.weight(1f),
                    error = errores.anio,
                    keyboardType = KeyboardType.Number
                )
                VetTextField(
                    value = peso,
                    onValueChange = { peso = it },
                    label = "Peso (kg)",
                    modifier = Modifier.weight(1f),
                    error = errores.peso,
                    keyboardType = KeyboardType.Decimal
                )
            }

            Spacer(Modifier.height(16.dp))
            Text("Sexo", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Sexo.entries.forEach { opcion ->
                    OpcionChip(
                        texto = opcion.name.lowercase().replaceFirstChar { it.uppercase() },
                        seleccionado = sexo == opcion.name,
                        solido = false,
                        modifier = Modifier.weight(1f),
                        onClick = { sexo = opcion.name }
                    )
                }
            }
            errores.sexo?.let { ErrorCampo(it) }

            Spacer(Modifier.height(28.dp))
            Button(
                onClick = {
                    if (esEdicion && existente == null) return@Button
                    viewModel.guardar(
                        Mascota(
                            id = existente?.id ?: 0,
                            duenoId = existente?.duenoId ?: 0,
                            nombre = nombre,
                            especie = especie ?: "",
                            raza = raza,
                            anioNacimiento = anio.toIntOrNull() ?: 0,
                            peso = peso.replace(',', '.').toDoubleOrNull() ?: 0.0,
                            sexo = sexo ?: ""
                        )
                    ) { onGuardado() }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Guardar mascota", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(12.dp))
            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.5.dp, colores.primary),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = colores.primary)
            ) {
                Text("Cancelar", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }

            if (esEdicion) {
                Spacer(Modifier.height(4.dp))
                TextButton(
                    onClick = { existente?.let { viewModel.eliminar(it) { onEliminada() } } },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        "Eliminar mascota",
                        color = colores.error,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun OpcionChip(
    texto: String,
    seleccionado: Boolean,
    solido: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val colores = MaterialTheme.colorScheme
    val forma = RoundedCornerShape(10.dp)
    val fondo = when {
        !seleccionado -> colores.tarjeta
        solido -> colores.primary
        else -> colores.menta
    }
    val colorBorde = if (seleccionado) colores.primary else colores.outlineVariant
    val colorTexto = when {
        !seleccionado -> colores.onSurfaceVariant
        solido -> colores.onPrimary
        else -> colores.primary
    }
    Box(
        modifier = modifier
            .height(40.dp)
            .clip(forma)
            .background(fondo)
            .border(if (seleccionado && !solido) 1.5.dp else 1.dp, colorBorde, forma)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            texto,
            color = colorTexto,
            fontSize = 13.sp,
            fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
private fun ErrorCampo(texto: String) {
    Text(
        text = texto,
        color = MaterialTheme.colorScheme.error,
        fontSize = 11.sp,
        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
    )
}