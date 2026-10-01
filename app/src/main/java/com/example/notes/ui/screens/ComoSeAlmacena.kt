package com.example.notes.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.notes.R
import com.example.notes.ui.components.BotonPixel
import com.example.notes.ui.components.EncabezadoPixel
import com.example.notes.ui.components.Icono
import com.example.notes.ui.components.PantallaPixel
import com.example.notes.ui.components.TarjetaPixel
import com.example.notes.ui.theme.BlancoPixel
import com.example.notes.ui.theme.RosaBorde
import com.example.notes.ui.theme.RosaFondo
import com.example.notes.ui.theme.VerdeOscuro
import com.example.notes.ui.theme.VerdePrimario
import com.example.notes.ui.theme.VerdeTarjeta

/** Pantalla 7: explica los dos medios de almacenamiento que compara la actividad. */
@Composable
fun PantallaComoSeAlmacena(
    onAtras: () -> Unit,
    onDemostracion: () -> Unit
) {
    PantallaPixel {
        EncabezadoPixel(
            titulo = "¿Cómo se almacena?",
            icono = R.drawable.ic_regadera,
            onAtras = onAtras
        )
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp)
        ) {
            BloqueAlmacenamiento(
                icono = R.drawable.ic_candado,
                titulo = "Almacenamiento interno",
                subtitulo = "(Privado + Cifrado)",
                cuerpo = "La nota se guarda en el almacenamiento interno del dispositivo " +
                    "usando EncryptedSharedPreferences. Las claves se cifran con AES256-SIV " +
                    "y los valores con AES256-GCM. Solo la aplicación puede leerla.",
                fondo = VerdeTarjeta,
                borde = MaterialTheme.colorScheme.outline
            )

            Spacer(Modifier.height(14.dp))
            BloqueAlmacenamiento(
                icono = R.drawable.ic_nota_rosa,
                titulo = "Almacenamiento externo",
                subtitulo = "(Sin cifrar)",
                cuerpo = "Se crea una copia de la nota en un directorio externo " +
                    "(simulado o tarjeta SD) sin cifrado, para comparar la exposición " +
                    "de la información.",
                fondo = RosaFondo,
                borde = RosaBorde
            )

            Spacer(Modifier.height(16.dp))
            TarjetaPixel(
                color = MaterialTheme.colorScheme.surface,
                borde = MaterialTheme.colorScheme.outline
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icono(R.drawable.ic_campana, 22.dp)
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = "La copia externa no se usa en la app. Solo sirve para la " +
                            "demostración de seguridad.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(20.dp))
            BotonPixel(
                texto = "Ver la demostración",
                onClick = onDemostracion,
                colorFondo = VerdePrimario,
                colorTexto = BlancoPixel,
                icono = R.drawable.ic_pulverizador
            )
            Spacer(Modifier.height(22.dp))
        }
    }
}

@Composable
private fun BloqueAlmacenamiento(
    @DrawableRes icono: Int,
    titulo: String,
    subtitulo: String,
    cuerpo: String,
    fondo: Color,
    borde: Color
) {
    TarjetaPixel(color = fondo, borde = borde) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icono(icono, 28.dp)
            Spacer(Modifier.width(10.dp))
            Column {
                Text(titulo, style = MaterialTheme.typography.titleLarge, color = VerdeOscuro)
                Text(subtitulo, style = MaterialTheme.typography.labelSmall, color = VerdeOscuro)
            }
        }
        Spacer(Modifier.height(10.dp))
        Text(text = cuerpo, style = MaterialTheme.typography.bodyMedium, color = VerdeOscuro)
    }
}
