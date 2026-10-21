package com.lajara.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lajara.huellitasvet.data.local.relation.CitaConDetalle
import com.lajara.huellitasvet.data.prefs.PreferenciasStore
import com.lajara.huellitasvet.data.repository.VetRepository
import com.lajara.huellitasvet.model.EstadoCita
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime

class MisCitasViewModel(
    private val repository: VetRepository,
    private val preferenciasStore: PreferenciasStore
) : ViewModel() {

    // Reloj que se actualiza solo para que las listas y la regla de 2 horas se mantengan al día
    val ahora: StateFlow<LocalDateTime> = flow {
        while (true) {
            emit(LocalDateTime.now())
            delay(30_000)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LocalDateTime.now())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val todas: Flow<List<CitaConDetalle>> = preferenciasStore.usuarioId
        .flatMapLatest { id ->
            if (id > 0) repository.observarCitasDeDueno(id) else flowOf(emptyList())
        }

    // Próximas: PENDIENTE y CONFIRMADA desde ahora
    val proximas: StateFlow<List<CitaConDetalle>> = combine(todas, ahora) { lista, ahora ->
        lista
            .filter { esActiva(it) && fechaHora(it) >= ahora }
            .sortedBy { fechaHora(it) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Historial: ATENDIDA, CANCELADA y las pasadas
    val historial: StateFlow<List<CitaConDetalle>> = combine(todas, ahora) { lista, ahora ->
        lista
            .filter { !(esActiva(it) && fechaHora(it) >= ahora) }
            .sortedByDescending { fechaHora(it) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private fun esActiva(c: CitaConDetalle): Boolean =
        c.cita.estado == EstadoCita.PENDIENTE.name || c.cita.estado == EstadoCita.CONFIRMADA.name

    private fun fechaHora(c: CitaConDetalle): LocalDateTime =
        LocalDateTime.parse("${c.cita.fecha}T${c.cita.hora}")

    // Solo PENDIENTE o CONFIRMADA y solo si faltan más de 2 horas
    fun puedeCancelar(c: CitaConDetalle): Boolean {
        if (!esActiva(c)) return false
        return Duration.between(LocalDateTime.now(), fechaHora(c)) > Duration.ofHours(2)
    }

    fun cancelar(c: CitaConDetalle) {
        if (!puedeCancelar(c)) {
            _mensaje.value = "Ya no puedes cancelar esta cita"
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