package com.lajara.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import com.lajara.huellitasvet.data.repository.VetRepository

class AtenderCitaViewModel(
    private val repository: VetRepository
) : ViewModel()