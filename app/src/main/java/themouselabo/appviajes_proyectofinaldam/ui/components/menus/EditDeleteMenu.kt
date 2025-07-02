package themouselabo.appviajes_proyectofinaldam.ui.components.menus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.theme.CardButtonSize
import themouselabo.appviajes_proyectofinaldam.theme.DialogTextStyle

@Composable
fun CustomEditDeleteMenu(
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        IconButton(onClick = {
            expanded = true
        }) {
            Icon(
                Icons.Default.Menu,
                contentDescription = null,
                modifier = Modifier
                    .size(CardButtonSize)
                    .background(Color.Transparent)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(id = R.string.edit), style = DialogTextStyle) },
                onClick = {
                    expanded = false
                    onEdit()
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(id = R.string.delete), style = DialogTextStyle) },
                onClick = {
                    expanded = false
                    onDelete()
                }
            )
        }
    }
}