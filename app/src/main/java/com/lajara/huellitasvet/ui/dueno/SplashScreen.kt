package com.lajara.huellitasvet.ui.dueno

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val FondoMenta = Color(0xFFDDF5EA)
private val VerdeMarca = Color(0xFF12B76A)
private val TintaSplash = Color(0xFF1F2937)
private val GrisSplash = Color(0xFF4B5563)
private val TarjetaMascota = Color(0xFFCDEFE0)
private val BordeSeleccion = Color(0xFF2F8F7A)

private val mascotas = listOf("🐶", "🐱", "🐕")

@Composable
fun SplashScreen(
    onRegistrar: () -> Unit,
    onYaTengoCuenta: () -> Unit
) {
    var seleccion by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoMenta)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(26.dp))
                    .background(VerdeMarca),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Pets,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(58.dp)
                )
            }

            Spacer(Modifier.height(20.dp))
            Text(
                "Huellitas Vet",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = TintaSplash
            )
            Spacer(Modifier.height(6.dp))
            Text("Cuídanos y da amor extra", fontSize = 15.sp, color = GrisSplash)

            Spacer(Modifier.height(32.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                mascotas.forEachIndexed { i, emoji ->
                    TarjetaMascotaSplash(
                        emoji = emoji,
                        seleccionada = i == seleccion,
                        conPunto = i == 1,
                        onClick = { seleccion = i }
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = onRegistrar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeMarca,
                    contentColor = Color.White
                )
            ) {
                Text("REGISTRARSE", fontSize = 14.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.8.sp)
            }
            Spacer(Modifier.height(8.dp))
            TextButton(onClick = onYaTengoCuenta) {
                Text("Ya tengo una cuenta", fontSize = 13.sp, color = TintaSplash)
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun TarjetaMascotaSplash(
    emoji: String,
    seleccionada: Boolean,
    conPunto: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(76.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(TarjetaMascota)
            .then(
                if (seleccionada) Modifier.border(
                    BorderStroke(2.dp, BordeSeleccion),
                    RoundedCornerShape(18.dp)
                ) else Modifier
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(emoji, fontSize = 38.sp)
        if (conPunto) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(VerdeMarca)
            )
        }
    }
}