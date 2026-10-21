package com.lajara.huellitasvet.ui.dueno

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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lajara.huellitasvet.data.local.entity.Mascota
import com.lajara.huellitasvet.data.local.relation.CitaConDetalle
import com.lajara.huellitasvet.ui.components.BarraTitulo
import com.lajara.huellitasvet.ui.components.graficos.CirculoMascota
import com.lajara.huellitasvet.ui.components.graficos.HistorialItem
import com.lajara.huellitasvet.ui.components.graficos.formatoPeso
import com.lajara.huellitasvet.viewmodel.MascotaViewModel
import java.time.LocalDate

private val FondoPestanas = Color(0xFFE6F0EB)
private val MentaChip = Color(0xFFD1FAE5)

@Composable
fun FichaMascotaScreen(
    viewModel: MascotaViewModel,
    mascotaId: Int,
    onVolver: () -> Unit,
    onPedirCita: () -> Unit,
    onEditar: (Int) -> Unit,
    onIrAInicio: () -> Unit = {},
    onIrAMisCitas: () -> Unit = {},
    onIrAPerfil: () -> Unit = {}
) {
    val mascota by remember(mascotaId) { viewModel.mascota(mascotaId) }.collectAsState()
    val historial by remember(mascotaId) { viewModel.historial(mascotaId) }.collectAsState()
    var pestana by rememberSaveable { mutableIntStateOf(1) }
    val colores = MaterialTheme.colorScheme
    val m = mascota

    Scaffold(
        containerColor = colores.background,
        topBar = { BarraTitulo(titulo = m?.nombre ?: "Ficha", onVolver = onVolver) },
        bottomBar = {
            BarraInferiorFicha(
                onInicio = onIrAInicio,
                onCitas = onIrAMisCitas,
                onPerfil = onIrAPerfil
            )
        }
    ) { padding ->
        if (m == null) {
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            CabeceraMascota(m, vacunadoAlDia(historial))

            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onPedirCita,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Filled.EventAvailable, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Pedir cita", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
                OutlinedButton(
                    onClick = { onEditar(m.id) },
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.5.dp, colores.primary),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = colores.primary)
                ) {
                    Icon(Icons.Filled.Edit, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Editar", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(14.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(FondoPestanas)
                    .padding(4.dp)
            ) {
                Pestana("Datos", pestana == 0, Modifier.weight(1f)) { pestana = 0 }
                Pestana("Historial clínico", pestana == 1, Modifier.weight(1f)) { pestana = 1 }
            }

            Spacer(Modifier.height(18.dp))
            if (pestana == 0) {
                TarjetaDatos(m)
            } else if (historial.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .border(1.dp, colores.outlineVariant, RoundedCornerShape(12.dp))
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Aún no tiene atenciones",
                        fontSize = 13.sp,
                        color = colores.onSurfaceVariant
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    historial.forEachIndexed { indice, item ->
                        HistorialItem(item = item, esUltimo = indice == historial.lastIndex)
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
private fun CabeceraMascota(m: Mascota, vacunado: Boolean) {
    val colores = MaterialTheme.colorScheme
    val forma = RoundedCornerShape(16.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(forma)
            .background(Color.White)
            .border(1.dp, colores.outlineVariant, forma)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CirculoMascota(especie = m.especie, tamano = 72.dp)
        Spacer(Modifier.width(14.dp))
        Column {
            Text(m.nombre, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(2.dp))
            Text(
                "${descripcion(m)} · ${textoEdad(m.anioNacimiento)}",
                fontSize = 11.sp,
                color = colores.onSurfaceVariant
            )
            Text(
                "${m.sexo.lowercase().replaceFirstChar { it.uppercase() }} · ${formatoPeso(m.peso)}",
                fontSize = 11.sp,
                color = colores.onSurfaceVariant
            )
            if (vacunado) {
                Spacer(Modifier.height(6.dp))
                Surface(shape = CircleShape, color = MentaChip) {
                    Text(
                        "Vacunado al día",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = colores.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun Pestana(
    texto: String,
    seleccionada: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val colores = MaterialTheme.colorScheme
    val forma = RoundedCornerShape(10.dp)
    Box(
        modifier = modifier
            .height(34.dp)
            .clip(forma)
            .background(if (seleccionada) Color.White else Color.Transparent)
            .then(
                if (seleccionada) Modifier.border(1.dp, colores.outlineVariant, forma) else Modifier
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            texto,
            fontSize = 12.sp,
            fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Medium,
            color = if (seleccionada) colores.primary else colores.onSurfaceVariant
        )
    }
}

@Composable
private fun TarjetaDatos(m: Mascota) {
    val colores = MaterialTheme.colorScheme
    val forma = RoundedCornerShape(12.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(forma)
            .background(Color.White)
            .border(1.dp, colores.outlineVariant, forma)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        FilaDato("Especie", m.especie.lowercase().replaceFirstChar { it.uppercase() })
        HorizontalDivider(color = colores.outlineVariant)
        FilaDato("Raza", m.raza.ifBlank { "—" })
        HorizontalDivider(color = colores.outlineVariant)
        FilaDato("Sexo", m.sexo.lowercase().replaceFirstChar { it.uppercase() })
        HorizontalDivider(color = colores.outlineVariant)
        FilaDato("Año de nacimiento", m.anioNacimiento.toString())
        HorizontalDivider(color = colores.outlineVariant)
        FilaDato("Peso actual", formatoPeso(m.peso))
    }
}

@Composable
private fun FilaDato(etiqueta: String, valor: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(etiqueta, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(valor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun BarraInferiorFicha(
    onInicio: () -> Unit,
    onCitas: () -> Unit,
    onPerfil: () -> Unit
) {
    val colores = MaterialTheme.colorScheme
    val estilo = NavigationBarItemDefaults.colors(
        selectedIconColor = colores.primary,
        selectedTextColor = colores.primary,
        indicatorColor = Color.Transparent,
        unselectedIconColor = colores.onSurfaceVariant,
        unselectedTextColor = colores.onSurfaceVariant
    )
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = false,
            onClick = onInicio,
            colors = estilo,
            icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
            label = { Text("Inicio", fontSize = 10.sp) }
        )
        NavigationBarItem(
            selected = true,
            onClick = {},
            colors = estilo,
            icon = { Icon(Icons.Filled.Pets, contentDescription = "Mascotas") },
            label = { Text("Mascotas", fontSize = 10.sp, fontWeight = FontWeight.Bold) }
        )
        NavigationBarItem(
            selected = false,
            onClick = onCitas,
            colors = estilo,
            icon = { Icon(Icons.Filled.CalendarMonth, contentDescription = "Citas") },
            label = { Text("Citas", fontSize = 10.sp) }
        )
        NavigationBarItem(
            selected = false,
            onClick = onPerfil,
            colors = estilo,
            icon = { Icon(Icons.Filled.Person, contentDescription = "Perfil") },
            label = { Text("Perfil", fontSize = 10.sp) }
        )
    }
}

private fun descripcion(m: Mascota): String {
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

private fun vacunadoAlDia(historial: List<CitaConDetalle>): Boolean = runCatching {
    val limite = LocalDate.now().minusYears(1)
    historial.any {
        it.servicio.nombre.contains("vacun", ignoreCase = true) &&
                !LocalDate.parse(it.cita.fecha).isBefore(limite)
    }
}.getOrDefault(false)