package com.lajara.huellitasvet

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lajara.huellitasvet.ui.dueno.LoginScreen
import com.lajara.huellitasvet.ui.dueno.SplashScreen
import com.lajara.huellitasvet.ui.theme.HuellitasVetTheme
import com.lajara.huellitasvet.viewmodel.SesionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as VetApp
        val sesionViewModel = SesionViewModel(app.repository, app.preferenciasStore)

        setContent {
            HuellitasVetTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    VetNavApp(sesionViewModel = sesionViewModel)
                }
            }
        }
    }
}

@Composable
fun VetNavApp(sesionViewModel: SesionViewModel) {
    val navController = rememberNavController()
    val context = LocalContext.current

    val perfilActivo by sesionViewModel.perfilActivo.collectAsState()
    val errorMsg by sesionViewModel.error.collectAsState()

    // 1. Notificar si las credenciales son incorrectas
    LaunchedEffect(errorMsg) {
        errorMsg?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            sesionViewModel.limpiarError()
        }
    }

    // 2. Navegación reactiva según el perfil autenticado en DataStore
    LaunchedEffect(perfilActivo) {
        if (perfilActivo.isNotBlank()) {
            val destino = if (perfilActivo.equals("ADMIN", ignoreCase = true) || perfilActivo.equals("VETERINARIO", ignoreCase = true)) {
                "vet_home"
            } else {
                "dueno_home"
            }
            navController.navigate(destino) {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            // Si la sesión se cierra, regresar limpiamente a la pantalla de Login
            val rutaActual = navController.currentBackStackEntry?.destination?.route
            if (rutaActual != null && rutaActual != "splash" && rutaActual != "login") {
                navController.navigate("login") {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        // 1. Ruta Inicial (Splash)
        composable("splash") {
            SplashScreen(
                onRegistrar = { navController.navigate("login") },
                onYaTengoCuenta = { navController.navigate("login") }
            )
        }

        // 2. Ruta de Autenticación (Login)
        composable("login") {
            LoginScreen(
                onIngresar = { correo, clave, _ ->
                    sesionViewModel.iniciarSesion(correo, clave)
                },
                onRegistrar = {
                    Toast.makeText(context, "Módulo de Registro en desarrollo", Toast.LENGTH_SHORT).show()
                },
                onOlvideClave = {
                    Toast.makeText(context, "Módulo de Recuperación en desarrollo", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // 3. Destino Temporal Módulo Dueño (Paso 8)
        composable("dueno_home") {
            PantallaTemporal(
                titulo = "Panel del Dueño",
                subtitulo = "Módulo listo para el Paso 8",
                onCerrarSesion = { sesionViewModel.cerrarSesion() }
            )
        }

        // 4. Destino Temporal Módulo Veterinario (Paso 9)
        composable("vet_home") {
            PantallaTemporal(
                titulo = "Panel del Veterinario",
                subtitulo = "Módulo listo para el Paso 9",
                onCerrarSesion = { sesionViewModel.cerrarSesion() }
            )
        }
    }
}

@Composable
fun PantallaTemporal(
    titulo: String,
    subtitulo: String,
    onCerrarSesion: () -> Unit
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
        Button(onClick = onCerrarSesion) {
            Text("Cerrar Sesión")
        }
    }
}