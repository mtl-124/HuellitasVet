package com.lajara.huellitasvet

import android.app.Application
import com.lajara.huellitasvet.data.local.DatosIniciales
import com.lajara.huellitasvet.data.local.VetDatabase
import com.lajara.huellitasvet.data.prefs.PreferenciasStore
import com.lajara.huellitasvet.data.repository.VetRepository
import com.lajara.huellitasvet.data.repository.VetRepositoryRoom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VetApp : Application() {

    lateinit var database: VetDatabase
        private set

    lateinit var repository: VetRepository
        private set

    lateinit var preferenciasStore: PreferenciasStore
        private set

    override fun onCreate() {
        super.onCreate()
        database = VetDatabase.obtener(this)
        repository = VetRepositoryRoom(database)
        preferenciasStore = PreferenciasStore(this)

        CoroutineScope(Dispatchers.IO).launch {
            DatosIniciales.sembrar(database)
        }
    }
}