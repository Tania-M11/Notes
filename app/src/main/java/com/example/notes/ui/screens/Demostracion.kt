package com.example.notes.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.notes.R
import com.example.notes.data.Inspeccion
import com.example.notes.ui.components.BotonPixel
import com.example.notes.ui.components.EncabezadoPixel
import com.example.notes.ui.components.Icono
import com.example.notes.ui.components.PantallaPixel
import com.example.notes.ui.components.TarjetaPixel
import com.example.notes.ui.theme.BlancoPixel
import com.example.notes.ui.theme.NegroPixel
import com.example.notes.ui.theme.RosaBorde
import com.example.notes.ui.theme.RosaFondo
import com.example.notes.ui.theme.RosaFuerte
import com.example.notes.ui.theme.VerdeOscuro
import com.example.notes.ui.theme.VerdePrimario
import com.example.notes.ui.theme.VerdeTarjeta

/** Cuanto contenido de cada archivo se muestra antes de cortar. */
private const val MAXIMO_VISIBLE = 520

/**
 * Pantalla 8: la demostracion del punto 4.
 *
 * Lee del disco los dos archivos tal como estan y los enfrenta. El interno lo
 * escribio EncryptedSharedPreferences, asi que se muestra dos veces: crudo
 * (base64, lo que veria quien lo extraiga) y descifrado por la libreria (lo
 * que ve la app). El veredicto no esta escrito a mano: se busca el texto de
 * una nota real dentro de cada archivo.
 */
@Composable
fun PantallaDemostracion(
    interna: Inspeccion?,
    externa: Inspeccion?,
    onCargar: () -> Unit,
    onAtras: () -> Unit
) {
    LaunchedEffect(Unit) { onCargar() }

    val termino = interna?.termino ?: externa?.termino

    PantallaPixel(conCesped = false) {
        EncabezadoPixel(
            titulo = "Demostración",
            icono = R.drawable.ic_pulverizador,
            onAtras = onAtras
        )
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp)
        ) {
            Text(
                text = "La misma nota, escrita en los dos medios. Esto es el contenido real " +
                    "de cada archivo, leído del disco ahora mismo.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (interna != null && termino == null) {
                Spacer(Modifier.height(12.dp))
                Aviso(
                    "Guarda una nota con un título de 4 letras o más para que la " +
                        "comparación tenga un texto que buscar."
                )
            }

            // ---------- Medio 1: interno privado y cifrado ----------
            Spacer(Modifier.height(14.dp))
            TarjetaPixel(color = VerdeTarjeta, borde = MaterialTheme.colorScheme.outline) {
                Cabecera(
                    icono = R.drawable.ic_candado,
                    titulo = "1. Interno privado",
                    subtitulo = "EncryptedSharedPreferences"
                )
                Spacer(Modifier.height(10.dp))
                Dato("Ruta", interna?.ruta)
                Dato("Tamaño", interna?.let { "${it.bytes} bytes" })

                Spacer(Modifier.height(10.dp))
                BloqueCodigo(
                    etiqueta = "Lo que hay en el archivo (sin descifrar)",
                    texto = interna?.contenido,
                    vacio = "Aún no existe. Guarda una nota para crearlo."
                )

                Spacer(Modifier.height(10.dp))
                BloqueCodigo(
                    etiqueta = "Lo que devuelve EncryptedSharedPreferences",
                    texto = interna?.descifrado,
                    vacio = "Sin valor guardado todavía."
                )

                if (termino != null && interna?.existe == true) {
                    Spacer(Modifier.height(10.dp))
                    Veredicto(
                        ok = !interna.textoVisible,
                        texto = if (interna.textoVisible) {
                            "«$termino» aparece legible en el archivo."
                        } else {
                            "«$termino» NO aparece en el archivo. Solo la app, " +
                                "con la llave del Keystore, puede recuperarlo."
                        }
                    )
                }
            }

            // ---------- Medio 2: externo sin cifrar ----------
            Spacer(Modifier.height(14.dp))
            TarjetaPixel(color = RosaFondo, borde = RosaBorde) {
                Cabecera(
                    icono = R.drawable.ic_nota_rosa,
                    titulo = "2. Externo",
                    subtitulo = "Texto plano, sin cifrado"
                )
                Spacer(Modifier.height(10.dp))
                Dato("Ruta", externa?.ruta)
                Dato("Tamaño", externa?.let { "${it.bytes} bytes" })

                Spacer(Modifier.height(10.dp))
                BloqueCodigo(
                    etiqueta = "Lo que hay en el archivo",
                    texto = externa?.contenido,
                    vacio = "Aún no existe. Guarda una nota para crearlo."
                )

                if (termino != null && externa?.existe == true) {
                    Spacer(Modifier.height(10.dp))
                    Veredicto(
                        ok = !externa.textoVisible,
                        texto = if (externa.textoVisible) {
                            "«$termino» se lee tal cual. Cualquiera que copie el " +
                                "archivo ve la nota completa."
                        } else {
                            "«$termino» no se encontró en el archivo."
                        }
                    )
                }
            }

            // ---------- Conclusion ----------
            Spacer(Modifier.height(14.dp))
            TarjetaPixel(
                color = MaterialTheme.colorScheme.surface,
                borde = MaterialTheme.colorScheme.outline
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icono(R.drawable.ic_campana, 22.dp)
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = "Los dos archivos guardan la misma nota. El interno es " +
                            "ilegible porque EncryptedSharedPreferences cifra la clave con " +
                            "AES256-SIV y el valor con AES256-GCM; la llave maestra se queda " +
                            "en el Android Keystore y nunca se escribe en el archivo.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            BotonPixel(
                texto = "Volver a leer los archivos",
                onClick = onCargar,
                colorFondo = VerdePrimario,
                colorTexto = BlancoPixel,
                icono = R.drawable.ic_regadera
            )
            Spacer(Modifier.height(10.dp))
            BotonPixel(
                texto = "Entendido",
                onClick = onAtras,
                colorFondo = NegroPixel,
                colorTexto = BlancoPixel,
                icono = R.drawable.ic_estrella
            )
            Spacer(Modifier.height(22.dp))
        }
    }
}

@Composable
private fun Cabecera(@DrawableRes icono: Int, titulo: String, subtitulo: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icono(icono, 26.dp)
        Spacer(Modifier.width(10.dp))
        Column {
            Text(titulo, style = MaterialTheme.typography.titleLarge, color = VerdeOscuro)
            Text(subtitulo, style = MaterialTheme.typography.labelSmall, color = VerdeOscuro)
        }
    }
}

/** Etiqueta corta mas valor seleccionable, para poder copiar la ruta. */
@Composable
private fun Dato(etiqueta: String, valor: String?) {
    if (valor == null) return
    Column(Modifier.padding(bottom = 6.dp)) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.titleMedium,
            color = VerdeOscuro
        )
        SelectionContainer {
            Text(
                text = valor,
                style = MaterialTheme.typography.labelSmall,
                color = VerdeOscuro
            )
        }
    }
}

/**
 * Los bytes del archivo sobre fondo oscuro: se lee como una terminal y deja
 * claro que es contenido crudo y no texto de la interfaz.
 */
@Composable
private fun BloqueCodigo(etiqueta: String, texto: String?, vacio: String) {
    val recortado = texto?.take(MAXIMO_VISIBLE)
    val hayMas = texto != null && texto.length > MAXIMO_VISIBLE

    Column {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.titleMedium,
            color = VerdeOscuro,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        SelectionContainer {
            Text(
                text = recortado ?: vacio,
                style = MaterialTheme.typography.labelSmall,
                color = if (recortado != null) BlancoPixel else BlancoPixel.copy(alpha = 0.6f),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NegroPixel, RoundedCornerShape(8.dp))
                    .padding(10.dp)
            )
        }
        if (hayMas) {
            Text(
                text = "... recortado. El archivo completo tiene ${texto!!.length} caracteres.",
                style = MaterialTheme.typography.labelSmall,
                color = VerdeOscuro,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

/** Resultado de buscar el texto de la nota dentro del archivo. */
@Composable
private fun Veredicto(ok: Boolean, texto: String) {
    val color: Color = if (ok) VerdePrimario else RosaFuerte
    val forma = RoundedCornerShape(8.dp)
    Row(
        Modifier
            .fillMaxWidth()
            .background(color, forma)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (ok) "OK" else "!",
            style = MaterialTheme.typography.labelLarge,
            color = BlancoPixel
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = texto,
            style = MaterialTheme.typography.bodyMedium,
            color = BlancoPixel
        )
    }
}

@Composable
private fun Aviso(texto: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(RosaFondo, RoundedCornerShape(8.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icono(R.drawable.ic_campana, 20.dp)
        Spacer(Modifier.width(10.dp))
        Text(
            text = texto,
            style = MaterialTheme.typography.bodyMedium,
            color = VerdeOscuro
        )
    }
}
