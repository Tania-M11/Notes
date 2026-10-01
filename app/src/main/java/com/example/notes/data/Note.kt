package com.example.notes.data

import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

/**
 * Una nota confidencial. La misma informacion viaja a los dos medios de
 * almacenamiento que compara la actividad: cifrada en el interno privado y
 * en texto plano en el externo.
 */
data class Note(
    val id: String = UUID.randomUUID().toString(),
    val titulo: String,
    val contenido: String,
    val actualizada: Long = System.currentTimeMillis()
) {
    fun fechaLegible(): String = formatoFecha.format(Date(actualizada))

    companion object {
        private val formatoFecha = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.forLanguageTag("es"))
    }
}

/** Serializa la lista completa al JSON que se guarda como unico valor cifrado. */
fun List<Note>.toJsonString(): String {
    val array = JSONArray()
    forEach { nota ->
        array.put(
            JSONObject()
                .put("id", nota.id)
                .put("titulo", nota.titulo)
                .put("contenido", nota.contenido)
                .put("actualizada", nota.actualizada)
        )
    }
    return array.toString()
}

/** Reconstruye la lista desde el JSON descifrado. Un valor corrupto no debe tumbar la app. */
fun String?.toNotes(): List<Note> {
    if (this.isNullOrBlank()) return emptyList()
    return try {
        val array = JSONArray(this)
        (0 until array.length()).map { i ->
            val objeto = array.getJSONObject(i)
            Note(
                id = objeto.optString("id", UUID.randomUUID().toString()),
                titulo = objeto.optString("titulo"),
                contenido = objeto.optString("contenido"),
                actualizada = objeto.optLong("actualizada", System.currentTimeMillis())
            )
        }
    } catch (e: Exception) {
        emptyList()
    }
}
