package themouselabo.appviajes_proyectofinaldam.ui.components.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.data.Luggage
import themouselabo.appviajes_proyectofinaldam.data.provideCategoryLuggageDataMap
import themouselabo.appviajes_proyectofinaldam.data.provideLuggageCategory
import themouselabo.appviajes_proyectofinaldam.theme.DialogButtonSize
import themouselabo.appviajes_proyectofinaldam.theme.DialogButtonTextStyle
import themouselabo.appviajes_proyectofinaldam.theme.DialogTextStyle
import themouselabo.appviajes_proyectofinaldam.theme.DialogTittleTextStyle
import themouselabo.appviajes_proyectofinaldam.ui.components.buttons.CustomTextIconButton
import themouselabo.appviajes_proyectofinaldam.ui.components.menus.CategoryDropdownMenu
import java.util.UUID

@Composable
fun AddLuggageDialog(
    userId: String,
    tripId: String,
    onDismiss: () -> Unit,
    onSave: (Luggage) -> Unit
) {
    val elementId = remember { UUID.randomUUID().toString() }
    var element by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    val checked by remember { mutableStateOf(false) }
    var expandedMenu by remember { mutableStateOf(false) }

    val categoryDataMap = provideCategoryLuggageDataMap()

    CustomAlertDialog(
        onDismiss = onDismiss,
        title = {
            Text(
                text = stringResource(id = R.string.dialog_luggage),
                style = DialogTittleTextStyle
            )
        },
        content = {
            Column(modifier = Modifier.padding(10.dp)) {
                OutlinedTextField(
                    value = element,
                    onValueChange = { element = it },
                    label = {
                        Text(
                            text = stringResource(id = R.string.element),
                            style = DialogTextStyle
                        )
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { expandedMenu = true }
                ) {
                    CategoryDropdownMenu(
                        expandedMenu = expandedMenu,
                        selectedOption = category.takeIf { it.isNotEmpty() },
                        options = provideLuggageCategory(),
                        onOptionSelected = { selectedCategory ->
                            category = selectedCategory
                            expandedMenu = false
                        },
                        onDismissRequest = { expandedMenu = false },
                        displayText = { option -> Text(text = option, style = DialogTextStyle) },
                        modifier = Modifier.fillMaxWidth(),
                        onIconClick = { expandedMenu = true }
                    )
                }
            }
        },
        confirmButton = {
            CustomTextIconButton(
                onClick = {
                    val categoryData = categoryDataMap[category] ?: ""
                    val luggage = Luggage(elementId, element, categoryData, checked, tripId, userId)
                    onSave(luggage)
                    onDismiss()
                },
                icon = Icons.Default.Save,
                text = stringResource(id = R.string.save),
                styleText = DialogButtonTextStyle,
                iconSize = DialogButtonSize
            )
        },
        dismissButton = {
            CustomTextIconButton(
                onClick = {
                    onDismiss()
                },
                icon = Icons.Default.Cancel,
                text = stringResource(id = R.string.cancel),
                styleText = DialogButtonTextStyle,
                iconSize = DialogButtonSize
            )
        }
    )
}