package com.lajara.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lajara.huellitasvet.data.local.relation.CitaConDetalle
import com.lajara.huellitasvet.data.repository.VetRepository
import com.lajara.huellitasvet.model.EstadoCita
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class AgendaViewModel(
    private val repository: VetRepository
) : ViewModel() {

    // Chips de los próximos días (hoy + 3)
    val dias: List<LocalDate> = (0..3).map { LocalDate.now().plusDays(it.toLong()) }

    private val _fechaSeleccionada = MutableStateFlow(LocalDate.now())
    val fechaSeleccionada: StateFlow<LocalDate> = _fechaSeleccionada.asStateFlow()

    // Citas del día elegido, ordenadas por hora
    @OptIn(ExperimentalCoroutinesApi::class)
    val citas: StateFlow<List<CitaConDetalle>> = _fechaSeleccionada
        .flatMapLatest { fecha -> repository.observarCitasPorFecha(fecha.toString()) }
        .map { lista -> lista.sortedBy { it.cita.hora } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    fun seleccionarFecha(fecha: LocalDate) {
        _fechaSeleccionada.value = fecha
    }

    // Reglas de estado: solo PENDIENTE se confirma; PENDIENTE o CONFIRMADA se cancelan
    fun puedeConfirmar(c: CitaConDetalle): Boolean =
        c.cita.estado == EstadoCita.PENDIENTE.name

    fun puedeCancelar(c: CitaConDetalle): Boolean =
        c.cita.estado == EstadoCita.PENDIENTE.name || c.cita.estado == EstadoCita.CONFIRMADA.name

    fun confirmar(c: CitaConDetalle) {
        if (!puedeConfirmar(c)) {
            _mensaje.value = "Solo se pueden confirmar citas pendientes"
            return
        }
        viewModelScope.launch {
            repository.cambiarEstadoCita(c.cita.id, EstadoCita.CONFIRMADA)
            _mensaje.value = "Cita confirmada"
        }
    }

    fun cancelar(c: CitaConDetalle) {
        if (!puedeCancelar(c)) {
            _mensaje.value = "Esta cita ya no se puede cancelar"
            return
        }
        viewModelScope.launch {
            repository.cambiarEstadoCita(c.cita.id, EstadoCita.CANCELADA)
            _mensaje.value = "Cita cancelada"
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}