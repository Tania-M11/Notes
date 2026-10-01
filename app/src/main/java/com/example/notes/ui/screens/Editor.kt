package com.example.notes.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.notes.R
import com.example.notes.data.Note
import com.example.notes.ui.components.BotonPixel
import com.example.notes.ui.components.EncabezadoPixel
import com.example.notes.ui.components.Icono
import com.example.notes.ui.components.PantallaPixel
import com.example.notes.ui.theme.BlancoPixel
import com.example.notes.ui.theme.NegroPixel

private const val MAXIMO_CONTENIDO = 500
private const val MAXIMO_TITULO = 60

/** Pantalla 3: crear o editar una nota. */
@Composable
fun PantallaEditor(
    notaExistente: Note?,
    onAtras: () -> Unit,
    onGuardar: (titulo: String, contenido: String) -> Unit
) {
    var titulo by rememberSaveable(notaExistente?.id) {
        mutableStateOf(notaExistente?.titulo.orEmpty())
    }
    var contenido by rememberSaveable(notaExistente?.id) {
        mutableStateOf(notaExistente?.contenido.orEmpty())
    }
    val hayAlgo = titulo.isNotBlank() || contenido.isNotBlank()

    PantallaPixel {
        EncabezadoPixel(
            titulo = if (notaExistente == null) "Nueva nota" else "Editar nota",
            onAtras = onAtras
        )
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp)
        ) {
            Spacer(Modifier.height(18.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Icono(R.drawable.ic_nota_blanca, 84.dp)
            }
            Spacer(Modifier.height(18.dp))

            Etiqueta("Título")
            CampoPixel(
                valor = titulo,
                onValorCambia = { if (it.length <= MAXIMO_TITULO) titulo = it },
                marcador = "Credenciales servidor",
                alturaMinima = 52.dp,
                unaLinea = true
            )

            Spacer(Modifier.height(16.dp))
            Etiqueta("Contenido")
            CampoPixel(
                valor = contenido,
                onValorCambia = { if (it.length <= MAXIMO_CONTENIDO) contenido = it },
                marcador = "Usuario: admin\nClave: ...\nServidor: ...",
                alturaMinima = 140.dp,
                unaLinea = false
            )
            Text(
                text = "${contenido.length}/$MAXIMO_CONTENIDO",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth().padding(top = 6.dp, end = 4.dp),
                textAlign = TextAlign.End
            )

            Spacer(Modifier.height(26.dp))
            BotonPixel(
                texto = "Guardar",
                onClick = { onGuardar(titulo, contenido) },
                colorFondo = NegroPixel,
                colorTexto = BlancoPixel,
                icono = R.drawable.ic_disquete,
                habilitado = hayAlgo
            )
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun Etiqueta(texto: String) {
    Text(
        text = texto,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

/** Campo de texto con el marco cuadrado del mockup en vez del subrayado de Material. */
@Composable
private fun CampoPixel(
    valor: String,
    onValorCambia: (String) -> Unit,
    marcador: String,
    alturaMinima: Dp,
    unaLinea: Boolean
) {
    val forma = RoundedCornerShape(10.dp)
    BasicTextField(
        value = valor,
        onValueChange = onValorCambia,
        singleLine = unaLinea,
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onSurface
        ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = alturaMinima)
            .background(MaterialTheme.colorScheme.surface, forma)
            .border(2.dp, MaterialTheme.colorScheme.outline, forma)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        decorationBox = { campo ->
            Box(contentAlignment = if (unaLinea) Alignment.CenterStart else Alignment.TopStart) {
                if (valor.isEmpty()) {
                    Text(
                        text = marcador,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
                campo()
            }
        }
    )
}
