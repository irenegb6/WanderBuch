package themouselabo.appviajes_proyectofinaldam.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import themouselabo.appviajes_proyectofinaldam.theme.TittleTextStyle
import themouselabo.appviajes_proyectofinaldam.ui.components.cards.ColorCard
import themouselabo.appviajes_proyectofinaldam.utils.provideCardColors

@Composable
fun ColorsDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onSelectColor: (Color) -> Unit,
    title: String,
) {
    val cardColors = provideCardColors()
    CustomAlertDialog(
        modifier = modifier.fillMaxWidth(),
        onDismiss = onDismiss,
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                style = TittleTextStyle
            )
        },
        content = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(cardColors.size) { index ->
                    val color = cardColors[index]
                    ColorCard(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(48.dp),
                        color = color,
                        onClick = { onSelectColor(color) },
                    )
                }
            }
        },
        dismissButton = null,
        confirmButton = null,
    )
}