package themouselabo.appviajes_proyectofinaldam.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun ProyectoFinalDAMTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

private val LightColorScheme = lightColorScheme(
    primary = BaseColor1,
    onPrimary = BaseColor6,
    secondary = BaseColor2,
    onSecondary = BaseColor6,
    background = BaseColor8,
    onBackground = BaseColor7,
    surface = BaseColor6,
    onSurface = BaseColor5
)

private val DarkColorScheme = darkColorScheme(
    primary = BaseColor1.copy(alpha = 0.8f),
    onPrimary = BaseColor6,
    secondary = BaseColor2.copy(alpha = 0.8f),
    onSecondary = BaseColor6,
    background = BaseColor2,
    onBackground = BaseColor6,
    surface = BaseColor4,
    onSurface = BaseColor6
)
