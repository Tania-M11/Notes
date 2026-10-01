package com.example.notes.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.notes.R

/**
 * Armazon comun de las pantallas: fondo de la paleta, la franja de cesped
 * recortada del arte y, si la pantalla lo pide, la barra de navegacion debajo.
 */
@Composable
fun PantallaPixel(
    modifier: Modifier = Modifier,
    conCesped: Boolean = true,
    barraInferior: (@Composable () -> Unit)? = null,
    contenido: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
    ) {
        Column(Modifier.weight(1f).fillMaxWidth()) { contenido() }
        if (conCesped) {
            Image(
                painter = painterResource(R.drawable.borde_cesped),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                contentScale = ContentScale.Crop,
                alignment = Alignment.BottomCenter
            )
        }
        barraInferior?.invoke()
    }
}

@Composable
fun Icono(
    @DrawableRes recurso: Int,
    tam: Dp = 24.dp,
    modifier: Modifier = Modifier,
    espejado: Boolean = false
) {
    Image(
        painter = painterResource(recurso),
        contentDescription = null,
        modifier = modifier
            .size(tam)
            .then(if (espejado) Modifier.graphicsLayer(scaleX = -1f) else Modifier),
        contentScale = ContentScale.Fit
    )
}

@Composable
fun IconoBoton(
    @DrawableRes recurso: Int,
    onClick: () -> Unit,
    tam: Dp = 24.dp,
    espejado: Boolean = false
) {
    Box(
        Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(6.dp)
    ) {
        Icono(recurso, tam, espejado = espejado)
    }
}

/** La flecha del kit, reflejada, hace de chevron de "abrir". */
@Composable
fun ChevronDerecha(tam: Dp = 14.dp, modifier: Modifier = Modifier) {
    Icono(R.drawable.ic_flecha_izq, tam, modifier.alpha(0.4f), espejado = true)
}

@Composable
fun BotonPixel(
    texto: String,
    onClick: () -> Unit,
    colorFondo: Color,
    colorTexto: Color,
    modifier: Modifier = Modifier,
    @DrawableRes icono: Int? = null,
    habilitado: Boolean = true
) {
    val fondo = if (habilitado) colorFondo else colorFondo.copy(alpha = 0.4f)
    Row(
        modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(fondo)
            .clickable(enabled = habilitado, onClick = onClick)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icono != null) {
            Icono(icono, 22.dp)
            Spacer(Modifier.width(10.dp))
        }
        Text(
            text = texto,
            style = MaterialTheme.typography.labelLarge,
            color = colorTexto,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun TarjetaPixel(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surface,
    borde: Color = MaterialTheme.colorScheme.outline,
    onClick: (() -> Unit)? = null,
    contenido: @Composable ColumnScope.() -> Unit
) {
    val forma = RoundedCornerShape(14.dp)
    Column(
        modifier
            .fillMaxWidth()
            .clip(forma)
            .background(color)
            .border(2.dp, borde, forma)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(14.dp),
        content = contenido
    )
}

@Composable
fun EncabezadoPixel(
    titulo: String,
    modifier: Modifier = Modifier,
    @DrawableRes icono: Int? = null,
    onAtras: (() -> Unit)? = null,
    accion: (@Composable () -> Unit)? = null
) {
    Row(
        modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onAtras != null) {
            IconoBoton(R.drawable.ic_flecha_izq, onAtras, 22.dp)
            Spacer(Modifier.width(4.dp))
        }
        if (icono != null) {
            Icono(icono, 32.dp)
            Spacer(Modifier.width(10.dp))
        }
        Text(
            text = titulo,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        accion?.invoke()
    }
}

@Composable
fun BarraNavegacion(
    destinoActual: String,
    onNotas: () -> Unit,
    onNueva: () -> Unit,
    onAjustes: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DestinoNav(R.drawable.ic_nav_casa, "Notas", destinoActual == "notas", onNotas)
        DestinoNav(R.drawable.ic_nav_mas, "Nueva", destinoActual == "nueva", onNueva)
        DestinoNav(
            R.drawable.ic_nav_ajustes,
            "Ajustes",
            destinoActual == "ajustes",
            onAjustes
        )
    }
}

@Composable
private fun DestinoNav(
    @DrawableRes recurso: Int,
    etiqueta: String,
    activo: Boolean,
    onClick: () -> Unit
) {
    Column(
        Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 4.dp)
            .alpha(if (activo) 1f else 0.45f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icono(recurso, 26.dp)
        Spacer(Modifier.height(4.dp))
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.labelSmall,
            color = if (activo) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
