package com.lajara.huellitasvet.ui.veterinario

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.lajara.huellitasvet.viewmodel.ServiciosViewModel

@Composable
fun ServiciosScreen(
    vm: ServiciosViewModel,
    onEditar: (Int) -> Unit
) {
    val servicios by vm.servicios.collectAsState()

    LazyColumn {
        items(servicios, key = { it.id }) { s ->
            // ServicioItem(s, onToggle = { vm.alternar(s) }, onEditar = { onEditar(s.id) })
        }
    }
}