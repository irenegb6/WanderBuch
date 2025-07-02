package themouselabo.appviajes_proyectofinaldam.ui.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.data.Event
import themouselabo.appviajes_proyectofinaldam.data.getTripDestination
import themouselabo.appviajes_proyectofinaldam.theme.CardButtonSize
import themouselabo.appviajes_proyectofinaldam.theme.CardButtonTextStyle
import themouselabo.appviajes_proyectofinaldam.theme.CardTextStyle
import themouselabo.appviajes_proyectofinaldam.theme.CardTittleTextStyle
import themouselabo.appviajes_proyectofinaldam.theme.DateTextStyle
import themouselabo.appviajes_proyectofinaldam.ui.components.buttons.CustomHashtagTextButton
import themouselabo.appviajes_proyectofinaldam.ui.components.buttons.CustomTextIconButton
import themouselabo.appviajes_proyectofinaldam.ui.components.buttons.LocationMapButton
import themouselabo.appviajes_proyectofinaldam.ui.components.dialogs.LocationMapDialog
import themouselabo.appviajes_proyectofinaldam.ui.components.menus.CustomEditDeleteMenu
import themouselabo.appviajes_proyectofinaldam.ui.components.viewModel.SettingsViewModel
import themouselabo.appviajes_proyectofinaldam.utils.getColorCardFromTrip

@Composable
fun EventCard(
    event: Event,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val settingsViewModel: SettingsViewModel = viewModel()
    var showMapDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(0.9f)
    ) {
        Card(
            modifier = Modifier.padding(8.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = getColorCardFromTrip(event.tripId).copy(
                    alpha = 0.2f
                )
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${event.startTime?.let { settingsViewModel.formatTime(it) }}",
                        style = DateTextStyle
                    )
                    Text(
                        text = " - ",
                        style = DateTextStyle
                    )
                    Text(
                        text = "${event.endTime?.let { settingsViewModel.formatTime(it) }}",
                        style = DateTextStyle
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    CustomEditDeleteMenu(onEdit = { onEdit() }, onDelete = { onDelete() })
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 2.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = event.title,
                            style = CardTittleTextStyle,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                        Text(
                            text = event.description,
                            style = CardTextStyle,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 15.dp)
        ) {
            if (event.isMap) {
                event.place?.let {
                    CustomTextIconButton(
                        onClick = { showMapDialog = true },
                        icon = Icons.Default.LocationOn,
                        text = stringResource(id = R.string.title_map),
                        styleText = CardButtonTextStyle,
                        iconSize = CardButtonSize,
                        buttonColor = getColorCardFromTrip(event.tripId)
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(0.dp))
            }
        }
    }
    if (showMapDialog) {
        event.place?.let { LocationMapDialog(geoPoint = it, onDismiss = { showMapDialog = false }) }
    }
}

@Composable
fun EventSimpleCard(
    event: Event,
    onTripTabsClick: (String) -> Unit,
) {
    val settingsViewModel: SettingsViewModel = viewModel()
    val loadingText = stringResource(R.string.loading)
    var tripDestination by remember { mutableStateOf(loadingText) }
    val tripId = event.tripId

    LaunchedEffect(tripId) {
        tripId.let {
            val destination = getTripDestination(it)
            tripDestination = destination ?: loadingText
        }
    }

    Card(
        modifier = Modifier.padding(8.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = getColorCardFromTrip(event.tripId).copy(
                alpha = 0.8f
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${event.startTime?.let { settingsViewModel.formatTime(it) }}",
                    style = DateTextStyle
                )
                Text(
                    text = " - ",
                    style = DateTextStyle
                )
                Text(
                    text = "${event.endTime?.let { settingsViewModel.formatTime(it) }}",
                    style = DateTextStyle
                )
                Spacer(modifier = Modifier.weight(1f))
                CustomHashtagTextButton(
                    onClick = { onTripTabsClick(tripId) },
                    text = tripDestination,
                    color = getColorCardFromTrip(event.tripId).copy(alpha = 0.7f)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = event.title,
                        style = CardTittleTextStyle,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    Text(
                        text = event.description,
                        style = CardTextStyle,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                if (event.isMap) {
                    event.place?.let { LocationMapButton(geoPoint = it) }
                }
                Spacer(modifier = Modifier.width(25.dp))
            }
        }
    }
}