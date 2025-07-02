package themouselabo.appviajes_proyectofinaldam.ui.components.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.launch
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.data.Note
import themouselabo.appviajes_proyectofinaldam.ui.components.cards.NoteCard
import themouselabo.appviajes_proyectofinaldam.ui.components.cards.NoteSimpleCard
import themouselabo.appviajes_proyectofinaldam.ui.components.dialogs.EditNoteDialog
import themouselabo.appviajes_proyectofinaldam.ui.components.dialogs.YesNoDialog
import themouselabo.appviajes_proyectofinaldam.ui.components.viewModel.DataViewModel

@Composable
fun NotesItem(note: Note, onTripTabsClick: (String) -> Unit, dataViewModel: DataViewModel) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    if (showEditDialog) {
        EditNoteDialog(
            note = note,
            onDismiss = { showEditDialog = false },
            onSave = { updatedNote ->
                scope.launch { dataViewModel.editNote(updatedNote) }
                showEditDialog = false
            }
        )
    }

    if (showDeleteDialog) {
        YesNoDialog(
            title = stringResource(id = R.string.confirm_delete_note),
            text = stringResource(id = R.string.are_you_sure_note),
            onConfirm = {
                scope.launch { dataViewModel.deleteNote(note) }
                showDeleteDialog = false
            },
            onDismiss = { showDeleteDialog = false })
    }

    Column(
        modifier = Modifier.fillMaxWidth(0.9f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NoteCard(
            note = note,
            onEdit = { showEditDialog = true },
            onDelete = { showDeleteDialog = true },
            onTripTabsClick = { onTripTabsClick(note.tripId) }
        )
    }
}


@Composable
fun NotesSimpleItem(note: Note, dataViewModel: DataViewModel) {
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    if (showEditDialog) {
        EditNoteDialog(
            note = note,
            onDismiss = { showEditDialog = false },
            onSave = { updatedNote ->
                scope.launch { dataViewModel.editNote(updatedNote) }
                showEditDialog = false
            }
        )
    }

    if (showDeleteDialog) {
        YesNoDialog(
            title = stringResource(id = R.string.confirm_delete_note),
            text = stringResource(id = R.string.are_you_sure_note),
            onConfirm = {
                scope.launch { dataViewModel.deleteNote(note) }
                showDeleteDialog = false
            },
            onDismiss = { showDeleteDialog = false })
    }

    Column(
        modifier = Modifier.fillMaxWidth(0.9f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NoteSimpleCard(
            note = note,
            onEdit = { showEditDialog = true },
            onDelete = { showDeleteDialog = true }
        )
    }
}