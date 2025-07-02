package themouselabo.appviajes_proyectofinaldam.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.data.getTripColor
import themouselabo.appviajes_proyectofinaldam.theme.DarkCardColor1
import themouselabo.appviajes_proyectofinaldam.theme.DarkCardColor2
import themouselabo.appviajes_proyectofinaldam.theme.DarkCardColor3
import themouselabo.appviajes_proyectofinaldam.theme.DarkCardColor4
import themouselabo.appviajes_proyectofinaldam.theme.LightCardColor1
import themouselabo.appviajes_proyectofinaldam.theme.LightCardColor2
import themouselabo.appviajes_proyectofinaldam.theme.LightCardColor3
import themouselabo.appviajes_proyectofinaldam.theme.LightCardColor4

@Composable
fun getThemeColorCardByName(baseColorName: String): Color {
    return when (baseColorName) {
        "CardColor1" -> if (isSystemInDarkTheme()) DarkCardColor1 else LightCardColor1
        "CardColor2" -> if (isSystemInDarkTheme()) DarkCardColor2 else LightCardColor2
        "CardColor3" -> if (isSystemInDarkTheme()) DarkCardColor3 else LightCardColor3
        "CardColor4" -> if (isSystemInDarkTheme()) DarkCardColor4 else LightCardColor4
        else -> LightCardColor1
    }
}

@Composable
fun getNameByThemeColorCard(color: Color): String {
    return when (color) {
        DarkCardColor1, LightCardColor1 -> "CardColor1"
        DarkCardColor2, LightCardColor2 -> "CardColor2"
        DarkCardColor3, LightCardColor3 -> "CardColor3"
        DarkCardColor4, LightCardColor4 -> "CardColor4"
        else -> "CardColor1"
    }
}

@Composable
fun provideCardColors(): List<Color> {
    return listOf(
        getThemeColorCardByName("CardColor1"),
        getThemeColorCardByName("CardColor2"),
        getThemeColorCardByName("CardColor3"),
        getThemeColorCardByName("CardColor4"),
    )
}

@Composable
fun getColorCardFromTrip(tripId: String?): Color {
    val loadingText = stringResource(R.string.loading)
    var tripColor by remember { mutableStateOf(loadingText) }
    var color by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(tripId) {
        tripId?.let {
            color = getTripColor(it)
            tripColor = color ?: loadingText
        }
    }
    return when (color) {
        "CardColor1" -> if (isSystemInDarkTheme()) DarkCardColor1 else LightCardColor1
        "CardColor2" -> if (isSystemInDarkTheme()) DarkCardColor2 else LightCardColor2
        "CardColor3" -> if (isSystemInDarkTheme()) DarkCardColor3 else LightCardColor3
        "CardColor4" -> if (isSystemInDarkTheme()) DarkCardColor4 else LightCardColor4
        else -> LightCardColor1
    }
}
