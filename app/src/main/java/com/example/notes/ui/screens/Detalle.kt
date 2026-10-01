package com.example.notes.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.notes.R
import com.example.notes.data.Note
import com.example.notes.ui.components.BotonPixel
import com.example.notes.ui.components.EncabezadoPixel
import com.example.notes.ui.components.Icono
import com.example.notes.ui.components.PantallaPixel
import com.example.notes.ui.components.TarjetaPixel
import com.example.notes.ui.theme.RosaFuerte
import com.example.notes.ui.theme.VerdeOscuro
import com.example.notes.ui.theme.VerdeTarjeta

/** Pantalla 4: una nota abierta, con editar y eliminar. */
@Composable
fun PantallaDetalle(
    nota: Note,
    onAtras: () -> Unit,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    var menuAbierto by remember { mutableStateOf(false) }
    var confirmarBorrado by remember { mutableStateOf(false) }

    PantallaPixel {
        EncabezadoPixel(
            titulo = "",
            onAtras = onAtras,
            accion = {
                Box {
                    Text(
                        text = "⋮",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .padding(horizontal = 14.dp)
                            .clickable { menuAbierto = true }
                    )
                    DropdownMenu(menuAbierto, onDismissRequest = { menuAbierto = false }) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Eliminar nota",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = RosaFuerte
                                )
                            },
                            onClick = {
                                menuAbierto = false
                                confirmarBorrado = true
                            }
                        )
                    }
                }
            }
        )

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp)
        ) {
            TarjetaPixel(color = MaterialTheme.colorScheme.surfaceVariant) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icono(iconoDeNota(nota), 30.dp)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = nota.titulo,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = nota.fechaLegible(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(Modifier.height(14.dp))
            TarjetaPixel(color = VerdeTarjeta, borde = MaterialTheme.colorScheme.outline) {
                Text(
                    text = nota.contenido.ifBlank { "(sin contenido)" },
                    style = MaterialTheme.typography.bodyLarge,
                    color = VerdeOscuro
                )
            }

            Spacer(Modifier.height(22.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Icono(R.drawable.ic_mascota, 96.dp)
            }
            Spacer(Modifier.height(18.dp))
            BotonPixel(
                texto = "Editar",
                onClick = onEditar,
                colorFondo = VerdeTarjeta,
                colorTexto = VerdeOscuro,
                icono = R.drawable.ic_lapiz
            )
            Spacer(Modifier.height(20.dp))
        }
    }

    if (confirmarBorrado) {
        AlertDialog(
            onDismissRequest = { confirmarBorrado = false },
            title = { Text("¿Eliminar la nota?", style = MaterialTheme.typography.titleLarge) },
            text = {
                Text(
                    "Se borrará del almacenamiento interno cifrado y también de la " +
                        "copia externa sin cifrar.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    confirmarBorrado = false
                    onEliminar()
                }) { Text("Eliminar", color = RosaFuerte) }
            },
            dismissButton = {
                TextButton(onClick = { confirmarBorrado = false }) { Text("Cancelar") }
            }
        )
    }
}

