package themouselabo.appviajes_proyectofinaldam.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StickyNote2
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseUser
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.theme.PageSubTittlesTextStyle
import themouselabo.appviajes_proyectofinaldam.theme.PageTittleTextStyle
import themouselabo.appviajes_proyectofinaldam.ui.components.buttons.SpeedDialData
import themouselabo.appviajes_proyectofinaldam.ui.components.buttons.SpeedDialFloatingActionButton
import themouselabo.appviajes_proyectofinaldam.ui.components.dialogs.AddTripDialog
import themouselabo.appviajes_proyectofinaldam.ui.components.items.EventsSimpleItem
import themouselabo.appviajes_proyectofinaldam.ui.components.lists.ColumnEventListByUser
import themouselabo.appviajes_proyectofinaldam.ui.components.lists.RowTripListByUser
import themouselabo.appviajes_proyectofinaldam.ui.components.viewModel.DataViewModel

@Composable
fun HomeScreen(
    userData: FirebaseUser?,
    onSettingsClick: () -> Unit,
    onNotesClick: () -> Unit,
    onTranslationClick: () -> Unit,
    onLuggageClick: (String) -> Unit,
    onTripListClick: () -> Unit,
    onTripTabsClick: (String) -> Unit
) {
    val dataViewModel: DataViewModel = viewModel()

    val showDialog = remember { mutableStateOf(false) }
    fun openTripDialog() {
        showDialog.value = true
    }

    val trips = dataViewModel.tripsState.value
    val events = dataViewModel.eventsState.value

    Scaffold(
        floatingActionButton = {
            SpeedDialFloatingActionButton(
                initialExpanded = false,
                animationDuration = 300,
                animationDelayPerSelection = 100,
                showLabels = true,
                fabBackgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                fabContentColor = MaterialTheme.colorScheme.secondary,
                speedDialBackgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                speedDialContentColor = contentColorFor(MaterialTheme.colorScheme.onSecondaryContainer),
                speedDialData = listOf(
                    SpeedDialData(
                        label = stringResource(R.string.title_settings),
                        painter = rememberVectorPainter(Icons.Default.Settings)
                    ) {
                        onSettingsClick()
                    },
                    SpeedDialData(
                        label = stringResource(R.string.title_trip_list),
                        painter = rememberVectorPainter(Icons.Default.Book)
                    ) {
                        onTripListClick()
                    },
                    SpeedDialData(
                        label = stringResource(R.string.title_notes),
                        painter = rememberVectorPainter(Icons.AutoMirrored.Filled.StickyNote2)
                    ) {
                        onNotesClick()
                    },
                    SpeedDialData(
                        label = stringResource(R.string.title_translation),
                        painter = rememberVectorPainter(Icons.Default.Translate)
                    ) {
                        onTranslationClick()
                    },
                    SpeedDialData(
                        label = stringResource(R.string.add_trip),
                        painter = rememberVectorPainter(Icons.Default.Add)
                    ) {
                        openTripDialog()
                    }
                )
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { Spacer(modifier = Modifier.height(30.dp)) }
            item {
                userData?.displayName?.let { it1 ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(
                            text = stringResource(id = R.string.welcome) + ' ' + it1,
                            style = PageTittleTextStyle
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        AsyncImage(
                            model = userData.photoUrl,
                            contentDescription = stringResource(R.string.profile_picture),
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(40.dp)) }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = stringResource(id = R.string.next_trips),
                        style = PageSubTittlesTextStyle
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(10.dp)) }
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (userData != null) {
                        RowTripListByUser(
                            userId = userData.uid,
                            fetchList = { dataViewModel.loadNextTrips(userData.uid) },
                            onTripTabsClick = onTripTabsClick,
                            onLuggageClick = onLuggageClick,
                            emptyMessage = stringResource(id = R.string.no_trips),
                            trips = trips,
                            type = "row"
                        )
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(50.dp)) }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = stringResource(id = R.string.next_events),
                        style = PageSubTittlesTextStyle
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(10.dp)) }
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (userData != null) {
                        ColumnEventListByUser(
                            userId = userData.uid,
                            fetchList = { dataViewModel.loadNextEvents(userData.uid) },
                            onTripTabsClick = onTripTabsClick,
                            renderItem = { event, onTripTabsClick ->
                                EventsSimpleItem(event, onTripTabsClick)
                            },
                            emptyMessage = stringResource(id = R.string.no_events),
                            events = events
                        )
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }

    if (showDialog.value) {
        if (userData != null) {
            AddTripDialog(
                userId = userData.uid,
                onDismiss = { showDialog.value = false },
                onSave = { trip ->
                    dataViewModel.addTrip(trip)
                    showDialog.value = false
                }
            )
        }
    }
}
