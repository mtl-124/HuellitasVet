package com.lajara.huellitasvet.data.local

import com.lajara.huellitasvet.data.local.entity.Cita
import com.lajara.huellitasvet.data.local.entity.Mascota
import com.lajara.huellitasvet.data.local.entity.Servicio
import com.lajara.huellitasvet.data.local.entity.Usuario
import com.lajara.huellitasvet.model.Especie
import com.lajara.huellitasvet.model.EstadoCita
import com.lajara.huellitasvet.model.Rol
import com.lajara.huellitasvet.model.Sexo
import java.time.LocalDate

object DatosIniciales {
    suspend fun sembrar(db: VetDatabase) {
        if (db.usuarioDao().contar() > 0) return

        val servicios = listOf(
            Servicio(id = 1, nombre = "Consulta general", descripcion = "Chequeo médico preventivo", duracionMin = 30, precio = 60.0, activo = true, icono = "heart"),
            Servicio(id = 2, nombre = "Vacunación", descripcion = "Aplicación de vacunas anuales", duracionMin = 30, precio = 45.0, activo = true, icono = "syringe"),
            Servicio(id = 3, nombre = "Baño y corte", descripcion = "Higiene y estética canina/felina", duracionMin = 60, precio = 50.0, activo = true, icono = "scissors"),
            Servicio(id = 4, nombre = "Desparasitación", descripcion = "Control de parásitos internos y externos", duracionMin = 15, precio = 25.0, activo = false, icono = "syringe")
        )
        db.servicioDao().insertarTodos(servicios)

        val usuarios = listOf(
            Usuario(id = 1, nombre = "Ana Gómez", correo = "ana@correo.test", clave = "1234", rol = Rol.CLIENTE.name),
            Usuario(id = 2, nombre = "Luis Torres", correo = "luis@correo.test", clave = "1234", rol = Rol.CLIENTE.name),
            Usuario(id = 3, nombre = "Dr. Carlos León", correo = "vet@huellitas.test", clave = "admin", rol = Rol.ADMIN.name)
        )
        db.usuarioDao().insertarTodos(usuarios)

        val mascotas = listOf(
            Mascota(id = 1, duenoId = 1, nombre = "Luna", especie = Especie.GATO.name, raza = "Siamesa", anioNacimiento = 2023, peso = 4.3, sexo = Sexo.HEMBRA.name),
            Mascota(id = 2, duenoId = 1, nombre = "Max", especie = Especie.PERRO.name, raza = "Labrador", anioNacimiento = 2021, peso = 24.5, sexo = Sexo.MACHO.name),
            Mascota(id = 3, duenoId = 2, nombre = "Rocky", especie = Especie.PERRO.name, raza = "Golden Retriever", anioNacimiento = 2020, peso = 30.0, sexo = Sexo.MACHO.name),
            Mascota(id = 4, duenoId = 2, nombre = "Michi", especie = Especie.GATO.name, raza = "Mestizo", anioNacimiento = 2023, peso = 3.5, sexo = Sexo.HEMBRA.name)
        )
        mascotas.forEach { db.mascotaDao().insertar(it) }

        val hoy = LocalDate.now()
        val ayer = hoy.minusDays(1).toString()
        val anteayer = hoy.minusDays(5).toString()
        val hoyStr = hoy.toString()
        val mananaStr = hoy.plusDays(1).toString()
        val pasadoStr = hoy.plusDays(2).toString()

        val citas = listOf(
            Cita(mascotaId = 1, servicioId = 2, fecha = LocalDate.of(hoy.year, 10, 12).toString(), hora = "10:00", nota = "Vacuna triple felina", estado = EstadoCita.ATENDIDA.name, precio = 45.0, diagnostico = "Vacuna aplicada sin reacciones", tratamiento = "Reposo por 24 horas", pesoRegistrado = 4.3),
            Cita(mascotaId = 1, servicioId = 1, fecha = LocalDate.of(hoy.year, 7, 3).toString(), hora = "09:30", nota = "Control de rutina, sin hallazgos", estado = EstadoCita.ATENDIDA.name, precio = 60.0, diagnostico = "Mascota saludable", tratamiento = "Ninguno", pesoRegistrado = 4.2),
            Cita(mascotaId = 1, servicioId = 3, fecha = LocalDate.of(hoy.year, 3, 15).toString(), hora = "16:00", nota = "Higiene general", estado = EstadoCita.ATENDIDA.name, precio = 50.0, diagnostico = "Piel y pelaje en buen estado", tratamiento = "Shampoo neutro", pesoRegistrado = 4.0),
            Cita(mascotaId = 2, servicioId = 2, fecha = ayer, hora = "11:30", nota = "Refuerzo anual", estado = EstadoCita.ATENDIDA.name, precio = 45.0, diagnostico = "Vacuna aplicada sin reacciones", tratamiento = "Reposo por 24 horas", pesoRegistrado = 24.5),
            Cita(mascotaId = 3, servicioId = 3, fecha = anteayer, hora = "15:00", nota = "Baño medicado", estado = EstadoCita.ATENDIDA.name, precio = 50.0, diagnostico = "Piel limpia y sin parásitos", tratamiento = "Shampoo neutro", pesoRegistrado = 29.8),
            Cita(mascotaId = 1, servicioId = 2, fecha = hoyStr, hora = "09:00", nota = "Vacuna rabia", estado = EstadoCita.CONFIRMADA.name, precio = 45.0),
            Cita(mascotaId = 4, servicioId = 1, fecha = hoyStr, hora = "10:30", nota = "Inapetencia", estado = EstadoCita.PENDIENTE.name, precio = 60.0),
            Cita(mascotaId = 3, servicioId = 1, fecha = hoyStr, hora = "14:00", nota = "Revisión de oídos", estado = EstadoCita.PENDIENTE.name, precio = 60.0),
            Cita(mascotaId = 2, servicioId = 3, fecha = mananaStr, hora = "10:00", nota = "Corte de pelo", estado = EstadoCita.PENDIENTE.name, precio = 50.0),
            Cita(mascotaId = 1, servicioId = 1, fecha = mananaStr, hora = "11:00", nota = "Control de peso", estado = EstadoCita.CONFIRMADA.name, precio = 60.0),
            Cita(mascotaId = 3, servicioId = 2, fecha = pasadoStr, hora = "16:00", nota = "Sextuple canina", estado = EstadoCita.PENDIENTE.name, precio = 45.0),
            Cita(mascotaId = 4, servicioId = 3, fecha = pasadoStr, hora = "17:00", nota = "Baño de espuma", estado = EstadoCita.CANCELADA.name, precio = 50.0)
        )
        citas.forEach { db.citaDao().insertar(it) }
    }
}