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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ErroresAtencion(
    val diagnostico: String? = null,
    val peso: String? = null
) {
    val hayErrores: Boolean
        get() = diagnostico != null || peso != null
}

class AtenderCitaViewModel(
    private val repository: VetRepository
) : ViewModel() {

    private val _citaId = MutableStateFlow<Int?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val cita: StateFlow<CitaConDetalle?> = _citaId
        .flatMapLatest { id ->
            if (id == null) flowOf(null) else repository.observarCita(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _errores = MutableStateFlow(ErroresAtencion())
    val errores: StateFlow<ErroresAtencion> = _errores.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    fun cargar(id: Int) {
        _citaId.value = id
    }

    // Solo se puede atender (o cancelar) una cita PENDIENTE o CONFIRMADA
    fun puedeAtender(c: CitaConDetalle?): Boolean =
        c != null && (c.cita.estado == EstadoCita.PENDIENTE.name ||
                c.cita.estado == EstadoCita.CONFIRMADA.name)

    // Valida y registra: la cita queda ATENDIDA y el peso de la mascota se actualiza
    // en la misma transacción (repository.registrarAtencion)
    fun registrar(diagnostico: String, tratamiento: String, peso: Double?, onExito: () -> Unit) {
        val actual = cita.value
        if (actual == null) {
            _mensaje.value = "No se encontró la cita"
            return
        }
        if (!puedeAtender(actual)) {
            _mensaje.value = "Solo se puede atender una cita pendiente o confirmada"
            return
        }

        val resultado = ErroresAtencion(
            diagnostico = if (diagnostico.trim().length < 5) "Mínimo 5 caracteres" else null,
            peso = if (peso == null || peso <= 0.0) "Debe ser mayor a 0" else null
        )
        _errores.value = resultado
        if (resultado.hayErrores) return

        val pesoValido = peso ?: return
        viewModelScope.launch {
            repository.registrarAtencion(
                citaId = actual.cita.id,
                diagnostico = diagnostico.trim(),
                tratamiento = tratamiento.trim(),
                peso = pesoValido
            )
            onExito()
        }
    }

    fun cancelar(onExito: () -> Unit) {
        val actual = cita.value ?: return
        if (!puedeAtender(actual)) {
            _mensaje.value = "Esta cita ya no se puede cancelar"
            return
        }
        viewModelScope.launch {
            repository.cambiarEstadoCita(actual.cita.id, EstadoCita.CANCELADA)
            onExito()
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}