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
import androidx.compose.material3.Surface
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lajara.huellitasvet.VetApp
import com.lajara.huellitasvet.ui.dueno.FichaMascotaScreen
import com.lajara.huellitasvet.ui.dueno.FormularioMascotaScreen
import com.lajara.huellitasvet.ui.dueno.InicioScreen
import com.lajara.huellitasvet.ui.dueno.LoginScreen
import com.lajara.huellitasvet.ui.dueno.MascotasScreen
import com.lajara.huellitasvet.ui.dueno.MisCitasScreen
import com.lajara.huellitasvet.ui.dueno.PedirCitaScreen
import com.lajara.huellitasvet.ui.dueno.PerfilScreen
import com.lajara.huellitasvet.ui.dueno.SplashScreen
import com.lajara.huellitasvet.ui.theme.HuellitasVetTheme
import com.lajara.huellitasvet.ui.veterinario.AgendaScreen
import com.lajara.huellitasvet.ui.veterinario.PanelScreen
import com.lajara.huellitasvet.viewmodel.AgendaViewModel
import com.lajara.huellitasvet.viewmodel.InicioViewModel
import com.lajara.huellitasvet.viewmodel.MascotaViewModel
import com.lajara.huellitasvet.viewmodel.MisCitasViewModel
import com.lajara.huellitasvet.viewmodel.PanelViewModel
import com.lajara.huellitasvet.viewmodel.PedirCitaViewModel
import com.lajara.huellitasvet.viewmodel.SesionViewModel
import com.lajara.huellitasvet.viewmodel.VetViewModelFactory

@Composable
fun NavVet(sesionViewModel: SesionViewModel) {
    val navController = rememberNavController()
    val context = LocalContext.current

    val perfilActivo by sesionViewModel.perfilActivo.collectAsState()
    val errorMsg by sesionViewModel.error.collectAsState()
    val modoOscuro by sesionViewModel.modoOscuro.collectAsState()

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
                popUpTo(0) { inclusive = true }
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

    // Navegación entre pestañas del veterinario (la raíz siempre es el Panel)
    val irAPanelVet: () -> Unit = { navController.popBackStack(Rutas.VET_HOME, false) }
    val irATabVet: (String) -> Unit = { ruta ->
        navController.navigate(ruta) {
            popUpTo(Rutas.VET_HOME)
            launchSingleTop = true
        }
    }

    NavHost(navController = navController, startDestination = Rutas.SPLASH) {
        composable(Rutas.SPLASH) {
            HuellitasVetTheme(darkTheme = false) {
                SplashScreen(
                    onRegistrar = { navController.navigate(Rutas.LOGIN) },
                    onYaTengoCuenta = { navController.navigate(Rutas.LOGIN) }
                )
            }
        }

        composable(Rutas.LOGIN) {
            HuellitasVetTheme(darkTheme = false) {
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
                onIrAPerfil = { navController.navigate(Rutas.PERFIL) },
                onIrAFicha = { id -> navController.navigate(Rutas.fichaMascota(id)) },
                onAgregarMascota = { navController.navigate(Rutas.formularioMascota()) }
            )
        }

        composable(
            route = Rutas.FICHA_MASCOTA,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entrada ->
            val id = entrada.arguments?.getInt("id") ?: 0
            val app = LocalContext.current.applicationContext as VetApp
            val vm: MascotaViewModel = viewModel(
                factory = VetViewModelFactory(app.repository, app.preferenciasStore)
            )
            FichaMascotaScreen(
                viewModel = vm,
                mascotaId = id,
                onVolver = { navController.popBackStack() },
                onPedirCita = { navController.navigate(Rutas.PEDIR_CITA) },
                onEditar = { navController.navigate(Rutas.formularioMascota(id)) },
                onIrAInicio = { navController.popBackStack(Rutas.INICIO_DUENO, false) },
                onIrAMisCitas = { navController.navigate(Rutas.MIS_CITAS) },
                onIrAPerfil = { navController.navigate(Rutas.PERFIL) }
            )
        }

        composable(
            route = Rutas.FORMULARIO_MASCOTA,
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) { entrada ->
            val id = entrada.arguments?.getInt("id")?.takeIf { it > 0 }
            val app = LocalContext.current.applicationContext as VetApp
            val vm: MascotaViewModel = viewModel(
                factory = VetViewModelFactory(app.repository, app.preferenciasStore)
            )
            FormularioMascotaScreen(
                viewModel = vm,
                mascotaId = id,
                onVolver = { navController.popBackStack() },
                onGuardado = { navController.popBackStack() },
                onEliminada = { navController.popBackStack(Rutas.INICIO_DUENO, false) }
            )
        }

        // ---------- Veterinario (tema azul) ----------
        composable(Rutas.VET_HOME) {
            val app = LocalContext.current.applicationContext as VetApp
            val vm: PanelViewModel = viewModel(
                factory = VetViewModelFactory(app.repository, app.preferenciasStore)
            )
            TemaVet(modoOscuro) {
                PanelScreen(
                    viewModel = vm,
                    onIrAAgenda = { irATabVet(Rutas.VET_AGENDA) },
                    onIrAPacientes = { irATabVet(Rutas.VET_PACIENTES) },
                    onIrAServicios = { irATabVet(Rutas.VET_SERVICIOS) },
                    onIrAAjustes = { irATabVet(Rutas.VET_AJUSTES) }
                )
            }
        }

        composable(Rutas.VET_AGENDA) {
            val app = LocalContext.current.applicationContext as VetApp
            val vm: AgendaViewModel = viewModel(
                factory = VetViewModelFactory(app.repository, app.preferenciasStore)
            )
            TemaVet(modoOscuro) {
                AgendaScreen(
                    viewModel = vm,
                    onVolver = { navController.popBackStack() },
                    onAtender = {
                        Toast.makeText(context, "Atención de citas: siguiente paso", Toast.LENGTH_SHORT).show()
                    },
                    onIrAPanel = irAPanelVet,
                    onIrAPacientes = { irATabVet(Rutas.VET_PACIENTES) },
                    onIrAServicios = { irATabVet(Rutas.VET_SERVICIOS) },
                    onIrAAjustes = { irATabVet(Rutas.VET_AJUSTES) }
                )
            }
        }

        composable(Rutas.VET_PACIENTES) {
            TemaVet(modoOscuro) {
                PantallaTemporal("Pacientes", "Módulo en desarrollo") { navController.popBackStack() }
            }
        }

        composable(Rutas.VET_SERVICIOS) {
            TemaVet(modoOscuro) {
                PantallaTemporal("Servicios", "Módulo en desarrollo") { navController.popBackStack() }
            }
        }

        composable(Rutas.VET_AJUSTES) {
            TemaVet(modoOscuro) {
                PantallaTemporal(
                    titulo = "Ajustes",
                    subtitulo = "Módulo en desarrollo",
                    textoBoton = "Cerrar sesión",
                    onAccion = { sesionViewModel.cerrarSesion() }
                )
            }
        }

        composable(Rutas.MASCOTAS) {
            val app = LocalContext.current.applicationContext as VetApp
            val vm: MascotaViewModel = viewModel(
                factory = VetViewModelFactory(app.repository, app.preferenciasStore)
            )
            MascotasScreen(
                viewModel = vm,
                onVolver = { navController.popBackStack() },
                onIrAFicha = { id -> navController.navigate(Rutas.fichaMascota(id)) },
                onAgregarMascota = { navController.navigate(Rutas.formularioMascota()) }
            )
        }
        composable(Rutas.PEDIR_CITA) {
            val app = LocalContext.current.applicationContext as VetApp
            val vm: PedirCitaViewModel = viewModel(
                factory = VetViewModelFactory(app.repository, app.preferenciasStore)
            )
            PedirCitaScreen(
                viewModel = vm,
                onVolver = { navController.popBackStack() },
                onCitaAgendada = {
                    navController.navigate(Rutas.MIS_CITAS) { popUpTo(Rutas.INICIO_DUENO) }
                }
            )
        }
        composable(Rutas.MIS_CITAS) {
            val app = LocalContext.current.applicationContext as VetApp
            val vm: MisCitasViewModel = viewModel(
                factory = VetViewModelFactory(app.repository, app.preferenciasStore)
            )
            MisCitasScreen(
                viewModel = vm,
                onVolver = { navController.popBackStack() },
                onNuevaCita = { navController.navigate(Rutas.PEDIR_CITA) },
                onIrAInicio = { navController.popBackStack(Rutas.INICIO_DUENO, false) },
                onIrAMascotas = { navController.navigate(Rutas.MASCOTAS) },
                onIrAPerfil = { navController.navigate(Rutas.PERFIL) }
            )
        }
        composable(Rutas.ASISTENTE_IA) {
            PantallaTemporal("Asistente IA", "Pantalla en desarrollo") { navController.popBackStack() }
        }
        composable(Rutas.PERFIL) {
            val app = LocalContext.current.applicationContext as VetApp
            val vm: InicioViewModel = viewModel(
                factory = VetViewModelFactory(app.repository, app.preferenciasStore)
            )
            PerfilScreen(
                viewModel = vm,
                sesionViewModel = sesionViewModel,
                onIrAInicio = { navController.popBackStack(Rutas.INICIO_DUENO, false) },
                onIrAMascotas = { navController.navigate(Rutas.MASCOTAS) },
                onIrAMisCitas = { navController.navigate(Rutas.MIS_CITAS) }
            )
        }
    }
}

// Envuelve las pantallas del veterinario con el tema azul (claro u oscuro)
@Composable
private fun TemaVet(
    modoOscuro: Boolean,
    contenido: @Composable () -> Unit
) {
    HuellitasVetTheme(darkTheme = modoOscuro, veterinario = true) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            contenido()
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