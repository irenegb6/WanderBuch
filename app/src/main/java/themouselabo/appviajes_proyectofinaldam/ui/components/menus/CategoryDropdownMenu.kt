package themouselabo.appviajes_proyectofinaldam.ui.components.menus

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.theme.DialogTextStyle

@Composable
fun <T> CategoryDropdownMenu(
    expandedMenu: Boolean,
    selectedOption: T?,
    options: List<T>,
    onOptionSelected: (T) -> Unit,
    onDismissRequest: () -> Unit,
    displayText: @Composable (T) -> Unit,
    modifier: Modifier = Modifier,
    onIconClick: () -> Unit
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable(onClick = onIconClick)
                .fillMaxWidth()
        ) {
            Text(
                text = selectedOption?.toString() ?: stringResource(id = R.string.select_category),
                modifier = Modifier.weight(1f),
                style = DialogTextStyle
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier.clickable(onClick = onIconClick)
            )
        }
        DropdownMenu(
            expanded = expandedMenu,
            onDismissRequest = onDismissRequest,
            modifier = Modifier.width(IntrinsicSize.Max)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { displayText(option) },
                    onClick = {
                        onOptionSelected(option)
                        onDismissRequest()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}


