package com.example.notes.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Radiografia de un medio de almacenamiento, tal como la muestra la pantalla
 * de demostracion.
 *
 * - [contenido] son los bytes del archivo leidos directo del disco.
 * - [descifrado] solo lo trae el medio cifrado: es el mismo dato pasado por
 *   EncryptedSharedPreferences, que lo devuelve en claro.
 * - [textoVisible] es el veredicto: si [termino] aparece literalmente dentro
 *   del archivo, la informacion esta expuesta.
 */
data class Inspeccion(
    val ruta: String,
    val contenido: String?,
    val bytes: Long,
    val cifrado: Boolean,
    val termino: String?,
    val textoVisible: Boolean,
    val descifrado: String? = null
) {
    val existe: Boolean get() = contenido != null
}

/**
 * Punto unico de entrada a los datos. Cada escritura toca los DOS medios:
 * el interno cifrado, que es el que la app lee de verdad, y la copia externa
 * en texto plano, que solo existe para la demostracion de seguridad.
 */
class NotesRepository(context: Context) {

    private val appContext = context.applicationContext
    private val interno = SecureNoteStore(appContext)
    private val externo = PlainExternalCopy(appContext)

    suspend fun listar(): List<Note> = withContext(Dispatchers.IO) {
        interno.leerNotas().sortedByDescending { it.actualizada }
    }

    /** Crea o actualiza segun el id, y re-sincroniza la copia externa. */
    suspend fun guardar(nota: Note): List<Note> = withContext(Dispatchers.IO) {
        val actuales = interno.leerNotas().toMutableList()
        val indice = actuales.indexOfFirst { it.id == nota.id }
        if (indice >= 0) actuales[indice] = nota else actuales.add(nota)
        persistir(actuales)
    }

    suspend fun eliminar(id: String): List<Note> = withContext(Dispatchers.IO) {
        persistir(interno.leerNotas().filterNot { it.id == id })
    }

    /** Lee los dos medios de una sola pasada, con el mismo termino de busqueda. */
    suspend fun inspeccionar(): Pair<Inspeccion, Inspeccion> = withContext(Dispatchers.IO) {
        val termino = terminoDePrueba()

        val crudoInterno = interno.contenidoCrudo()
        val interna = Inspeccion(
            ruta = interno.ruta(),
            contenido = crudoInterno,
            bytes = interno.tamanoBytes(),
            cifrado = true,
            termino = termino,
            textoVisible = aparece(termino, crudoInterno),
            descifrado = interno.valorDescifrado()
        )

        val crudoExterno = externo.contenidoCrudo()
        val externa = Inspeccion(
            ruta = externo.ruta(),
            contenido = crudoExterno,
            bytes = externo.tamanoBytes(),
            cifrado = false,
            termino = termino,
            textoVisible = aparece(termino, crudoExterno)
        )

        interna to externa
    }

    fun externoDisponible(): Boolean = externo.disponible()

    private fun persistir(notas: List<Note>): List<Note> {
        interno.guardarNotas(notas)
        externo.escribir(notas)
        return notas.sortedByDescending { it.actualizada }
    }

    /**
     * Texto que se busca dentro de los dos archivos para dar el veredicto: el
     * titulo de la nota mas reciente, o su primera linea con contenido si no
     * tiene titulo. Se exigen 4 caracteres minimo porque una cadena mas corta
     * puede salir por casualidad dentro del base64 y falsear el resultado.
     */
    private fun terminoDePrueba(): String? {
        val nota = interno.leerNotas().maxByOrNull { it.actualizada } ?: return null
        val candidato = nota.titulo.ifBlank {
            nota.contenido.lineSequence().firstOrNull { it.isNotBlank() }.orEmpty()
        }
        return candidato.trim().takeIf { it.length >= MINIMO_TERMINO }
    }

    private fun aparece(termino: String?, contenido: String?): Boolean =
        termino != null && contenido != null && contenido.contains(termino, ignoreCase = true)

    private companion object {
        const val MINIMO_TERMINO = 4
    }
}
