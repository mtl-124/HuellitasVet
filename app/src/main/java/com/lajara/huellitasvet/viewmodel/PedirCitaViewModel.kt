package com.lajara.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import com.lajara.huellitasvet.data.prefs.PreferenciasStore
import com.lajara.huellitasvet.data.repository.VetRepository

class PedirCitaViewModel(
    private val repository: VetRepository,
    private val preferenciasStore: PreferenciasStore
) : ViewModel()