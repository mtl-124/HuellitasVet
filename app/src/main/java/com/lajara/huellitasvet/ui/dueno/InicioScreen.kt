package com.lajara.huellitasvet.ui.dueno

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lajara.huellitasvet.data.local.relation.CitaConDetalle
import com.lajara.huellitasvet.ui.components.graficos.CirculoMascota
import com.lajara.huellitasvet.ui.theme.MentaBorde
import com.lajara.huellitasvet.ui.theme.MentaTarjeta
import com.lajara.huellitasvet.viewmodel.InicioViewModel
import java.time.LocalDate

@Composable
fun InicioScreen(
    viewModel: InicioViewModel,
    onIrAMascotas: () -> Unit,
    onIrANuevaCita: () -> Unit,
    onIrAMisCitas: () -> Unit,
    onIrAAsistenteIa: () -> Unit = {},
    onCerrarSesion: () -> Unit = {},
    onIrAPerfil: () -> Unit = {},
    onIrAFicha: (Int) -> Unit = {},
    onAgregarMascota: () -> Unit = {},
    nombreUsuario: String = ""
) {
    val mascotas by viewModel.mascotas.collectAsState()
    val citas by viewModel.citas.collectAsState()
    val usuario by viewModel.usuario.collectAsState()
    val nombrePila = usuario?.nombre?.substringBefore(" ").orEmpty()

    val proxima = remember(citas) {
        val hoy = LocalDate.now().toString()
        citas
            .filter { (it.cita.estado == "CONFIRMADA" || it.cita.estado == "PENDIENTE") && it.cita.fecha >= hoy }
            .minWithOrNull(compareBy<CitaConDetalle>({ it.cita.fecha }, { it.cita.hora }))
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            BarraInferiorInicio(
                onMascotas = onIrAMascotas,
                onCitas = onIrAMisCitas,
                onPerfil = onIrAPerfil
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (nombrePila.isBlank()) "Hola" else "Hola, $nombrePila",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "¿Cómo están tus mascotas hoy?",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = {}) {
                    Icon(
                        Icons.Filled.Notifications,
                        contentDescription = "Notificaciones",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Spacer(Modifier.height(14.dp))
            TarjetaProximaCita(proxima)

            Spacer(Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Mis mascotas",
                    modifier = Modifier.weight(1f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Ver todas",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable { onIrAMascotas() }
                        .padding(end = 6.dp)
                )
            }

            Spacer(Modifier.height(20.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(mascotas) { mascota ->
                    TarjetaMascotaInicio(
                        nombre = mascota.nombre,
                        especie = mascota.especie,
                        onClick = { onIrAFicha(mascota.id) }
                    )
                }
                item { TarjetaAgregar(onClick = onAgregarMascota) }
            }

            Spacer(Modifier.height(24.dp))
            Text("Servicios", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                TarjetaServicio(Icons.Filled.Favorite, "Consulta", Modifier.weight(1f), onIrANuevaCita)
                TarjetaServicio(Icons.Filled.Vaccines, "Vacunas", Modifier.weight(1f), onIrANuevaCita)
                TarjetaServicio(Icons.Filled.ContentCut, "Baño", Modifier.weight(1f), onIrANuevaCita)
            }

            if (proxima != null) {
                Spacer(Modifier.height(16.dp))
                TarjetaRecordatorio(proxima)
            }

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onIrANuevaCita,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Pedir una cita", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun BarraInferiorInicio(
    onMascotas: () -> Unit,
    onCitas: () -> Unit,
    onPerfil: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
            label = { Text("Inicio", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
        )
        NavigationBarItem(
            selected = false,
            onClick = onMascotas,
            icon = { Icon(Icons.Filled.Pets, contentDescription = "Mascotas") },
            label = { Text("Mascotas", fontSize = 12.sp, fontWeight = FontWeight.Medium) }
        )
        NavigationBarItem(
            selected = false,
            onClick = onCitas,
            icon = { Icon(Icons.Filled.CalendarMonth, contentDescription = "Citas") },
            label = { Text("Citas", fontSize = 12.sp, fontWeight = FontWeight.Medium) }
        )
        NavigationBarItem(
            selected = false,
            onClick = onPerfil,
            icon = { Icon(Icons.Filled.Person, contentDescription = "Perfil") },
            label = { Text("Perfil", fontSize = 12.sp, fontWeight = FontWeight.Medium) }
        )
    }
}

@Composable
private fun TarjetaProximaCita(proxima: CitaConDetalle?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (proxima == null) {
            Text(
                "No tienes citas agendadas",
                modifier = Modifier.weight(1f),
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            return@Row
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                "${proxima.mascota.mascota.nombre} - ${proxima.servicio.nombre}",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.Schedule,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    "${formatoFecha(proxima.cita.fecha)} - ${proxima.cita.hora}",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(8.dp))
            Surface(shape = CircleShape, color = Color.White) {
                Text(
                    proxima.cita.estado.lowercase().replaceFirstChar { it.uppercase() },
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            CirculoMascota(especie = proxima.mascota.mascota.especie, tamano = 44.dp)
        }
    }
}

@Composable
private fun TarjetaMascotaInicio(nombre: String, especie: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.width(84.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CirculoMascota(especie = especie, tamano = 48.dp)
            Spacer(Modifier.height(8.dp))
            Text(nombre, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(
                "Ver ficha",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun TarjetaAgregar(onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .width(84.dp)
            .height(82.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Filled.Add,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(30.dp)
            )
            Text(
                "Agregar",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun TarjetaServicio(
    icono: ImageVector,
    texto: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MentaTarjeta),
        border = BorderStroke(1.dp, MentaBorde)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icono,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(22.dp)
            )
            Spacer(Modifier.height(6.dp))
            Text(texto, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun TarjetaRecordatorio(cita: CitaConDetalle) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Filled.Notifications,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = Modifier.size(22.dp)
        )
        Spacer(Modifier.width(14.dp))
        Column {
            Text(
                "Recordatorio",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Text(
                "${cita.mascota.mascota.nombre}: ${cita.servicio.nombre.lowercase()} el ${formatoFecha(cita.cita.fecha)}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
    }
}

private fun formatoFecha(fecha: String): String = runCatching {
    val d = LocalDate.parse(fecha)
    val dias = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")
    val meses = listOf("ene", "feb", "mar", "abr", "may", "jun", "jul", "ago", "sep", "oct", "nov", "dic")
    "${dias[d.dayOfWeek.value - 1]} ${d.dayOfMonth} ${meses[d.monthValue - 1]}"
}.getOrDefault(fecha)