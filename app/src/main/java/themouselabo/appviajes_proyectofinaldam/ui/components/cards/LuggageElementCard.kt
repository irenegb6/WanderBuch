package themouselabo.appviajes_proyectofinaldam.ui.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import themouselabo.appviajes_proyectofinaldam.theme.CardButtonSize
import themouselabo.appviajes_proyectofinaldam.theme.CategoryElementTextStyle
import themouselabo.appviajes_proyectofinaldam.ui.components.buttons.CustomIconButton

@Composable
fun LuggageElementCard(
    element: String,
    checked: Boolean,
    onCheckElement: () -> Unit,
    onDelete: () -> Unit,
    content: @Composable (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CustomIconButton(
                onClick = onCheckElement,
                icon = if (checked) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                iconSize = CardButtonSize
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = element, style = CategoryElementTextStyle)
            Spacer(modifier = Modifier.weight(1f))
            CustomIconButton(
                onClick = onDelete,
                icon = Icons.Default.Delete,
                iconSize = CardButtonSize
            )
        }
        content?.invoke()
    }
}