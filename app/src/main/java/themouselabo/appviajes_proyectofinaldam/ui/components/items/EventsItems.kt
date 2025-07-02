package themouselabo.appviajes_proyectofinaldam.ui.components.items

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.data.Event
import themouselabo.appviajes_proyectofinaldam.ui.components.cards.EventCard
import themouselabo.appviajes_proyectofinaldam.ui.components.cards.EventSimpleCard
import themouselabo.appviajes_proyectofinaldam.ui.components.dialogs.EditEventDialog
import themouselabo.appviajes_proyectofinaldam.ui.components.dialogs.YesNoDialog
import themouselabo.appviajes_proyectofinaldam.ui.components.viewModel.DataViewModel

@Composable
fun EventsItem(event: Event) {
    val dataViewModel: DataViewModel = viewModel()
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    if (showEditDialog) {
        EditEventDialog(
            event = event,
            onDismiss = { showEditDialog = false },
            onSave = { updatedEvent ->
                scope.launch { dataViewModel.editEvent(updatedEvent) }
                showEditDialog = false
            }
        )
    }

    if (showDeleteDialog) {
        YesNoDialog(
            title = stringResource(id = R.string.confirm_delete_event),
            text = stringResource(id = R.string.are_you_sure_event),
            onConfirm = {
                scope.launch { dataViewModel.deleteEvent(event) }
                showDeleteDialog = false
            },
            onDismiss = { showDeleteDialog = false })
    }

    Column {
        EventCard(
            event = event,
            onEdit = { showEditDialog = true },
            onDelete = { showDeleteDialog = true }
        )
    }
}

@Composable
fun EventsSimpleItem(
    event: Event,
    onTripTabsClick: (String) -> Unit
) {
    Column {
        EventSimpleCard(
            event = event,
            onTripTabsClick = { onTripTabsClick(event.tripId) }
        )
    }
}
