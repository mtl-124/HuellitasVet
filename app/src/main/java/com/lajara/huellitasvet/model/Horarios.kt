package com.lajara.huellitasvet.model

import java.time.LocalDate
import java.time.LocalTime

object Horarios {
    // 18 horarios: de 09:00 a 17:30 cada 30 minutos
    val todos: List<String> = (0 until 18).map { i ->
        val minutos = 9 * 60 + i * 30
        "%02d:%02d".format(minutos / 60, minutos % 60)
    }

    private val dias = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")
    private val meses = listOf("ene", "feb", "mar", "abr", "may", "jun", "jul", "ago", "sep", "oct", "nov", "dic")

    fun nombreDia(fecha: LocalDate): String = dias[fecha.dayOfWeek.value - 1]

    fun nombreMes(fecha: LocalDate): String = meses[fecha.monthValue - 1]

    fun yaPaso(fecha: String, hora: String): Boolean {
        if (fecha != LocalDate.now().toString()) return false
        return LocalTime.parse(hora).isBefore(LocalTime.now())
    }
}