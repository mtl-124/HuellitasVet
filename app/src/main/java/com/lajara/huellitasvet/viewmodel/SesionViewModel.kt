package com.lajara.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lajara.huellitasvet.data.prefs.PreferenciasStore
import com.lajara.huellitasvet.data.repository.VetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SesionViewModel(
    private val repo: VetRepository,
    private val prefs: PreferenciasStore
) : ViewModel() {

    val perfilActivo: StateFlow<String> = prefs.perfilActivo.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    val usuarioId: StateFlow<Int> = prefs.usuarioId.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    fun iniciarSesion(correo: String, clave: String) {
        if (correo.isBlank() || clave.isBlank()) {
            _error.value = "Ingresa correo y contraseña"
            return
        }

        viewModelScope.launch {
            _cargando.value = true
            _error.value = null

            val usuario = repo.login(correo.trim(), clave.trim())
            if (usuario != null) {
                prefs.guardarSesion(usuario.rol, usuario.id)
            } else {
                _error.value = "Credenciales incorrectas"
            }
            _cargando.value = false
        }
    }

    fun cerrarSesion() {
        viewModelScope.launch {
            prefs.cerrarSesion()
        }
    }

    fun limpiarError() {
        _error.value = null
    }
}