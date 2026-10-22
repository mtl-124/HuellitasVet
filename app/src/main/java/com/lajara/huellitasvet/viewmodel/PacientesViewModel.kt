package com.lajara.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lajara.huellitasvet.data.local.relation.MascotaConDueno
import com.lajara.huellitasvet.data.repository.VetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

enum class FiltroEspecie(val etiqueta: String) {
    TODOS("Todos"),
    PERROS("Perros"),
    GATOS("Gatos"),
    OTROS("Otros")
}

class PacientesViewModel(
    private val repository: VetRepository
) : ViewModel() {

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    private val _filtro = MutableStateFlow(FiltroEspecie.TODOS)
    val filtro: StateFlow<FiltroEspecie> = _filtro.asStateFlow()

    // Mascotas con su dueño, filtradas por texto (mascota o dueño) y por especie
    val pacientes: StateFlow<List<MascotaConDueno>> = combine(
        repository.observarTodasLasMascotas(),
        _busqueda,
        _filtro
    ) { lista, texto, seleccionado ->
        val q = texto.trim()
        lista.filter { p ->
            val especie = p.mascota.especie
            val coincideEspecie = when (seleccionado) {
                FiltroEspecie.TODOS -> true
                FiltroEspecie.PERROS -> especie.equals("PERRO", ignoreCase = true)
                FiltroEspecie.GATOS -> especie.equals("GATO", ignoreCase = true)
                FiltroEspecie.OTROS -> !especie.equals("PERRO", ignoreCase = true) &&
                        !especie.equals("GATO", ignoreCase = true)
            }
            val coincideTexto = q.isEmpty() ||
                    p.mascota.nombre.contains(q, ignoreCase = true) ||
                    p.dueno.nombre.contains(q, ignoreCase = true)
            coincideEspecie && coincideTexto
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // mascotaId -> fecha (YYYY-MM-DD) de su última cita ATENDIDA
    val ultimasVisitas: StateFlow<Map<Int, String>> = repository.observarCitasAtendidas()
        .map { citas ->
            citas.groupBy { it.mascotaId }
                .mapValues { (_, lista) -> lista.maxOf { it.fecha } }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    fun buscar(texto: String) {
        _busqueda.value = texto
    }

    fun elegirFiltro(filtro: FiltroEspecie) {
        _filtro.value = filtro
    }
}