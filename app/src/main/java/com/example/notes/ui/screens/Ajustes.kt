package com.example.notes.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.notes.R
import com.example.notes.ui.components.BarraNavegacion
import com.example.notes.ui.components.ChevronDerecha
import com.example.notes.ui.components.EncabezadoPixel
import com.example.notes.ui.components.Icono
import com.example.notes.ui.components.PantallaPixel
import com.example.notes.ui.components.TarjetaPixel
import com.example.notes.ui.theme.VerdeOscuro

/** Pantalla 6: ajustes. */
@Composable
fun PantallaAjustes(
    onAtras: () -> Unit,
    onSeguridad: () -> Unit,
    onNotas: () -> Unit,
    onNuevaNota: () -> Unit
) {
    PantallaPixel(
        barraInferior = {
            BarraNavegacion(
                destinoActual = "ajustes",
                onNotas = onNotas,
                onNueva = onNuevaNota,
                onAjustes = {}
            )
        }
    ) {
        EncabezadoPixel(titulo = "Ajustes", onAtras = onAtras)

        Column(
            Modifier.fillMaxSize().padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FilaAjuste(
                icono = R.drawable.ic_candado,
                titulo = "Seguridad",
                subtitulo = "Cifrado y privacidad",
                onClick = onSeguridad
            )
            FilaAjuste(
                icono = R.drawable.ic_cactus,
                titulo = "Acerca de",
                subtitulo = "Versión 1.0.0",
                onClick = null
            )

            Spacer(Modifier.height(10.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icono(R.drawable.ic_chica, 110.dp)
                Spacer(Modifier.width(10.dp))
                Bocadillo("La seguridad también es parte del cuidado.")
            }
        }
    }
}

@Composable
private fun FilaAjuste(
    @DrawableRes icono: Int,
    titulo: String,
    subtitulo: String,
    onClick: (() -> Unit)?
) {
    TarjetaPixel(
        color = MaterialTheme.colorScheme.surface,
        borde = MaterialTheme.colorScheme.outline,
        onClick = onClick
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icono(icono, 28.dp)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    titulo,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    subtitulo,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (onClick != null) ChevronDerecha()
        }
    }
}

@Composable
private fun Bocadillo(texto: String) {
    val forma = RoundedCornerShape(12.dp)
    Text(
        text = texto,
        style = MaterialTheme.typography.bodyMedium,
        color = VerdeOscuro,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface, forma)
            .border(2.dp, MaterialTheme.colorScheme.outline, forma)
            .padding(12.dp)
    )
}
