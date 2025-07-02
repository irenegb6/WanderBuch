package themouselabo.appviajes_proyectofinaldam.ui.components.dialogs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.theme.DialogButtonSize
import themouselabo.appviajes_proyectofinaldam.theme.DialogButtonTextStyle
import themouselabo.appviajes_proyectofinaldam.theme.DialogTextStyle
import themouselabo.appviajes_proyectofinaldam.theme.DialogTittleTextStyle
import themouselabo.appviajes_proyectofinaldam.ui.components.buttons.CustomTextIconButton

@Composable
fun YesNoDialog(title: String, text: String, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    CustomAlertDialog(
        onDismiss = { onDismiss() },
        title = { Text(text = title, style = DialogTittleTextStyle) },
        content = { Text(text = text, style = DialogTextStyle) },
        confirmButton = {
            CustomTextIconButton(
                onClick = {
                    onConfirm()
                    onDismiss()
                },
                icon = Icons.Default.Check,
                text = stringResource(id = R.string.yes),
                styleText = DialogButtonTextStyle,
                iconSize = DialogButtonSize
            )
        },
        dismissButton = {
            CustomTextIconButton(
                onClick = {
                    onDismiss()
                },
                icon = Icons.Default.Close,
                text = stringResource(id = R.string.no),
                styleText = DialogButtonTextStyle,
                iconSize = DialogButtonSize
            )
        }
    )
}