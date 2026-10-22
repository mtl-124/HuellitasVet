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

// Esquemas azules del módulo del veterinario
private val VetLightColorScheme = lightColorScheme(
    primary = AzulPrimario,
    onPrimary = Color.White,
    primaryContainer = AzulContenedor,
    onPrimaryContainer = AzulSobreContenedor,
    secondaryContainer = AzulContenedor,
    onSecondaryContainer = AzulSobreContenedor,
    tertiaryContainer = AmbarContenedor,
    onTertiaryContainer = AmbarTexto,
    background = Color.White,
    onBackground = TintaTexto,
    surface = Color.White,
    onSurface = TintaTexto,
    onSurfaceVariant = GrisTexto,
    surfaceContainer = Color.White,
    surfaceContainerLowest = Color.White,
    outlineVariant = GrisBorde
)

private val VetDarkColorScheme = darkColorScheme(
    primary = AzulPrimarioOscuro,
    onPrimary = Color(0xFF0B1E4A),
    primaryContainer = AzulSobreContenedor,
    onPrimaryContainer = AzulContenedor,
    secondaryContainer = Color(0xFF1E2A47),
    onSecondaryContainer = AzulContenedor,
    tertiaryContainer = Color(0xFF3A2F10),
    onTertiaryContainer = Color(0xFFFCD34D),
    background = Color(0xFF0B1220),
    onBackground = Color(0xFFE5EAF5),
    surface = Color(0xFF0B1220),
    onSurface = Color(0xFFE5EAF5),
    onSurfaceVariant = Color(0xFF9AA8C7),
    surfaceContainer = Color(0xFF111A2E),
    surfaceContainerLowest = Color(0xFF16203A),
    surfaceContainerLow = Color(0xFF0F1729),
    surfaceContainerHigh = Color(0xFF1E2A47),
    surfaceContainerHighest = Color(0xFF26345A),
    outline = Color(0xFF4A5A80),
    outlineVariant = Color(0xFF2B3A5E)
)

@Composable
fun HuellitasVetTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    veterinario: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        veterinario -> if (darkTheme) VetDarkColorScheme else VetLightColorScheme
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
val ColorScheme.esVeterinario: Boolean get() = primary == AzulPrimario || primary == AzulPrimarioOscuro
val ColorScheme.tarjeta: Color
    get() = when {
        esClaro -> Color.White
        esVeterinario -> Color(0xFF16203A)
        else -> Color(0xFF1A2B24)
    }
val ColorScheme.menta: Color get() = if (esClaro) MentaTarjeta else Color(0xFF14382B)
val ColorScheme.mentaBorde: Color get() = if (esClaro) MentaBorde else Color(0xFF245C46)
val ColorScheme.bordeSuave: Color
    get() = when {
        esClaro -> GrisBorde
        esVeterinario -> Color(0xFF2B3A5E)
        else -> Color(0xFF34463E)
    }
val ColorScheme.fondoInactivo: Color
    get() = when {
        esClaro -> Color(0xFFEEF0F2)
        esVeterinario -> Color(0xFF1E2A47)
        else -> Color(0xFF22312B)
    }
val ColorScheme.fondoPestanas: Color get() = if (esClaro) Color(0xFFE6F0EB) else Color(0xFF15231D)
val ColorScheme.ambar: Color get() = if (esClaro) AmbarContenedor else Color(0xFF3A2F10)
val ColorScheme.ambarBorde: Color get() = if (esClaro) AmbarBorde else Color(0xFF5C4A14)
val ColorScheme.ambarTexto: Color get() = if (esClaro) AmbarTexto else Color(0xFFFCD34D)
val ColorScheme.rojo: Color get() = if (esClaro) Color(0xFFDC2626) else Color(0xFFF87171)