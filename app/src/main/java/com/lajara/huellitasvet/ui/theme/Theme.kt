package com.lajara.huellitasvet.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF34D399),
    onPrimary = Color(0xFF003827),
    primaryContainer = Color(0xFF064E3B),
    onPrimaryContainer = Color(0xFFCFF5E3),
    secondaryContainer = Color(0xFF2B4A3D),
    onSecondaryContainer = Color(0xFFD1FAE5),
    tertiaryContainer = Color(0xFF3A2F10),
    onTertiaryContainer = Color(0xFFFCD34D),
    background = Color(0xFF0E1713),
    onBackground = Color(0xFFE5EDE8),
    surface = Color(0xFF0E1713),
    onSurface = Color(0xFFE5EDE8),
    onSurfaceVariant = Color(0xFFA7B5AE),
    surfaceContainer = Color(0xFF15211B),
    surfaceContainerLowest = Color(0xFF1A2B24),
    surfaceContainerLow = Color(0xFF121D18),
    surfaceContainerHigh = Color(0xFF1F332A),
    surfaceContainerHighest = Color(0xFF263D33),
    outline = Color(0xFF5B6E65),
    outlineVariant = Color(0xFF34463E)
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
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Colores que cambian según el tema claro u oscuro
val ColorScheme.esClaro: Boolean get() = background.luminance() > 0.5f
val ColorScheme.tarjeta: Color get() = if (esClaro) Color.White else Color(0xFF1A2B24)
val ColorScheme.menta: Color get() = if (esClaro) MentaTarjeta else Color(0xFF14382B)
val ColorScheme.mentaBorde: Color get() = if (esClaro) MentaBorde else Color(0xFF245C46)
val ColorScheme.bordeSuave: Color get() = if (esClaro) GrisBorde else Color(0xFF34463E)
val ColorScheme.fondoInactivo: Color get() = if (esClaro) Color(0xFFEEF0F2) else Color(0xFF22312B)
val ColorScheme.fondoPestanas: Color get() = if (esClaro) Color(0xFFE6F0EB) else Color(0xFF15231D)
val ColorScheme.ambar: Color get() = if (esClaro) AmbarContenedor else Color(0xFF3A2F10)
val ColorScheme.ambarBorde: Color get() = if (esClaro) AmbarBorde else Color(0xFF5C4A14)
val ColorScheme.ambarTexto: Color get() = if (esClaro) AmbarTexto else Color(0xFFFCD34D)
val ColorScheme.rojo: Color get() = if (esClaro) Color(0xFFDC2626) else Color(0xFFF87171)