package com.example.notes.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.notes.R
import com.example.notes.data.Note
import com.example.notes.ui.components.BarraNavegacion
import com.example.notes.ui.components.BotonPixel
import com.example.notes.ui.components.ChevronDerecha
import com.example.notes.ui.components.EncabezadoPixel
import com.example.notes.ui.components.Icono
import com.example.notes.ui.components.IconoBoton
import com.example.notes.ui.components.PantallaPixel
import com.example.notes.ui.components.TarjetaPixel
import com.example.notes.ui.theme.BlancoPixel
import com.example.notes.ui.theme.VerdePrimario
import kotlin.math.absoluteValue

/** Cada nota recibe uno de los iconos del kit, siempre el mismo para la misma nota. */
fun iconoDeNota(nota: Note): Int {
    val iconos = listOf(
        R.drawable.ic_nota_rosa,
        R.drawable.ic_nota_verde,
        R.drawable.ic_nota_corazon,
        R.drawable.ic_nota_estrella
    )
    return iconos[nota.id.hashCode().absoluteValue % iconos.size]
}

/** Pantalla 2: listado de notas. */
@Composable
fun PantallaLista(
    notas: List<Note>,
    cargando: Boolean,
    onNuevaNota: () -> Unit,
    onAbrirNota: (Note) -> Unit,
    onAjustes: () -> Unit
) {
    PantallaPixel(
        conCesped = false,
        barraInferior = {
            BarraNavegacion(
                destinoActual = "notas",
                onNotas = {},
                onNueva = onNuevaNota,
                onAjustes = onAjustes
            )
        }
    ) {
        EncabezadoPixel(
            titulo = "Mis notas",
            icono = R.drawable.ic_brote,
            accion = { IconoBoton(R.drawable.ic_nav_ajustes, onAjustes, 24.dp) }
        )
        Text(
            text = "Tu información, segura y privada.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 18.dp)
        )
        Spacer(Modifier.height(14.dp))
        BotonPixel(
            texto = "Nueva nota",
            onClick = onNuevaNota,
            colorFondo = VerdePrimario,
            colorTexto = BlancoPixel,
            modifier = Modifier.padding(horizontal = 18.dp),
            icono = R.drawable.ic_mas
        )
        Spacer(Modifier.height(14.dp))

        when {
            cargando -> MensajeCentral("Abriendo el almacén cifrado...")
            notas.isEmpty() -> MensajeCentral(
                "Todavía no hay notas.\nCrea la primera y se guardará cifrada."
            )
            else -> LazyColumn(
                Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(notas, key = { it.id }) { nota ->
                    FilaNota(nota) { onAbrirNota(nota) }
                }
            }
        }
    }
}

@Composable
private fun FilaNota(nota: Note, onClick: () -> Unit) {
    TarjetaPixel(
        color = MaterialTheme.colorScheme.surfaceVariant,
        borde = MaterialTheme.colorScheme.outline,
        onClick = onClick
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icono(iconoDeNota(nota), 30.dp)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = nota.titulo,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = nota.fechaLegible(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            ChevronDerecha()
        }
    }
}

@Composable
private fun MensajeCentral(texto: String) {
    Column(
        Modifier.fillMaxWidth().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}
