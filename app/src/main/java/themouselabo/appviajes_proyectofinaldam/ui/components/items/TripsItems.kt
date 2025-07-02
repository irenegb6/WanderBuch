package themouselabo.appviajes_proyectofinaldam.ui.components.items

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.data.Trip
import themouselabo.appviajes_proyectofinaldam.ui.components.cards.TripCard
import themouselabo.appviajes_proyectofinaldam.ui.components.dialogs.EditTripDialog
import themouselabo.appviajes_proyectofinaldam.ui.components.dialogs.YesNoDialog
import themouselabo.appviajes_proyectofinaldam.ui.components.viewModel.DataViewModel

@Composable
fun TripsItem(
    trip: Trip,
    onTripTabsClick: (String) -> Unit,
    onLuggageClick: (String) -> Unit,
    type: String
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    val dataViewModel: DataViewModel = viewModel()

    if (showDeleteDialog) {
        YesNoDialog(
            title = stringResource(id = R.string.confirm_delete_trip),
            text = stringResource(id = R.string.are_you_sure_trip),
            onDismiss = { showDeleteDialog = false },
            onConfirm = {
                dataViewModel.deleteTrip(trip)
                showDeleteDialog = false
            }
        )
    }

    if (showEditDialog) {
        EditTripDialog(
            trip = trip,
            onDismiss = { showEditDialog = false },
            onSave = { updatedTrip ->
                dataViewModel.editTrip(updatedTrip)
                showEditDialog = false
            }
        )
    }

    Column {
        TripCard(
            trip = trip,
            onEdit = { showEditDialog = true },
            onDelete = { showDeleteDialog = true },
            onTripTabsClick = { onTripTabsClick(trip.id) },
            onLuggageClick = { onLuggageClick(trip.id) },
            type = type
        )
    }
}
