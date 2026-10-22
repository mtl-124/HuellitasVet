package com.lajara.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lajara.huellitasvet.data.local.entity.Servicio
import com.lajara.huellitasvet.data.repository.VetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ServiciosViewModel(private val repo: VetRepository) : ViewModel() {

    val servicios: StateFlow<List<Servicio>> = repo.observarServicios()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    fun alternar(s: Servicio) {
        viewModelScope.launch {
            repo.cambiarActivo(s.id, !s.activo)
        }
    }

    // Nuevo (id = 0) o editado (id > 0)
    fun guardar(servicio: Servicio, onExito: () -> Unit) {
        viewModelScope.launch {
            repo.guardarServicio(servicio)
            onExito()
        }
    }

    // Un servicio con citas no se elimina: solo se desactiva
    fun eliminar(servicio: Servicio, onExito: () -> Unit = {}) {
        viewModelScope.launch {
            if (repo.servicioTieneCitas(servicio.id)) {
                repo.cambiarActivo(servicio.id, false)
                _mensaje.value = "Este servicio tiene citas, no se puede eliminar. Se desactivó."
            } else {
                repo.eliminarServicio(servicio)
                onExito()
            }
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}