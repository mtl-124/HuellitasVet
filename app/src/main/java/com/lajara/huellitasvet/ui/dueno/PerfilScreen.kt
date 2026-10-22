package com.lajara.huellitasvet.ui.dueno

import com.lajara.huellitasvet.ui.theme.tarjeta
import com.lajara.huellitasvet.ui.theme.menta
import com.lajara.huellitasvet.ui.theme.bordeSuave
import com.lajara.huellitasvet.ui.theme.esClaro
import com.lajara.huellitasvet.ui.theme.ambar
import com.lajara.huellitasvet.ui.theme.rojo
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lajara.huellitasvet.data.local.relation.CitaConDetalle
import com.lajara.huellitasvet.ui.theme.AmbarContenedor
import com.lajara.huellitasvet.ui.theme.GrisBorde
import com.lajara.huellitasvet.ui.theme.MentaTarjeta
import com.lajara.huellitasvet.ui.theme.VerdeContenedor
import com.lajara.huellitasvet.viewmodel.InicioViewModel
import com.lajara.huellitasvet.viewmodel.SesionViewModel
import java.time.LocalDateTime

@Composable
fun PerfilScreen(
    viewModel: InicioViewModel,
    sesionViewModel: SesionViewModel,
    onIrAInicio: () -> Unit = {},
    onIrAMascotas: () -> Unit = {},
    onIrAMisCitas: () -> Unit = {}
) {
    val colores = MaterialTheme.colorScheme
    val usuario by viewModel.usuario.collectAsState()
    val mascotas by viewModel.mascotas.collectAsState()
    val citas by viewModel.citas.collectAsState()
    val modoOscuro by sesionViewModel.modoOscuro.collectAsState()
    val recordatorios by sesionViewModel.recordatorios.collectAsState()

    val proximas = remember(citas) {
        val ahora = LocalDateTime.now()
        citas.count { it.esProxima(ahora) }
    }
    val atenciones = remember(citas) { citas.count { it.cita.estado == "ATENDIDA" } }

    Scaffold(
        containerColor = colores.background,
        bottomBar = {
            BarraInferiorInicio(
                seleccion = 3,
                onInicio = onIrAInicio,
                onMascotas = onIrAMascotas,
                onCitas = onIrAMisCitas,
                onPerfil = {}
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(52.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Perfil",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = colores.onBackground
                )
            }

            Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                // Tarjeta de usuario
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(colores.tarjeta)
                        .border(BorderStroke(1.dp, colores.bordeSuave), RoundedCornerShape(16.dp))
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(57.dp)
                            .clip(CircleShape)
                            .background(if (colores.esClaro) Color(0xFFD5EFE2) else colores.menta),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = null,
                            tint = colores.onBackground,
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
                                .background(if (colores.esClaro) Color(0xFFC9F0DC) else colores.primaryContainer)
                                .padding(horizontal = 9.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Dueño",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = colores.primary
                            )
                        }
                    }
                }

                Spacer(Modifier.height(13.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    TarjetaEstadistica(
                        valor = mascotas.size,
                        etiqueta = "Mascotas",
                        fondo = if (colores.esClaro) VerdeContenedor else Color(0xFF123D2E),
                        colorValor = colores.primary,
                        colorEtiqueta = if (colores.esClaro) Color(0xFF047857) else Color(0xFF6EE7B7),
                        modifier = Modifier.weight(1f)
                    )
                    TarjetaEstadistica(
                        valor = proximas,
                        etiqueta = "Próximas",
                        fondo = colores.ambar,
                        colorValor = if (colores.esClaro) Color(0xFFB45309) else Color(0xFFFBBF24),
                        colorEtiqueta = if (colores.esClaro) Color(0xFF92400E) else Color(0xFFFCD34D),
                        modifier = Modifier.weight(1f)
                    )
                    TarjetaEstadistica(
                        valor = atenciones,
                        etiqueta = "Atenciones",
                        fondo = if (colores.esClaro) Color(0xFFDBEAFE) else Color(0xFF1E2A4A),
                        colorValor = if (colores.esClaro) Color(0xFF1D4ED8) else Color(0xFF93C5FD),
                        colorEtiqueta = if (colores.esClaro) Color(0xFF1E40AF) else Color(0xFFBFDBFE),
                        modifier = Modifier.weight(1f)
                    )
                }

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
                            text = "Cambia tu perfil de Dueño / Veterinario",
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
                        .clip(RoundedCornerShape(16.dp))
                        .background(colores.tarjeta)
                        .border(BorderStroke(1.dp, colores.bordeSuave), RoundedCornerShape(16.dp))
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    FilaPreferencia(
                        titulo = "Modo oscuro",
                        detalle = "Activa el tema oscuro en la app",
                        activo = modoOscuro,
                        onCambio = { sesionViewModel.cambiarModoOscuro(it) }
                    )
                    FilaPreferencia(
                        titulo = "Recordatorios",
                        detalle = "Recibe notificaciones de tus citas",
                        activo = recordatorios,
                        onCambio = { sesionViewModel.cambiarRecordatorios(it) }
                    )
                }

                Spacer(Modifier.height(13.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(colores.menta)
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "Se guarda en el dispositivo",
                        fontSize = 13.sp,
                        color = colores.onBackground
                    )
                    Text(
                        text = "con DataStore",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = colores.onBackground
                    )
                }

                Spacer(Modifier.height(13.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (colores.esClaro) Color(0xFFF5E6E4) else Color(0xFF3B1F1F))
                        .border(BorderStroke(1.dp, colores.rojo), RoundedCornerShape(12.dp))
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
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = colores.rojo
                    )
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

private fun CitaConDetalle.esProxima(ahora: LocalDateTime): Boolean {
    val activa = cita.estado == "PENDIENTE" || cita.estado == "CONFIRMADA"
    return activa && LocalDateTime.parse("${cita.fecha}T${cita.hora}") >= ahora
}

@Composable
private fun TarjetaEstadistica(
    valor: Int,
    etiqueta: String,
    fondo: Color,
    colorValor: Color,
    colorEtiqueta: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(66.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(fondo),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = valor.toString(),
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = colorValor
        )
        Text(
            text = etiqueta,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = colorEtiqueta
        )
    }
}

@Composable
private fun FilaPreferencia(
    titulo: String,
    detalle: String,
    activo: Boolean,
    onCambio: (Boolean) -> Unit
) {
    val colores = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                fontSize = 15.sp,
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