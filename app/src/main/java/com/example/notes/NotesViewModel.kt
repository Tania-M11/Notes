package com.example.notes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.notes.data.Inspeccion
import com.example.notes.data.Note
import com.example.notes.data.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Destinos de la app. La navegacion se resuelve con una pila propia. */
sealed interface Pantalla {
    data object Inicio : Pantalla
    data object Lista : Pantalla
    data class Editor(val notaId: String?) : Pantalla
    data class Detalle(val notaId: String) : Pantalla
    data object Guardada : Pantalla
    data object Ajustes : Pantalla
    data object ComoSeAlmacena : Pantalla
    data object Demostracion : Pantalla
}

data class NotesUiState(
    val pantalla: Pantalla = Pantalla.Inicio,
    val notas: List<Note> = emptyList(),
    val cargando: Boolean = true,
    val interna: Inspeccion? = null,
    val externa: Inspeccion? = null,
    val externoDisponible: Boolean = true
) {
    fun nota(id: String?): Note? = id?.let { buscado -> notas.firstOrNull { it.id == buscado } }
}

class NotesViewModel(aplicacion: Application) : AndroidViewModel(aplicacion) {

    private val repositorio = NotesRepository(aplicacion)
    private val pila = ArrayDeque<Pantalla>()

    private val _estado = MutableStateFlow(NotesUiState())
    val estado: StateFlow<NotesUiState> = _estado.asStateFlow()

    init {
        _estado.update { it.copy(externoDisponible = repositorio.externoDisponible()) }
        refrescarNotas()
    }

    fun irA(pantalla: Pantalla) {
        pila.addLast(_estado.value.pantalla)
        _estado.update { it.copy(pantalla = pantalla) }
    }

    /** Reemplaza el destino actual sin crecer la pila: para los flujos de guardado. */
    fun reemplazarCon(pantalla: Pantalla) {
        _estado.update { it.copy(pantalla = pantalla) }
    }

    /** Vuelve al destino anterior. Devuelve false cuando ya no hay a donde volver. */
    fun volver(): Boolean {
        val anterior = pila.removeLastOrNull() ?: return false
        _estado.update { it.copy(pantalla = anterior) }
        return true
    }

    /** Vuelve a la lista limpiando la pila: el "Ver mis notas" del mockup. */
    fun volverALista() {
        pila.clear()
        pila.addLast(Pantalla.Inicio)
        _estado.update { it.copy(pantalla = Pantalla.Lista) }
    }

    fun guardarNota(id: String?, titulo: String, contenido: String) {
        val limpio = titulo.trim().ifBlank { "Nota sin titulo" }
        viewModelScope.launch {
            val existente = _estado.value.nota(id)
            val nota = existente?.copy(
                titulo = limpio,
                contenido = contenido,
                actualizada = System.currentTimeMillis()
            ) ?: Note(titulo = limpio, contenido = contenido)

            val notas = repositorio.guardar(nota)
            _estado.update { it.copy(notas = notas, cargando = false) }
            reemplazarCon(Pantalla.Guardada)
        }
    }

    fun eliminarNota(id: String) {
        viewModelScope.launch {
            val notas = repositorio.eliminar(id)
            _estado.update { it.copy(notas = notas) }
            volverALista()
        }
    }

    /** Lee de disco los dos archivos tal cual estan, para la pantalla de demostracion. */
    fun cargarInspeccion() {
        viewModelScope.launch {
            val (interna, externa) = repositorio.inspeccionar()
            _estado.update { it.copy(interna = interna, externa = externa) }
        }
    }

    private fun refrescarNotas() {
        viewModelScope.launch {
            val notas = repositorio.listar()
            _estado.update { it.copy(notas = notas, cargando = false) }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: androidx.lifecycle.viewmodel.CreationExtras
            ): T {
                val aplicacion = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                    ?: error("Falta la Application en las CreationExtras")
                return NotesViewModel(aplicacion) as T
            }
        }
    }
}
