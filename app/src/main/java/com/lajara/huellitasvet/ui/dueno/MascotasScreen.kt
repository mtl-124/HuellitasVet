package com.lajara.huellitasvet.ui.dueno

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lajara.huellitasvet.data.local.entity.Mascota
import com.lajara.huellitasvet.ui.components.BarraTitulo
import com.lajara.huellitasvet.ui.components.graficos.CirculoMascota
import com.lajara.huellitasvet.ui.theme.MentaBorde
import com.lajara.huellitasvet.ui.theme.MentaTarjeta
import com.lajara.huellitasvet.viewmodel.MascotaViewModel
import java.time.LocalDate

@Composable
fun MascotasScreen(
    viewModel: MascotaViewModel,
    onVolver: () -> Unit,
    onIrAFicha: (Int) -> Unit = {},
    onAgregarMascota: () -> Unit = {}
) {
    val mascotas by viewModel.mascotas.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { BarraTitulo(titulo = "Mis Mascotas", onVolver = onVolver) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAgregarMascota,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar mascota")
            }
        }
    ) { padding ->
        if (mascotas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Filled.Pets,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Aún no tienes mascotas registradas",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Toca el botón + para agregar la primera",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 88.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(mascotas, key = { it.id }) { mascota ->
                    TarjetaMascota(mascota = mascota, onClick = { onIrAFicha(mascota.id) })
                }
            }
        }
    }
}

@Composable
private fun TarjetaMascota(mascota: Mascota, onClick: () -> Unit) {
    val edad = (LocalDate.now().year - mascota.anioNacimiento).coerceAtLeast(0)
    val textoEdad = if (edad == 1) "1 año" else "$edad años"
    val detalle = listOf(
        mascota.raza.takeIf { it.isNotBlank() } ?: mascota.especie.lowercase().replaceFirstChar { it.uppercase() },
        textoEdad,
        "${mascota.peso} kg"
    ).joinToString(" • ")

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MentaTarjeta),
        border = BorderStroke(1.dp, MentaBorde)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CirculoMascota(especie = mascota.especie)
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = mascota.nombre,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = detalle,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}