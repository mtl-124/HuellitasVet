package com.lajara.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lajara.huellitasvet.data.local.entity.Mascota
import com.lajara.huellitasvet.data.local.relation.CitaConDetalle
import com.lajara.huellitasvet.data.prefs.PreferenciasStore
import com.lajara.huellitasvet.data.repository.VetRepository
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

data class ErroresMascota(
    val nombre: String? = null,
    val especie: String? = null,
    val anio: String? = null,
    val peso: String? = null,
    val sexo: String? = null
) {
    val hayErrores: Boolean
        get() = listOf(nombre, especie, anio, peso, sexo).any { it != null }
}

class MascotaViewModel(
    private val repository: VetRepository,
    private val preferenciasStore: PreferenciasStore
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val mascotas: StateFlow<List<Mascota>> = preferenciasStore.usuarioId
        .flatMapLatest { id ->
            if (id > 0) repository.observarMascotasDeDueno(id) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _errores = MutableStateFlow(ErroresMascota())
    val errores: StateFlow<ErroresMascota> = _errores.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private val fichas = mutableMapOf<Int, StateFlow<Mascota?>>()
    private val historiales = mutableMapOf<Int, StateFlow<List<CitaConDetalle>>>()

    fun mascota(id: Int): StateFlow<Mascota?> = fichas.getOrPut(id) {
        repository.observarMascota(id)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    }

    fun historial(mascotaId: Int): StateFlow<List<CitaConDetalle>> = historiales.getOrPut(mascotaId) {
        repository.observarHistorial(mascotaId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    private fun validar(m: Mascota): ErroresMascota {
        val anioActual = LocalDate.now().year
        return ErroresMascota(
            nombre = if (m.nombre.trim().length < 2) "Mínimo 2 caracteres" else null,
            especie = if (m.especie.isBlank()) "Elige una especie" else null,
            anio = if (m.anioNacimiento !in 1990..anioActual) "Debe estar entre 1990 y $anioActual" else null,
            peso = if (m.peso <= 0.0) "Debe ser mayor a 0" else null,
            sexo = if (m.sexo.isBlank()) "Elige el sexo" else null
        )
    }

    fun guardar(m: Mascota, onExito: () -> Unit) {
        val resultado = validar(m)
        _errores.value = resultado
        if (resultado.hayErrores) return

        viewModelScope.launch {
            val duenoId = if (m.duenoId > 0) m.duenoId else preferenciasStore.usuarioId.first()
            if (duenoId <= 0) {
                _mensaje.value = "No hay una sesión activa"
                return@launch
            }
            repository.guardarMascota(
                m.copy(
                    nombre = m.nombre.trim(),
                    raza = m.raza.trim(),
                    duenoId = duenoId
                )
            )
            onExito()
        }
    }

    fun eliminar(m: Mascota, onExito: () -> Unit) {
        viewModelScope.launch {
            if (repository.mascotaTieneCitas(m.id)) {
                _mensaje.value = "No se puede eliminar: ${m.nombre} tiene citas registradas"
            } else {
                repository.eliminarMascota(m)
                onExito()
            }
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}