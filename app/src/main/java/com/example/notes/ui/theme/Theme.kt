package com.example.notes.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val EsquemaClaro = lightColorScheme(
    primary = VerdePrimario,
    onPrimary = BlancoPixel,
    primaryContainer = VerdeTarjeta,
    onPrimaryContainer = VerdeOscuro,
    secondary = RosaFuerte,
    onSecondary = BlancoPixel,
    secondaryContainer = RosaFondo,
    onSecondaryContainer = VerdeOscuro,
    tertiary = NegroPixel,
    onTertiary = BlancoPixel,
    background = VerdeFondo,
    onBackground = VerdeOscuro,
    surface = BlancoPixel,
    onSurface = VerdeOscuro,
    surfaceVariant = VerdeTarjeta,
    onSurfaceVariant = VerdeTexto,
    outline = VerdeBorde,
    outlineVariant = RosaBorde,
    error = RosaFuerte,
    onError = BlancoPixel
)

/** Esquinas generosas, como las tarjetas del mockup. */
private val FormasPixel = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(18.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

/**
 * La app se fija en modo claro: el mockup es claro y el tema oscuro cambiaria
 * los verdes y rosas del arte. Tampoco se usa dynamicColor, porque Material You
 * reemplazaria la paleta por la del fondo de pantalla del dispositivo.
 */
@Composable
fun NotesTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = EsquemaClaro,
        typography = Typography,
        shapes = FormasPixel,
        content = content
    )
}
