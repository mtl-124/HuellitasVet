package com.lajara.huellitasvet.ui.components.graficos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VetTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    error: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    var enfocado by remember { mutableStateOf(false) }
    val forma = RoundedCornerShape(10.dp)
    val colores = MaterialTheme.colorScheme
    val colorBorde = when {
        error != null -> colores.error
        enfocado -> colores.primary
        else -> colores.outlineVariant
    }

    Column(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(forma)
                .background(Color.White)
                .border(if (enfocado || error != null) 1.5.dp else 1.dp, colorBorde, forma)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(label, fontSize = 10.sp, color = colores.onSurfaceVariant)
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = colores.onSurface
                ),
                cursorBrush = SolidColor(colores.primary),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { enfocado = it.isFocused }
            )
        }
        if (error != null) {
            Text(
                text = error,
                color = colores.error,
                fontSize = 11.sp,
                modifier = Modifier.padding(start = 4.dp, top = 3.dp)
            )
        }
    }
}