package com.lajara.huellitasvet.ui.veterinario

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
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lajara.huellitasvet.ui.components.BarraTitulo
import com.lajara.huellitasvet.ui.theme.bordeSuave
import com.lajara.huellitasvet.ui.theme.esClaro
import com.lajara.huellitasvet.ui.theme.menta
import com.lajara.huellitasvet.ui.theme.rojo
import com.lajara.huellitasvet.ui.theme.tarjeta
import com.lajara.huellitasvet.viewmodel.InicioViewModel
import com.lajara.huellitasvet.viewmodel.SesionViewModel

@Composable
fun AjustesScreen(
    viewModel: InicioViewModel,
    sesionViewModel: SesionViewModel,
    onVolver: () -> Unit,
    onIrAPanel: () -> Unit,
    onIrAAgenda: () -> Unit,
    onIrAPacientes: () -> Unit,
    onIrAServicios: () -> Unit
) {
    val colores = MaterialTheme.colorScheme
    val usuario by viewModel.usuario.collectAsState()
    val modoOscuro by sesionViewModel.modoOscuro.collectAsState()
    val notificaciones by sesionViewModel.recordatorios.collectAsState()
    val aceptarCitas by sesionViewModel.aceptarCitas.collectAsState()

    Scaffold(
        containerColor = colores.background,
        topBar = { BarraTitulo(titulo = "Ajustes", onVolver = onVolver) },
        bottomBar = {
            BarraInferiorVet(
                seleccion = 4,
                onPanel = onIrAPanel,
                onAgenda = onIrAAgenda,
                onPacientes = onIrAPacientes,
                onServicios = onIrAServicios,
                onAjustes = {}
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            // Tarjeta de usuario
            val formaTarjeta = RoundedCornerShape(16.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(formaTarjeta)
                    .background(colores.tarjeta)
                    .border(BorderStroke(1.dp, colores.bordeSuave), formaTarjeta)
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(57.dp)
                        .clip(CircleShape)
                        .background(colores.menta),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null,
                        tint = colores.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = usuario?.nombre.orEmpty(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = colores.onBackground
                    )
                    Text(
                        text = usuario?.correo.orEmpty(),
                        fontSize = 12.sp,
                        color = colores.onSurfaceVariant
                    )
                    Spacer(Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(colores.menta)
                            .padding(horizontal = 9.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Perfil activo",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = colores.primary
                        )
                    }
                }
            }

            // Cambiar de perfil
            Spacer(Modifier.height(14.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(colores.primary)
                    .clickable { sesionViewModel.cambiarPerfil() }
                    .padding(horizontal = 18.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.SwapHoriz,
                    contentDescription = null,
                    tint = colores.onPrimary,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Cambiar de perfil",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = colores.onPrimary
                    )
                    Text(
                        text = "Modo práctica: ir a vista Dueño",
                        fontSize = 11.sp,
                        color = colores.onPrimary.copy(alpha = 0.9f)
                    )
                }
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = null,
                    tint = colores.onPrimary
                )
            }

            // Preferencias
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Preferencias",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = colores.onBackground
            )
            Spacer(Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(formaTarjeta)
                    .background(colores.tarjeta)
                    .border(BorderStroke(1.dp, colores.bordeSuave), formaTarjeta)
                    .padding(horizontal = 14.dp, vertical = 4.dp)
            ) {
                FilaAjuste(
                    titulo = "Modo oscuro",
                    detalle = "Cambia el tema de la app",
                    activo = modoOscuro,
                    onCambio = { sesionViewModel.cambiarModoOscuro(it) }
                )
                HorizontalDivider(color = colores.bordeSuave)
                FilaAjuste(
                    titulo = "Notificaciones",
                    detalle = "Avisar citas nuevas",
                    activo = notificaciones,
                    onCambio = { sesionViewModel.cambiarRecordatorios(it) }
                )
                HorizontalDivider(color = colores.bordeSuave)
                FilaAjuste(
                    titulo = "Aceptar citas",
                    detalle = "Abrir o cerrar la agenda",
                    activo = aceptarCitas,
                    onCambio = { sesionViewModel.cambiarAceptarCitas(it) }
                )
            }

            // Aviso DataStore
            Spacer(Modifier.height(13.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(colores.menta)
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Se guardan en el dispositivo",
                    fontSize = 13.sp,
                    color = colores.onBackground
                )
                Text(
                    text = "con DataStore",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = colores.primary
                )
            }

            // Cerrar sesión
            Spacer(Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (colores.esClaro) Color(0xFFFDF2F2) else Color(0xFF3B1F1F))
                    .border(BorderStroke(1.5.dp, colores.rojo), RoundedCornerShape(12.dp))
                    .clickable { sesionViewModel.cerrarSesion() },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = null,
                    tint = colores.rojo,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Cerrar sesión",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = colores.rojo
                )
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun FilaAjuste(
    titulo: String,
    detalle: String,
    activo: Boolean,
    onCambio: (Boolean) -> Unit
) {
    val colores = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = colores.onBackground
            )
            Text(
                text = detalle,
                fontSize = 11.sp,
                color = colores.onSurfaceVariant
            )
        }
        Switch(checked = activo, onCheckedChange = onCambio)
    }
}