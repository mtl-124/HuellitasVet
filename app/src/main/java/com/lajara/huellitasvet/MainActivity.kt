package com.lajara.huellitasvet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.lajara.huellitasvet.ui.dueno.LoginScreen
import com.lajara.huellitasvet.ui.dueno.SplashScreen
import com.lajara.huellitasvet.ui.theme.HuellitasVetTheme
import com.lajara.huellitasvet.model.Rol

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HuellitasVetTheme {

                var pantalla by remember { mutableStateOf("splash") }

                when (pantalla) {

                    "splash" -> {
                        SplashScreen(
                            onRegistrar = {
                                pantalla = "login"
                            },
                            onYaTengoCuenta = {
                                pantalla = "login"
                            }
                        )
                    }

                    "login" -> {
                        LoginScreen(
                            onIngresar = { correo, clave, rol ->
                                // Aquí después conectaremos el login con el ViewModel
                            },
                            onRegistrar = {
                                // Aquí después conectaremos la pantalla de registro
                            },
                            onOlvideClave = {
                                // Aquí después conectaremos recuperar contraseña
                            }
                        )
                    }
                }
            }
        }
    }
}