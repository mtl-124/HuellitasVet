package com.lajara.huellitasvet.ui.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lajara.huellitasvet.VetApp
import com.lajara.huellitasvet.ui.dueno.InicioScreen
import com.lajara.huellitasvet.ui.dueno.LoginScreen
import com.lajara.huellitasvet.ui.dueno.SplashScreen
import com.lajara.huellitasvet.viewmodel.InicioViewModel
import com.lajara.huellitasvet.viewmodel.SesionViewModel
import com.lajara.huellitasvet.viewmodel.VetViewModelFactory

@Composable
fun NavVet(sesionViewModel: SesionViewModel) {
    val navController = rememberNavController()
    val context = LocalContext.current

    val perfilActivo by sesionViewModel.perfilActivo.collectAsState()
    val errorMsg by sesionViewModel.error.collectAsState()

    LaunchedEffect(errorMsg) {
        errorMsg?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            sesionViewModel.limpiarError()
        }
    }

    LaunchedEffect(perfilActivo) {
        if (perfilActivo.isNotBlank()) {
            val esVet = perfilActivo.equals("ADMIN", ignoreCase = true) ||
                    perfilActivo.equals("VETERINARIO", ignoreCase = true)
            navController.navigate(if (esVet) Rutas.VET_HOME else Rutas.INICIO_DUENO) {
                popUpTo(Rutas.SPLASH) { inclusive = true }
            }
        } else {
            val rutaActual = navController.currentBackStackEntry?.destination?.route
            if (rutaActual != null && rutaActual != Rutas.SPLASH && rutaActual != Rutas.LOGIN) {
                navController.navigate(Rutas.LOGIN) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }

    NavHost(navController = navController, startDestination = Rutas.SPLASH) {
        composable(Rutas.SPLASH) {
            SplashScreen(
                onRegistrar = { navController.navigate(Rutas.LOGIN) },
                onYaTengoCuenta = { navController.navigate(Rutas.LOGIN) }
            )
        }

        composable(Rutas.LOGIN) {
            LoginScreen(
                onIngresar = { correo, clave, _ -> sesionViewModel.iniciarSesion(correo, clave) },
                onRegistrar = {
                    Toast.makeText(context, "Módulo de Registro en desarrollo", Toast.LENGTH_SHORT).show()
                },
                onOlvideClave = {
                    Toast.makeText(context, "Módulo de Recuperación en desarrollo", Toast.LENGTH_SHORT).show()
                }
            )
        }

        composable(Rutas.INICIO_DUENO) {
            val app = LocalContext.current.applicationContext as VetApp
            val vm: InicioViewModel = viewModel(
                factory = VetViewModelFactory(app.repository, app.preferenciasStore)
            )
            InicioScreen(
                viewModel = vm,
                onIrAMascotas = { navController.navigate(Rutas.MASCOTAS) },
                onIrANuevaCita = { navController.navigate(Rutas.PEDIR_CITA) },
                onIrAMisCitas = { navController.navigate(Rutas.MIS_CITAS) },
                onIrAAsistenteIa = { navController.navigate(Rutas.ASISTENTE_IA) },
                onIrAPerfil = { navController.navigate(Rutas.PERFIL) }
            )
        }

        composable(Rutas.VET_HOME) {
            PantallaTemporal(
                titulo = "Panel del Veterinario",
                subtitulo = "Módulo listo para el Paso 9",
                textoBoton = "Cerrar sesión",
                onAccion = { sesionViewModel.cerrarSesion() }
            )
        }

        composable(Rutas.MASCOTAS) {
            PantallaTemporal("Mis Mascotas", "Pantalla en desarrollo") { navController.popBackStack() }
        }
        composable(Rutas.PEDIR_CITA) {
            PantallaTemporal("Pedir Cita", "Pantalla en desarrollo") { navController.popBackStack() }
        }
        composable(Rutas.MIS_CITAS) {
            PantallaTemporal("Mis Citas", "Pantalla en desarrollo") { navController.popBackStack() }
        }
        composable(Rutas.ASISTENTE_IA) {
            PantallaTemporal("Asistente IA", "Pantalla en desarrollo") { navController.popBackStack() }
        }
        composable(Rutas.PERFIL) {
            PantallaTemporal(
                titulo = "Perfil",
                subtitulo = "Pantalla en desarrollo",
                textoBoton = "Cerrar sesión",
                onAccion = { sesionViewModel.cerrarSesion() }
            )
        }
    }
}

@Composable
fun PantallaTemporal(
    titulo: String,
    subtitulo: String,
    textoBoton: String = "Volver",
    onAccion: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = titulo, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = subtitulo, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onAccion) { Text(textoBoton) }
    }
}