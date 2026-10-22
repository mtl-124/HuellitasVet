package com.lajara.huellitasvet.ui.veterinario

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
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
fun ServiciosScreen(
    viewModel: ServiciosViewModel,
    onVolver: () -> Unit,
    onNuevo: () -> Unit,
    onEditar: (Int) -> Unit,
    onIrAPanel: () -> Unit,
    onIrAAgenda: () -> Unit,
    onIrAPacientes: () -> Unit,
    onIrAAjustes: () -> Unit
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

    Scaffold(
        containerColor = colores.background,
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = { BarraTitulo(titulo = "Servicios", onVolver = onVolver) },
        bottomBar = {
            BarraInferiorVet(
                seleccion = 3,
                onPanel = onIrAPanel,
                onAgenda = onIrAAgenda,
                onPacientes = onIrAPacientes,
                onServicios = {},
                onAjustes = onIrAAjustes
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNuevo,
                containerColor = colores.primary,
                contentColor = colores.onPrimary,
                shape = RoundedCornerShape(18.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Nuevo servicio")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 90.dp)
        ) {
            items(servicios, key = { it.id }) { s ->
                ServicioItem(
                    servicio = s,
                    onToggle = { viewModel.alternar(s) },
                    onEditar = { onEditar(s.id) }
                )
            }
        }
    }
}

@Composable
private fun ServicioItem(
    servicio: Servicio,
    onToggle: () -> Unit,
    onEditar: () -> Unit
) {
    val colores = MaterialTheme.colorScheme
    val forma = RoundedCornerShape(16.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(forma)
            .background(colores.tarjeta)
            .border(1.dp, colores.bordeSuave, forma)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(colores.menta),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = iconoServicioVet(servicio.icono),
                contentDescription = null,
                tint = colores.primary,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = servicio.nombre,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = colores.onBackground
            )
            Text(
                text = "${servicio.duracionMin} min · " +
                        String.format(Locale.US, "S/ %.2f", servicio.precio),
                fontSize = 11.sp,
                color = colores.onSurfaceVariant
            )
        }
        Switch(checked = servicio.activo, onCheckedChange = { onToggle() })
        IconButton(onClick = onEditar) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Editar",
                tint = colores.primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

internal fun iconoServicioVet(clave: String): ImageVector = when (clave) {
    "syringe" -> Icons.Filled.Vaccines
    "scissors" -> Icons.Filled.ContentCut
    else -> Icons.Filled.Favorite
}