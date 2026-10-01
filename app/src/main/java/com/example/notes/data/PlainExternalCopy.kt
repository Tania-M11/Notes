package com.example.notes.data

import android.content.Context
import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Almacenamiento EXTERNO SIN CIFRAR.
 *
 * Escribe la misma informacion en texto plano dentro de getExternalFilesDir(),
 * es decir /sdcard/Android/data/com.example.notes/files/nota_externa.txt.
 * No requiere permisos y sale con un adb pull directo: ese es justamente el
 * punto de la comparacion.
 *
 * Esta copia NO la usa la app para funcionar. Existe solo para la demostracion.
 */
class PlainExternalCopy(private val context: Context) {

    fun disponible(): Boolean =
        Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED &&
            context.getExternalFilesDir(null) != null

    fun ruta(): String = archivo()?.absolutePath ?: "almacenamiento externo no disponible"

    /** Tamano en disco. Cero mientras no se haya guardado ninguna nota. */
    fun tamanoBytes(): Long = archivo()?.takeIf { it.exists() }?.length() ?: 0L

    fun escribir(notas: List<Note>): Boolean {
        val destino = archivo() ?: return false
        return runCatching {
            destino.parentFile?.mkdirs()
            destino.writeText(render(notas))
            true
        }.getOrDefault(false)
    }

    /** Lee la copia tal cual, que es como la veria cualquiera: legible. */
    fun contenidoCrudo(): String? = runCatching {
        archivo()?.takeIf { it.exists() }?.readText()
    }.getOrNull()

    private fun archivo(): File? =
        context.getExternalFilesDir(null)?.let { File(it, NOMBRE) }

    private fun render(notas: List<Note>): String = buildString {
        appendLine("=== NOTAS CONFIDENCIALES - COPIA SIN CIFRAR ===")
        appendLine("Generado: ${formatoFecha.format(Date())}")
        appendLine("Archivo de demostracion. La aplicacion no lee este archivo.")
        appendLine()
        if (notas.isEmpty()) {
            appendLine("(sin notas)")
        } else {
            notas.forEach { nota ->
                appendLine("--- ${nota.titulo} ---")
                appendLine("Actualizada: ${nota.fechaLegible()}")
                appendLine(nota.contenido)
                appendLine()
            }
        }
    }

    companion object {
        const val NOMBRE = "nota_externa.txt"
        private val formatoFecha = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.forLanguageTag("es"))
    }
}
