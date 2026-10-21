package com.lajara.huellitasvet.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF34D399),
    onPrimary = Color(0xFF003827),
    primaryContainer = Color(0xFF064E3B),
    onPrimaryContainer = Color(0xFFCFF5E3)
)

private val LightColorScheme = lightColorScheme(
    primary = VerdePrimario,
    onPrimary = Color.White,
    primaryContainer = VerdeContenedor,
    onPrimaryContainer = VerdeSobreContenedor,
    secondaryContainer = LavandaSeleccion,
    onSecondaryContainer = LavandaIcono,
    tertiaryContainer = AmbarContenedor,
    onTertiaryContainer = AmbarTexto,
    background = MentaFondo,
    onBackground = TintaTexto,
    surface = MentaFondo,
    onSurface = TintaTexto,
    onSurfaceVariant = GrisTexto,
    surfaceContainer = MentaFondo,
    surfaceContainerLowest = Color.White,
    outlineVariant = GrisBorde
)

@Composable
fun HuellitasVetTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}