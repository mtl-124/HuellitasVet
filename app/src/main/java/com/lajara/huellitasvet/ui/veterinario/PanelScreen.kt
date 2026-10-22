package com.lajara.huellitasvet.ui.veterinario

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
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
import com.lajara.huellitasvet.model.Horarios
import com.lajara.huellitasvet.ui.components.graficos.GraficoBarras
import com.lajara.huellitasvet.ui.components.graficos.GraficoDona
import com.lajara.huellitasvet.ui.theme.ambar
import com.lajara.huellitasvet.ui.theme.ambarBorde
import com.lajara.huellitasvet.ui.theme.ambarTexto
import com.lajara.huellitasvet.ui.theme.bordeSuave
import com.lajara.huellitasvet.ui.theme.esClaro
import com.lajara.huellitasvet.ui.theme.fondoInactivo
import com.lajara.huellitasvet.ui.theme.tarjeta
import com.lajara.huellitasvet.viewmodel.PanelViewModel
import java.time.LocalDate
import java.util.Locale

private val NaranjaAlerta = Color(0xFFD97706)
private val ColoresEstado = listOf(
    Color(0xFFD97706), // Pendiente
    Color(0xFF16A34A), // Confirmada
    Color(0xFF475569), // Atendida
    Color(0xFFDC2626)  // Cancelada
)

@Composable
fun PanelScreen(
    viewModel: PanelViewModel,
    onIrAAgenda: () -> Unit,
    onIrAPacientes: () -> Unit,
    onIrAServicios: () -> Unit,
    onIrAAjustes: () -> Unit
) {
    val colores = MaterialTheme.colorScheme
    val citasHoy by viewModel.citasHoy.collectAsState()
    val pendientes by viewModel.pendientes.collectAsState()
    val atendidas by viewModel.atendidas.collectAsState()
    val ingresosMes by viewModel.ingresosMes.collectAsState()
    val atencionesMes by viewModel.atencionesMes.collectAsState()
    val porEstado by viewModel.porEstado.collectAsState()
    val porServicio by viewModel.porServicio.collectAsState()

    val hoy = remember { LocalDate.now() }
    val fechaTexto = "Hoy · ${Horarios.nombreDia(hoy)} ${hoy.dayOfMonth} ${Horarios.nombreMes(hoy)}"

    Scaffold(
        containerColor = colores.background,
        bottomBar = {
            BarraInferiorVet(
                seleccion = 0,
                onPanel = {},
                onAgenda = onIrAAgenda,
                onPacientes = onIrAPacientes,
                onServicios = onIrAServicios,
                onAjustes = onIrAAjustes
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Panel de la clínica",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = colores.onBackground
                    )
                    Text(
                        text = fechaTexto,
                        fontSize = 12.sp,
                        color = colores.onSurfaceVariant
                    )
                }
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "Notificaciones",
                    tint = colores.onBackground
                )
            }

            Spacer(Modifier.height(16.dp))

            // 3 contadores de color
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Contador(
                    valor = citasHoy,
                    etiqueta = "Citas hoy",
                    fondo = colores.primaryContainer,
                    colorValor = colores.primary,
                    modifier = Modifier.weight(1f)
                )
                Contador(
                    valor = pendientes,
                    etiqueta = "Pendientes",
                    fondo = colores.ambar,
                    colorValor = if (colores.esClaro) NaranjaAlerta else colores.ambarTexto,
                    modifier = Modifier.weight(1f)
                )
                Contador(
                    valor = atendidas,
                    etiqueta = "Atendidas",
                    fondo = colores.fondoInactivo,
                    colorValor = colores.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(12.dp))

            // Tarjeta de ingresos del mes
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(colores.primary)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Ingresos del mes",
                    fontSize = 12.sp,
                    color = colores.onPrimary.copy(alpha = 0.85f)
                )
                Spacer(Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "S/ " + String.format(Locale.US, "%,.2f", ingresosMes),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = colores.onPrimary
                    )
                    Text(
                        text = if (atencionesMes == 1) "1 atención" else "$atencionesMes atenciones",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = colores.onPrimary
                    )
                }
            }

            Spacer(Modifier.height(20.dp))
            TituloSeccion("Citas por estado")
            Spacer(Modifier.height(8.dp))

            TarjetaGrafico {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    GraficoDona(
                        datos = porEstado,
                        colores = ColoresEstado
                    )
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        porEstado.forEachIndexed { i, dato ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(ColoresEstado[i % ColoresEstado.size])
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = dato.first,
                                    fontSize = 12.sp,
                                    modifier = Modifier.weight(1f),
                                    color = colores.onBackground
                                )
                                Text(
                                    text = dato.second.toString(),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colores.onBackground
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
            TituloSeccion("Citas por servicio")
            Spacer(Modifier.height(8.dp))

            TarjetaGrafico {
                GraficoBarras(datos = porServicio)
            }

            // Alerta de citas pendientes
            if (pendientes > 0) {
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(colores.ambar)
                        .border(1.dp, colores.ambarBorde, RoundedCornerShape(14.dp))
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Warning,
                        contentDescription = null,
                        tint = NaranjaAlerta,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (pendientes == 1) "1 cita por confirmar" else "$pendientes citas por confirmar",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = colores.ambarTexto
                        )
                        Text(
                            text = "revisa la agenda",
                            fontSize = 11.sp,
                            color = colores.ambarTexto.copy(alpha = 0.8f)
                        )
                    }
                    Button(
                        onClick = onIrAAgenda,
                        modifier = Modifier.height(36.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NaranjaAlerta,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Ver", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun Contador(
    valor: Int,
    etiqueta: String,
    fondo: Color,
    colorValor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(fondo)
            .padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = valor.toString(),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = colorValor
        )
        Text(
            text = etiqueta,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = colorValor
        )
    }
}

@Composable
private fun TituloSeccion(texto: String) {
    Text(
        text = texto,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
private fun TarjetaGrafico(contenido: @Composable () -> Unit) {
    val colores = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(colores.tarjeta)
            .border(1.dp, colores.bordeSuave, RoundedCornerShape(14.dp))
            .padding(16.dp)
    ) {
        contenido()
    }
}

private data class ItemVet(
    val icono: ImageVector,
    val etiqueta: String,
    val accion: () -> Unit
)

// Barra inferior compartida por Panel y Agenda (Panel, Agenda, Pacientes, Servicios, Ajustes)
@Composable
internal fun BarraInferiorVet(
    seleccion: Int,
    onPanel: () -> Unit,
    onAgenda: () -> Unit,
    onPacientes: () -> Unit,
    onServicios: () -> Unit,
    onAjustes: () -> Unit
) {
    val colores = MaterialTheme.colorScheme
    val estilo = NavigationBarItemDefaults.colors(
        selectedIconColor = colores.primary,
        selectedTextColor = colores.primary,
        indicatorColor = Color.Transparent,
        unselectedIconColor = colores.onSurfaceVariant,
        unselectedTextColor = colores.onSurfaceVariant
    )
    val items = listOf(
        ItemVet(Icons.Filled.BarChart, "Panel", onPanel),
        ItemVet(Icons.Filled.CalendarMonth, "Agenda", onAgenda),
        ItemVet(Icons.Filled.Pets, "Pacientes", onPacientes),
        ItemVet(Icons.Filled.LocalOffer, "Servicios", onServicios),
        ItemVet(Icons.Filled.Settings, "Ajustes", onAjustes)
    )
    NavigationBar(containerColor = colores.tarjeta) {
        items.forEachIndexed { i, item ->
            NavigationBarItem(
                selected = seleccion == i,
                onClick = item.accion,
                colors = estilo,
                icon = { Icon(item.icono, contentDescription = item.etiqueta) },
                label = {
                    Text(
                        text = item.etiqueta,
                        fontSize = 10.sp,
                        fontWeight = if (seleccion == i) FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
        }
    }
}