package com.example.notes

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notes.ui.screens.PantallaAjustes
import com.example.notes.ui.screens.PantallaComoSeAlmacena
import com.example.notes.ui.screens.PantallaDemostracion
import com.example.notes.ui.screens.PantallaDetalle
import com.example.notes.ui.screens.PantallaEditor
import com.example.notes.ui.screens.PantallaGuardada
import com.example.notes.ui.screens.PantallaInicio
import com.example.notes.ui.screens.PantallaLista
import com.example.notes.ui.theme.NotesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotesTheme {
                AppNotas()
            }
        }
    }
}

@Composable
private fun AppNotas(modeloVista: NotesViewModel = viewModel(factory = NotesViewModel.Factory)) {
    val estado by modeloVista.estado.collectAsState()
    val actividad = LocalContext.current as? Activity

    // El boton atras del sistema recorre la pila propia; si ya no hay, cierra la app.
    BackHandler { if (!modeloVista.volver()) actividad?.finish() }

    when (val pantalla = estado.pantalla) {
        Pantalla.Inicio -> PantallaInicio(
            onComenzar = { modeloVista.irA(Pantalla.Lista) }
        )

        Pantalla.Lista -> PantallaLista(
            notas = estado.notas,
            cargando = estado.cargando,
            onNuevaNota = { modeloVista.irA(Pantalla.Editor(null)) },
            onAbrirNota = { modeloVista.irA(Pantalla.Detalle(it.id)) },
            onAjustes = { modeloVista.irA(Pantalla.Ajustes) }
        )

        is Pantalla.Editor -> PantallaEditor(
            notaExistente = estado.nota(pantalla.notaId),
            onAtras = { modeloVista.volver() },
            onGuardar = { titulo, contenido ->
                modeloVista.guardarNota(pantalla.notaId, titulo, contenido)
            }
        )

        is Pantalla.Detalle -> {
            val nota = estado.nota(pantalla.notaId)
            if (nota == null) {
                // La nota ya no existe (se elimino): se vuelve al listado.
                LaunchedEffect(pantalla.notaId) { modeloVista.volverALista() }
            } else {
                PantallaDetalle(
                    nota = nota,
                    onAtras = { modeloVista.volver() },
                    onEditar = { modeloVista.irA(Pantalla.Editor(nota.id)) },
                    onEliminar = { modeloVista.eliminarNota(nota.id) }
                )
            }
        }

        Pantalla.Guardada -> PantallaGuardada(
            onVerNotas = { modeloVista.volverALista() },
            onComoSeAlmacena = { modeloVista.irA(Pantalla.ComoSeAlmacena) }
        )

        Pantalla.Ajustes -> PantallaAjustes(
            onAtras = { modeloVista.volver() },
            onSeguridad = { modeloVista.irA(Pantalla.ComoSeAlmacena) },
            onNotas = { modeloVista.volverALista() },
            onNuevaNota = { modeloVista.irA(Pantalla.Editor(null)) }
        )

        Pantalla.ComoSeAlmacena -> PantallaComoSeAlmacena(
            onAtras = { modeloVista.volver() },
            onDemostracion = { modeloVista.irA(Pantalla.Demostracion) }
        )

        Pantalla.Demostracion -> PantallaDemostracion(
            interna = estado.interna,
            externa = estado.externa,
            onCargar = { modeloVista.cargarInspeccion() },
            onAtras = { modeloVista.volver() }
        )
    }
}
