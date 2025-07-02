package themouselabo.appviajes_proyectofinaldam.ui.components.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.data.Note
import themouselabo.appviajes_proyectofinaldam.theme.DialogButtonSize
import themouselabo.appviajes_proyectofinaldam.theme.DialogButtonTextStyle
import themouselabo.appviajes_proyectofinaldam.theme.TittleTextStyle
import themouselabo.appviajes_proyectofinaldam.ui.components.buttons.CustomTextIconButton
import java.util.UUID

@Composable
fun AddNoteDialog(
    userId: String, tripId: String,
    onDismiss: () -> Unit,
    onSave: (Note) -> Unit
) {
    val noteId = UUID.randomUUID().toString()
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    CustomAlertDialog(
        onDismiss = onDismiss,
        title = { Text(text = stringResource(id = R.string.dialog_note), style = TittleTextStyle) },
        content = {
            Column(modifier = Modifier.padding(5.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(text = stringResource(id = R.string.title)) })
                Spacer(modifier = Modifier.height(5.dp))
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text(text = stringResource(id = R.string.note_content)) })
            }
        },
        confirmButton = {
            CustomTextIconButton(
                onClick = {
                    val note = Note(noteId, title, content, tripId, userId)
                    onSave(note)
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

@Composable
fun EditNoteDialog(
    note: Note,
    onDismiss: () -> Unit,
    onSave: (Note) -> Unit
) {
    var title by remember { mutableStateOf(note.title) }
    var content by remember { mutableStateOf(note.note) }

    CustomAlertDialog(
        onDismiss = onDismiss,
        title = { Text(text = stringResource(id = R.string.edit_note)) },
        content = {
            Column(modifier = Modifier.padding(5.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(text = stringResource(id = R.string.title)) }
                )
                Spacer(modifier = Modifier.height(5.dp))
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text(text = stringResource(id = R.string.note_content)) }
                )
            }
        },
        confirmButton = {
            CustomTextIconButton(
                onClick = {
                    val editedNote = note.copy(title = title, note = content)
                    onSave(editedNote)
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
