package com.lajara.huellitasvet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.lajara.huellitasvet.ui.navigation.NavVet
import com.lajara.huellitasvet.ui.theme.HuellitasVetTheme
import com.lajara.huellitasvet.viewmodel.SesionViewModel
import com.lajara.huellitasvet.viewmodel.VetViewModelFactory

class MainActivity : ComponentActivity() {

    private val sesionViewModel: SesionViewModel by viewModels {
        val app = application as VetApp
        VetViewModelFactory(app.repository, app.preferenciasStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val modoOscuro by sesionViewModel.modoOscuro.collectAsState()
            HuellitasVetTheme(darkTheme = modoOscuro) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavVet(sesionViewModel = sesionViewModel)
                }
            }
        }
    }
}