package com.lajara.huellitasvet.ui.navigation

object Rutas {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val INICIO_DUENO = "inicio_dueno"
    const val VET_HOME = "vet_home"
    const val VET_AGENDA = "vet_agenda"
    const val VET_PACIENTES = "vet_pacientes"
    const val VET_SERVICIOS = "vet_servicios"
    const val VET_AJUSTES = "vet_ajustes"
    const val MASCOTAS = "mascotas"
    const val PEDIR_CITA = "pedir_cita"
    const val MIS_CITAS = "mis_citas"
    const val ASISTENTE_IA = "asistente_ia"
    const val PERFIL = "perfil"

    const val FORMULARIO_MASCOTA = "formulario_mascota?id={id}"
    const val FICHA_MASCOTA = "ficha_mascota/{id}"
    const val ATENDER_CITA = "atender_cita/{id}"
    const val VET_FICHA = "vet_ficha/{id}"
    const val FORMULARIO_SERVICIO = "formulario_servicio?id={id}"

    fun formularioMascota(id: Int? = null) =
        if (id == null) "formulario_mascota" else "formulario_mascota?id=$id"

    fun fichaMascota(id: Int) = "ficha_mascota/$id"

    fun atenderCita(id: Int) = "atender_cita/$id"

    fun vetFicha(id: Int) = "vet_ficha/$id"

    fun formularioServicio(id: Int? = null) =
        if (id == null) "formulario_servicio" else "formulario_servicio?id=$id"
}