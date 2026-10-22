package com.lajara.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lajara.huellitasvet.data.repository.VetRepository
import com.lajara.huellitasvet.model.EstadoCita
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

class PanelViewModel(
    private val repository: VetRepository
) : ViewModel() {

    private val hoy = LocalDate.now().toString()
    private val inicioMes = LocalDate.now().withDayOfMonth(1).toString()

    private fun <T> Flow<T>.comoEstado(inicial: T): StateFlow<T> =
        stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), inicial)

    val citasHoy: StateFlow<Int> = repository.observarCitasDeFecha(hoy).comoEstado(0)

    val pendientes: StateFlow<Int> =
        repository.observarCantidadPorEstado(EstadoCita.PENDIENTE).comoEstado(0)

    val atendidas: StateFlow<Int> =
        repository.observarCantidadPorEstado(EstadoCita.ATENDIDA).comoEstado(0)

    // Ingresos de las citas ATENDIDAS desde el día 1 del mes
    val ingresosMes: StateFlow<Double> =
        repository.observarIngresosDesde(inicioMes).comoEstado(0.0)

    val atencionesMes: StateFlow<Int> =
        repository.observarAtencionesDesde(inicioMes).comoEstado(0)

    // Datos para GraficoDona: (nombre, cantidad)
    val porEstado: StateFlow<List<Pair<String, Int>>> = combine(
        repository.observarCantidadPorEstado(EstadoCita.PENDIENTE),
        repository.observarCantidadPorEstado(EstadoCita.CONFIRMADA),
        repository.observarCantidadPorEstado(EstadoCita.ATENDIDA),
        repository.observarCantidadPorEstado(EstadoCita.CANCELADA)
    ) { pendiente, confirmada, atendida, cancelada ->
        listOf(
            "Pendiente" to pendiente,
            "Confirmada" to confirmada,
            "Atendida" to atendida,
            "Cancelada" to cancelada
        )
    }.comoEstado(emptyList())

    // Datos para GraficoBarras: los 4 servicios con más citas
    val porServicio: StateFlow<List<Pair<String, Int>>> = repository.observarCitasPorServicio()
        .map { lista ->
            lista.sortedByDescending { it.cantidad }
                .take(4)
                .map { it.servicioNombre to it.cantidad }
        }
        .comoEstado(emptyList())
}