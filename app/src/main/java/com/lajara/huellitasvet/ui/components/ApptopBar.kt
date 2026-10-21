package com.lajara.huellitasvet.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApptopBar(
    titulo: String,
    onCerrarSesion: (() -> Unit)? = null
) {
    TopAppBar(
        title = { Text(titulo) },
        actions = {
            if (onCerrarSesion != null) {
                IconButton(onClick = onCerrarSesion) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Cerrar Sesión"
                    )
                }
            }
        }
    )
}