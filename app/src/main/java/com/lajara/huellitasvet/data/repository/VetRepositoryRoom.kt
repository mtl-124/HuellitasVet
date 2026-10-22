package com.lajara.huellitasvet.data.repository

import androidx.room.withTransaction
import com.lajara.huellitasvet.data.local.VetDatabase
import com.lajara.huellitasvet.data.local.entity.Cita
import com.lajara.huellitasvet.data.local.entity.Mascota
import com.lajara.huellitasvet.data.local.entity.Servicio
import com.lajara.huellitasvet.data.local.entity.Usuario
import com.lajara.huellitasvet.data.local.relation.CitaConDetalle
import com.lajara.huellitasvet.data.local.relation.MascotaConDueno
import com.lajara.huellitasvet.model.ConteoServicio
import com.lajara.huellitasvet.model.EstadoCita
import kotlinx.coroutines.flow.Flow

class VetRepositoryRoom(
    private val db: VetDatabase
) : VetRepository {

    override suspend fun login(correo: String, clave: String): Usuario? {
        return db.usuarioDao().login(correo, clave)
    }

    override fun observarServicios(): Flow<List<Servicio>> {
        return db.servicioDao().observarTodos()
    }

    override fun observarServiciosActivos(): Flow<List<Servicio>> {
        return db.servicioDao().observarActivos()
    }

    override suspend fun guardarServicio(s: Servicio) {
        if (s.id == 0) {
            db.servicioDao().insertar(s)
        } else {
            db.servicioDao().actualizar(s)
        }
    }

    override suspend fun eliminarServicio(s: Servicio) {
        db.servicioDao().eliminar(s)
    }

    override suspend fun cambiarActivo(id: Int, activo: Boolean) {
        db.servicioDao().cambiarActivo(id, activo)
    }

    override suspend fun servicioTieneCitas(id: Int): Boolean {
        return db.citaDao().contarPorServicioId(id) > 0
    }

    override fun observarMascotasDeDueno(duenoId: Int): Flow<List<Mascota>> {
        return db.mascotaDao().observarPorDueno(duenoId)
    }

    override fun observarMascota(id: Int): Flow<Mascota?> {
        return db.mascotaDao().observarUna(id)
    }

    override fun observarTodasLasMascotas(): Flow<List<MascotaConDueno>> {
        return db.mascotaDao().observarTodas()
    }

    override suspend fun guardarMascota(m: Mascota) {
        if (m.id == 0) {
            db.mascotaDao().insertar(m)
        } else {
            db.mascotaDao().actualizar(m)
        }
    }

    override suspend fun eliminarMascota(m: Mascota) {
        db.mascotaDao().eliminar(m)
    }

    override suspend fun mascotaTieneCitas(id: Int): Boolean {
        return db.mascotaDao().contarCitas(id) > 0
    }

    override fun observarHorasOcupadas(fecha: String): Flow<List<String>> {
        return db.citaDao().observarHorasOcupadas(fecha)
    }

    override suspend fun crearCita(
        mascotaId: Int,
        servicioId: Int,
        fecha: String,
        hora: String,
        nota: String
    ): Long {
        val servicio = db.servicioDao().obtener(servicioId)
            ?: throw IllegalArgumentException("Servicio no encontrado")
        val cita = Cita(
            mascotaId = mascotaId,
            servicioId = servicioId,
            fecha = fecha,
            hora = hora,
            nota = nota,
            estado = EstadoCita.PENDIENTE.name,
            precio = servicio.precio
        )
        return db.citaDao().insertar(cita)
    }

    override fun observarCitasDeDueno(duenoId: Int): Flow<List<CitaConDetalle>> {
        return db.citaDao().observarPorDueno(duenoId)
    }

    override fun observarCitasPorFecha(fecha: String): Flow<List<CitaConDetalle>> {
        return db.citaDao().observarPorFecha(fecha)
    }

    override fun observarCita(id: Int): Flow<CitaConDetalle?> {
        return db.citaDao().observarUna(id)
    }

    override fun observarHistorial(mascotaId: Int): Flow<List<CitaConDetalle>> {
        return db.citaDao().observarHistorial(mascotaId)
    }

    override suspend fun cambiarEstadoCita(id: Int, estado: EstadoCita) {
        db.citaDao().actualizarEstado(id, estado.name)
    }

    override suspend fun registrarAtencion(
        citaId: Int,
        diagnostico: String,
        tratamiento: String,
        peso: Double
    ) {
        db.withTransaction {
            val cita = db.citaDao().obtener(citaId)
                ?: throw IllegalArgumentException("Cita no encontrada")
            db.citaDao().registrarAtencion(citaId, diagnostico, tratamiento, peso)
            db.mascotaDao().actualizarPeso(cita.mascotaId, peso)
        }
    }

    override fun observarCitasDeFecha(fecha: String): Flow<Int> {
        return db.citaDao().contarPorFecha(fecha)
    }

    override fun observarCantidadPorEstado(estado: EstadoCita): Flow<Int> {
        return db.citaDao().contarPorEstado(estado.name)
    }

    override fun observarCitasPorServicio(): Flow<List<ConteoServicio>> {
        return db.citaDao().contarPorServicio()
    }

    override fun observarIngresosDesde(fechaInicio: String): Flow<Double> {
        return db.citaDao().ingresosDesde(fechaInicio)
    }

    override fun observarAtencionesDesde(fechaInicio: String): Flow<Int> {
        return db.citaDao().atencionesDesde(fechaInicio)
    }

    override fun observarUsuario(id: Int): Flow<Usuario?> = db.usuarioDao().observarPorId(id)

}