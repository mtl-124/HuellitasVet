package com.lajara.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lajara.huellitasvet.data.local.entity.Mascota
import com.lajara.huellitasvet.data.local.relation.CitaConDetalle
import com.lajara.huellitasvet.data.prefs.PreferenciasStore
import com.lajara.huellitasvet.data.repository.VetRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import com.lajara.huellitasvet.data.local.entity.Usuario

class InicioViewModel(
    private val repository: VetRepository,
    private val preferenciasStore: PreferenciasStore
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val mascotas: StateFlow<List<Mascota>> = preferenciasStore.usuarioId
        .flatMapLatest { id ->
            if (id != null && id > 0) repository.observarMascotasDeDueno(id)
            else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val citas: StateFlow<List<CitaConDetalle>> = preferenciasStore.usuarioId
        .flatMapLatest { id ->
            if (id != null && id > 0) repository.observarCitasDeDueno(id)
            else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val usuario: StateFlow<Usuario?> = preferenciasStore.usuarioId
        .flatMapLatest { id ->
            if (id > 0) repository.observarUsuario(id) else flowOf(null)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}