package com.lajara.huellitasvet.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "ajustes")

class PreferenciasStore(private val context: Context) {

    companion object {
        val PERFIL_ACTIVO = stringPreferencesKey("perfil_activo")
        val USUARIO_ID = intPreferencesKey("usuario_id")
        val MODO_OSCURO = booleanPreferencesKey("modo_oscuro")
        val NOTIFICACIONES = booleanPreferencesKey("notificaciones")
        val ACEPTAR_CITAS = booleanPreferencesKey("aceptar_citas")
    }

    val perfilActivo: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[PERFIL_ACTIVO] ?: ""
    }

    val usuarioId: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[USUARIO_ID] ?: 0
    }

    val modoOscuro: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[MODO_OSCURO] ?: false
    }

    val notificaciones: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[NOTIFICACIONES] ?: true
    }

    val aceptarCitas: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[ACEPTAR_CITAS] ?: true
    }

    suspend fun guardarSesion(rol: String, usuarioId: Int) {
        context.dataStore.edit { prefs ->
            prefs[PERFIL_ACTIVO] = rol
            prefs[USUARIO_ID] = usuarioId
        }
    }

    suspend fun guardarModoOscuro(activo: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[MODO_OSCURO] = activo
        }
    }

    suspend fun guardarNotificaciones(activo: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[NOTIFICACIONES] = activo
        }
    }

    suspend fun guardarAceptarCitas(activo: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[ACEPTAR_CITAS] = activo
        }
    }

    suspend fun cerrarSesion() {
        context.dataStore.edit { prefs ->
            prefs[PERFIL_ACTIVO] = ""
            prefs[USUARIO_ID] = 0
        }
    }
}