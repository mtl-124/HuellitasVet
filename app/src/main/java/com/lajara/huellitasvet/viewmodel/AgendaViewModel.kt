package com.lajara.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import com.lajara.huellitasvet.data.repository.VetRepository

class AgendaViewModel(
    private val repository: VetRepository
) : ViewModel()