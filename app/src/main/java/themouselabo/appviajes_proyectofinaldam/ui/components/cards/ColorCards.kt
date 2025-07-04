package themouselabo.appviajes_proyectofinaldam.ui.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ColorCard(
    modifier: Modifier = Modifier,
    color: Color,
    onClick: (Color) -> Unit
) {
    Box(
        modifier = modifier
            .size(32.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(color)
            .clickable {
                onClick(color)
            },
    )
}