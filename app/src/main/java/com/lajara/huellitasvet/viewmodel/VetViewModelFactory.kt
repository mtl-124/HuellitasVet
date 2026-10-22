package com.lajara.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lajara.huellitasvet.data.prefs.PreferenciasStore
import com.lajara.huellitasvet.data.repository.VetRepository

class VetViewModelFactory(
    private val repository: VetRepository,
    private val preferenciasStore: PreferenciasStore
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SesionViewModel::class.java) -> {
                SesionViewModel(repository, preferenciasStore) as T
            }
            modelClass.isAssignableFrom(InicioViewModel::class.java) -> {
                InicioViewModel(repository, preferenciasStore) as T
            }
            modelClass.isAssignableFrom(MascotaViewModel::class.java) -> {
                MascotaViewModel(repository, preferenciasStore) as T
            }
            modelClass.isAssignableFrom(PedirCitaViewModel::class.java) -> {
                PedirCitaViewModel(repository, preferenciasStore) as T
            }
            modelClass.isAssignableFrom(MisCitasViewModel::class.java) -> {
                MisCitasViewModel(repository, preferenciasStore) as T
            }
            modelClass.isAssignableFrom(PanelViewModel::class.java) -> {
                PanelViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AgendaViewModel::class.java) -> {
                AgendaViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AtenderCitaViewModel::class.java) -> {
                AtenderCitaViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("ViewModel no reconocido: ${modelClass.name}")
        }
    }
}