package com.example.notes.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.notes.R
import com.example.notes.ui.components.BotonPixel
import com.example.notes.ui.components.Icono
import com.example.notes.ui.components.PantallaPixel
import com.example.notes.ui.theme.BlancoPixel
import com.example.notes.ui.theme.NegroPixel
import com.example.notes.ui.theme.VerdePrimario
import com.example.notes.ui.theme.VerdeTexto

/**
 * Pantalla 1: portada. El arte completo viene de fondo_inicio.jpg, asi que la
 * pantalla solo coloca encima el titulo y el boton.
 */
@Composable
fun PantallaInicio(onComenzar: () -> Unit) {
    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.fondo_inicio),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // La maceta del arte llega hasta un 45% de la altura: el texto
            // arranca por debajo para no montarse encima.
            Spacer(Modifier.weight(0.58f))
            Text(
                text = "NOTAS\nCONFIDENCIALES",
                style = MaterialTheme.typography.displaySmall,
                color = NegroPixel,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Tus ideas, tus datos,\nsiempre protegidos.",
                style = MaterialTheme.typography.bodyLarge,
                color = VerdeTexto,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(34.dp))
            BotonPixel(
                texto = "Comenzar",
                onClick = onComenzar,
                colorFondo = NegroPixel,
                colorTexto = BlancoPixel,
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            Spacer(Modifier.weight(0.42f))
        }
    }
}

/** Pantalla 5: confirmacion tras guardar, con el mensaje de los dos medios. */
@Composable
fun PantallaGuardada(onVerNotas: () -> Unit, onComoSeAlmacena: () -> Unit) {
    PantallaPixel {
        Column(
            Modifier.fillMaxSize().padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icono(R.drawable.ic_maceta, 120.dp)
            Spacer(Modifier.height(22.dp))
            Text(
                text = "¡Nota guardada!",
                style = MaterialTheme.typography.headlineMedium,
                color = NegroPixel,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(14.dp))
            Text(
                text = "Se ha almacenado de forma segura en el almacenamiento " +
                    "interno (cifrado) y también se ha creado una copia en el " +
                    "almacenamiento externo (sin cifrar) para fines de demostración.",
                style = MaterialTheme.typography.bodyMedium,
                color = VerdeTexto,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(28.dp))
            BotonPixel(
                texto = "Ver mis notas",
                onClick = onVerNotas,
                colorFondo = VerdePrimario,
                colorTexto = BlancoPixel,
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            Spacer(Modifier.height(12.dp))
            BotonPixel(
                texto = "¿Cómo se almacena?",
                onClick = onComoSeAlmacena,
                colorFondo = MaterialTheme.colorScheme.surface,
                colorTexto = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth(0.8f),
                icono = R.drawable.ic_candado
            )
        }
    }
}
