package com.lajara.huellitasvet.ui.veterinario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lajara.huellitasvet.data.local.entity.Servicio
import com.lajara.huellitasvet.ui.components.BarraTitulo
import com.lajara.huellitasvet.ui.theme.bordeSuave
import com.lajara.huellitasvet.ui.theme.menta
import com.lajara.huellitasvet.ui.theme.tarjeta
import com.lajara.huellitasvet.viewmodel.ServiciosViewModel
import java.util.Locale

@Composable
fun FormularioServicioScreen(
    viewModel: ServiciosViewModel,
    servicioId: Int?,
    onVolver: () -> Unit
) {
    val colores = MaterialTheme.colorScheme
    val servicios by viewModel.servicios.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()

    val snackbar = remember { SnackbarHostState() }
    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbar.showSnackbar(it)
            viewModel.limpiarMensaje()
        }
    }

    var nombre by remember { mutableStateOf("") }
    var duracion by remember { mutableIntStateOf(15) }
    var precio by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var icono by remember { mutableStateOf("heart") }
    var activo by remember { mutableStateOf(true) }
    var intentoGuardar by remember { mutableStateOf(false) }
    var cargado by remember { mutableStateOf(false) }

    // Al editar, se cargan los datos del servicio una sola vez
    val existente = servicios.firstOrNull { it.id == servicioId }
    LaunchedEffect(existente) {
        if (existente != null && !cargado) {
            nombre = existente.nombre
            duracion = existente.duracionMin
            precio = String.format(Locale.US, "%.2f", existente.precio)
            descripcion = existente.descripcion
            icono = existente.icono
            activo = existente.activo
            cargado = true
        }
    }

    val precioNum = precio.replace(',', '.').toDoubleOrNull() ?: 0.0
    val errorNombre = intentoGuardar && nombre.isBlank()
    val errorPrecio = intentoGuardar && precioNum <= 0.0

    fun guardar() {
        intentoGuardar = true
        if (nombre.isBlank() || precioNum <= 0.0) return
        viewModel.guardar(
            Servicio(
                id = servicioId ?: 0,
                nombre = nombre.trim(),
                descripcion = descripcion.trim(),
                duracionMin = duracion,
                precio = precioNum,
                activo = activo,
                icono = icono
            ),
            onVolver
        )
    }

    Scaffold(
        containerColor = colores.background,
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = {
            BarraTitulo(
                titulo = if (servicioId == null) "Nuevo servicio" else "Editar servicio",
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
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del servicio") },
                singleLine = true,
                isError = errorNombre,
                supportingText = { if (errorNombre) Text("El nombre es obligatorio") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(14.dp))
            Text(
                text = "Duración",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = colores.onBackground
            )
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(15, 30, 45, 60).forEach { min ->
                    val sel = duracion == min
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .clip(CircleShape)
                            .background(if (sel) colores.primary else colores.tarjeta)
                            .border(
                                1.dp,
                                if (sel) colores.primary else colores.bordeSuave,
                                CircleShape
                            )
                            .clickable { duracion = min },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$min min",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (sel) colores.onPrimary else colores.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(Modifier.height(14.dp))
            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio (S/)") },
                singleLine = true,
                isError = errorPrecio,
                supportingText = { if (errorPrecio) Text("El precio debe ser mayor a 0") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                minLines = 3,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(14.dp))
            Text(
                text = "Ícono",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = colores.onBackground
            )
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf("heart", "syringe", "scissors").forEach { clave ->
                    val sel = icono == clave
                    val forma = RoundedCornerShape(12.dp)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .clip(forma)
                            .background(if (sel) colores.menta else colores.tarjeta)
                            .border(
                                if (sel) 2.dp else 1.dp,
                                if (sel) colores.primary else colores.bordeSuave,
                                forma
                            )
                            .clickable { icono = clave },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = iconoServicioVet(clave),
                            contentDescription = null,
                            tint = if (sel) colores.primary else colores.onSurfaceVariant,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(14.dp))
            val formaActivo = RoundedCornerShape(14.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(formaActivo)
                    .background(colores.tarjeta)
                    .border(1.dp, colores.bordeSuave, formaActivo)
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Servicio activo",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = colores.onBackground
                    )
                    Text(
                        text = "Los dueños podrán pedirlo",
                        fontSize = 11.sp,
                        color = colores.onSurfaceVariant
                    )
                }
                Switch(checked = activo, onCheckedChange = { activo = it })
            }

            Spacer(Modifier.height(20.dp))
            Button(
                onClick = { guardar() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colores.primary)
            ) {
                Text("Guardar servicio", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(10.dp))
            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.5.dp, colores.primary)
            ) {
                Text(
                    text = "Cancelar",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colores.primary
                )
            }

            // Solo al editar
            if (existente != null) {
                Spacer(Modifier.height(6.dp))
                TextButton(
                    onClick = { viewModel.eliminar(existente, onVolver) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Eliminar servicio", color = MaterialTheme.colorScheme.error)
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}