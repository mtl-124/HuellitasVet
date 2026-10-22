package com.lajara.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lajara.huellitasvet.data.local.entity.Mascota
import com.lajara.huellitasvet.data.local.entity.Servicio
import com.lajara.huellitasvet.data.prefs.PreferenciasStore
import com.lajara.huellitasvet.data.repository.VetRepository
import com.lajara.huellitasvet.model.Horarios
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class PedirCitaViewModel(
    private val repository: VetRepository,
    private val preferenciasStore: PreferenciasStore
) : ViewModel() {

    // De hoy a 7 días
    val fechas: List<LocalDate> = LocalDate.now().let { hoy -> (0..6).map { hoy.plusDays(it.toLong()) } }

    @OptIn(ExperimentalCoroutinesApi::class)
    val mascotas: StateFlow<List<Mascota>> = preferenciasStore.usuarioId
        .flatMapLatest { id ->
            if (id > 0) repository.observarMascotasDeDueno(id) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Solo servicios activos
    val servicios: StateFlow<List<Servicio>> = repository.observarServiciosActivos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val aceptaCitas: StateFlow<Boolean> = preferenciasStore.aceptarCitas
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    private val _mascotaId = MutableStateFlow<Int?>(null)
    val mascotaId: StateFlow<Int?> = _mascotaId.asStateFlow()

    private val _servicioId = MutableStateFlow<Int?>(null)
    val servicioId: StateFlow<Int?> = _servicioId.asStateFlow()

    private val _fecha = MutableStateFlow(LocalDate.now().toString())
    val fecha: StateFlow<String> = _fecha.asStateFlow()

    private val _hora = MutableStateFlow<String?>(null)
    val hora: StateFlow<String?> = _hora.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    // Cambia cada vez que cambia la fecha elegida
    @OptIn(ExperimentalCoroutinesApi::class)
    val horasOcupadas: StateFlow<List<String>> = _fecha
        .flatMapLatest { repository.observarHorasOcupadas(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun elegirMascota(id: Int) {
        _mascotaId.value = id
    }

    fun elegirServicio(id: Int) {
        _servicioId.value = id
    }

    fun elegirFecha(fecha: String) {
        if (fecha != _fecha.value) {
            _fecha.value = fecha
            _hora.value = null
        }
    }

    fun elegirHora(hora: String) {
        _hora.value = hora
    }

    fun confirmar(onExito: () -> Unit) {
        val mascota = _mascotaId.value
        val servicio = _servicioId.value
        val fecha = _fecha.value
        val hora = _hora.value
        if (mascota == null || servicio == null || hora == null) {
            _mensaje.value = "Completa mascota, servicio, fecha y hora"
            return
        }

        viewModelScope.launch {
            if (!preferenciasStore.aceptarCitas.first()) {
                _mensaje.value = "La clínica no está recibiendo citas"
                return@launch
            }
            // Se vuelve a consultar: el horario pudo ocuparse mientras tanto
            val ocupadas = repository.observarHorasOcupadas(fecha).first()
            if (hora in ocupadas || Horarios.yaPaso(fecha, hora)) {
                _hora.value = null
                _mensaje.value = "Ese horario ya no está disponible, elige otro"
                return@launch
            }
            repository.crearCita(
                mascotaId = mascota,
                servicioId = servicio,
                fecha = fecha,
                hora = hora,
                nota = ""
            )
            onExito()
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}