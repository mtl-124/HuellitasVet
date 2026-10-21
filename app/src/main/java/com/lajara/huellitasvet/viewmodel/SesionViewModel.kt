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
    private val repository: VetRepository,
    private val preferenciasStore: PreferenciasStore
) : ViewModel() {

    // 1. Solución al error de StateFlow: Convertir Flow a StateFlow
    val perfilActivo: StateFlow<String> = preferenciasStore.perfilActivo
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ""
        )

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun iniciarSesion(correo: String, clave: String) {
        viewModelScope.launch {
            val correoLimpio = correo.trim().lowercase()
            val claveLimpia = clave.trim()

            val usuario = repository.login(correoLimpio, claveLimpia)
            if (usuario != null) {
                // 2. Solución a Argument mismatch: Pasar primero rol (String) y luego id (Int)
                preferenciasStore.guardarSesion(usuario.rol, usuario.id)
            } else {
                _error.value = "Credenciales incorrectas"
            }
        }
    }

    fun cerrarSesion() {
        viewModelScope.launch {
            // 3. Solución a Unresolved reference: Usar el nombre exacto de tu PreferenciasStore
            preferenciasStore.cerrarSesion()
        }
    }

    fun limpiarError() {
        _error.value = null
    }
}