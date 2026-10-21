package com.lajara.huellitasvet.ui.dueno

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lajara.huellitasvet.model.Rol

private val VerdeDueno = Color(0xFF059669)
private val AzulVet = Color(0xFF1D4ED8)
private val Tinta = Color(0xFF111827)
private val Gris = Color(0xFF6B7280)
private val BordeCampo = Color(0xFFE5E7EB)
private val FondoToggle = Color(0xFFF1F3F5)
private val FondoInfo = Color(0xFFEFF4FF)

@Composable
fun LoginScreen(
    onIngresar: (correo: String, clave: String, rol: Rol) -> Unit,
    onRegistrar: () -> Unit,
    onOlvideClave: () -> Unit = {}
) {
    var rol by rememberSaveable { mutableStateOf(Rol.CLIENTE) }
    val esDueno = rol == Rol.CLIENTE
    val acento by animateColorAsState(if (esDueno) VerdeDueno else AzulVet, label = "acento")

    var correo by remember(rol) {
        mutableStateOf(if (esDueno) "ana@correo.test" else "vet@huellitas.test")
    }
    var clave by remember(rol) { mutableStateOf(if (esDueno) "1234" else "admin") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp)
    ) {
        Spacer(Modifier.height(24.dp))
        Text(
            "Bienvenido",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Tinta
        )
        Spacer(Modifier.height(4.dp))
        Text("Ingresa para continuar", fontSize = 14.sp, color = Gris)

        Spacer(Modifier.height(24.dp))
        SelectorRol(rol = rol, acento = acento, onCambio = { rol = it })

        Spacer(Modifier.height(24.dp))
        CampoLogin(
            valor = correo,
            onCambio = { correo = it },
            etiqueta = "Correo",
            icono = { Icon(Icons.Outlined.Email, null) },
            acento = acento
        )
        Spacer(Modifier.height(12.dp))
        CampoLogin(
            valor = clave,
            onCambio = { clave = it },
            etiqueta = "Contraseña",
            icono = { Icon(Icons.Outlined.Lock, null) },
            acento = acento,
            oculto = true
        )

        Spacer(Modifier.height(10.dp))
        Text(
            "¿Olvidaste tu contraseña?",
            color = acento,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.End)
                .clickable { onOlvideClave() }
        )

        Spacer(Modifier.height(24.dp))
        Button(
            onClick = { onIngresar(correo, clave, rol) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = acento, contentColor = Color.White)
        ) {
            Text("Ingresar", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(20.dp))
        if (esDueno) {
            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "¿No tienes cuenta?",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerdeDueno
                )
                Text(
                    "  Regístrate",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerdeDueno,
                    modifier = Modifier.clickable { onRegistrar() }
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(FondoInfo)
                    .padding(14.dp)
            ) {
                Text(
                    "Modo práctica",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulVet
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Puedes cambiar de perfil desde\nAjustes para probar ambos roles.",
                    fontSize = 12.sp,
                    color = Gris,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

@Composable
private fun SelectorRol(rol: Rol, acento: Color, onCambio: (Rol) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(FondoToggle)
            .padding(4.dp)
    ) {
        OpcionRol("Dueño", rol == Rol.CLIENTE, acento) { onCambio(Rol.CLIENTE) }
        OpcionRol("Veterinario", rol == Rol.ADMIN, acento) { onCambio(Rol.ADMIN) }
    }
}

@Composable
private fun RowScope.OpcionRol(
    texto: String,
    seleccionado: Boolean,
    acento: Color,
    onClick: () -> Unit
) {
    val fondo by animateColorAsState(if (seleccionado) acento else Color.Transparent, label = "fondo")
    val color by animateColorAsState(if (seleccionado) Color.White else Gris, label = "texto")
    Box(
        modifier = Modifier
            .weight(1f)
            .height(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(fondo)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(texto, color = color, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun CampoLogin(
    valor: String,
    onCambio: (String) -> Unit,
    etiqueta: String,
    icono: @Composable () -> Unit,
    acento: Color,
    oculto: Boolean = false
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(etiqueta, fontSize = 12.sp) },
        leadingIcon = icono,
        singleLine = true,
        visualTransformation = if (oculto) PasswordVisualTransformation() else VisualTransformation.None,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = acento,
            unfocusedBorderColor = BordeCampo,
            focusedLabelColor = acento,
            unfocusedLabelColor = Gris,
            focusedLeadingIconColor = acento,
            unfocusedLeadingIconColor = acento,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            cursorColor = acento
        )
    )
}