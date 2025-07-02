package themouselabo.appviajes_proyectofinaldam.ui.screens.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.StickyNote2
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.data.getEndDateFromFirebase
import themouselabo.appviajes_proyectofinaldam.data.getStartDateFromFirebase
import themouselabo.appviajes_proyectofinaldam.data.getTripDestination
import themouselabo.appviajes_proyectofinaldam.theme.ButtonTextStyle
import themouselabo.appviajes_proyectofinaldam.theme.PageButtonSize
import themouselabo.appviajes_proyectofinaldam.theme.PageTabTittlesTextStyle
import themouselabo.appviajes_proyectofinaldam.theme.PageTittleTextStyle
import themouselabo.appviajes_proyectofinaldam.ui.components.buttons.CustomIconButton
import themouselabo.appviajes_proyectofinaldam.ui.components.buttons.CustomTextIconButton
import themouselabo.appviajes_proyectofinaldam.ui.components.dialogs.AddEventDialog
import themouselabo.appviajes_proyectofinaldam.ui.components.dialogs.AddNoteDialog
import themouselabo.appviajes_proyectofinaldam.ui.components.viewModel.DataViewModel
import themouselabo.appviajes_proyectofinaldam.ui.components.viewModel.SettingsViewModel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TripTabs(userData: FirebaseUser?, tripId: String?, navController: NavController) {

    val tabOptions = listOf(
        stringResource(R.string.title_documents),
        stringResource(R.string.title_notes),
        stringResource(R.string.title_calendar)
    )

    val tabIcons = listOf(
        Icons.Default.Description,
        Icons.AutoMirrored.Filled.StickyNote2,
        Icons.Default.Event
    )

    val tabIconsText = listOf(
        stringResource(R.string.title_documents),
        stringResource(R.string.add_note),
        stringResource(R.string.add_event)
    )

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val loadingText = stringResource(R.string.loading)
    var tripDestination by remember { mutableStateOf(loadingText) }
    val dataViewModel: DataViewModel = viewModel()
    val openNoteDialog = remember { mutableStateOf(false) }
    val eventDialogShown = remember { mutableStateOf(false) }
    var startDate by remember { mutableStateOf(loadingText) }
    var endDate by remember { mutableStateOf(loadingText) }
    val settingsViewModel: SettingsViewModel = viewModel()

    LaunchedEffect(tripId) {
        tripId?.let {
            val destination = getTripDestination(it)
            tripDestination = destination ?: loadingText

            val startDateTimestamp = getStartDateFromFirebase(it)
            startDate = startDateTimestamp?.let { timestamp ->
                settingsViewModel.formatDate(timestamp)
            } ?: loadingText

            val endDateTimestamp = getEndDateFromFirebase(it)
            endDate = endDateTimestamp?.let { timestamp ->
                settingsViewModel.formatDate(timestamp)
            } ?: loadingText
        }
    }

    Scaffold(
        modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.trip_to, tripDestination),
                            style = PageTittleTextStyle
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        if (selectedTabIndex == 1 || selectedTabIndex == 2) {
                            CustomTextIconButton(
                                onClick = {
                                    if (selectedTabIndex == 1) {
                                        openNoteDialog.value = true
                                    } else if (selectedTabIndex == 2) {
                                        eventDialogShown.value = true
                                    }
                                },
                                icon = tabIcons[selectedTabIndex],
                                text = tabIconsText[selectedTabIndex],
                                styleText = ButtonTextStyle,
                                iconSize = 24.dp,
                                buttonColor = Color.Transparent
                            )
                        }
                    }
                },
                navigationIcon = {
                    CustomIconButton(
                        onClick = { navController.popBackStack() },
                        icon = Icons.AutoMirrored.Filled.ArrowBackIos,
                        iconSize = PageButtonSize
                    )
                }
            )
        },
        bottomBar = {
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabOptions.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title, style = PageTabTittlesTextStyle) },
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = startDate,
                    style = PageTabTittlesTextStyle
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "-",
                    style = PageTabTittlesTextStyle
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = endDate,
                    style = PageTabTittlesTextStyle
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            when (selectedTabIndex) {
                0 -> tripId?.let { DocumentsTab(userData, it) }
                1 -> tripId?.let {
                    NotesTab(userData, it)
                    NotesTabIcon(userData, tripId, openNoteDialog, dataViewModel)
                }

                2 -> tripId?.let {
                    EventsTab(userData, it)
                    EventsTabIcon(userData, tripId, eventDialogShown, dataViewModel)
                }
            }
        }
    }
}


@Composable
fun NotesTabIcon(
    userData: FirebaseUser?,
    tripId: String?,
    openNoteDialog: MutableState<Boolean>,
    dataViewModel: DataViewModel
) {
    if (openNoteDialog.value) {
        if (userData != null) {
            if (tripId != null) {
                AddNoteDialog(
                    userId = userData.uid,
                    tripId = tripId,
                    onDismiss = { openNoteDialog.value = false },
                    onSave = { note ->
                        dataViewModel.addNote(note)
                        openNoteDialog.value = false
                    }
                )
            }
        }
    }
}

@Composable
fun EventsTabIcon(
    userData: FirebaseUser?,
    tripId: String?,
    eventDialogShown: MutableState<Boolean>,
    dataViewModel: DataViewModel
) {
    var startDateTimestamp: Timestamp? by remember { mutableStateOf(null) }
    var endDateTimestamp: Timestamp? by remember { mutableStateOf(null) }
    LaunchedEffect(tripId) {
        tripId.let {
            startDateTimestamp = it?.let { it1 -> getStartDateFromFirebase(it1) }
            endDateTimestamp = it?.let { it1 -> getEndDateFromFirebase(it1) }
        }
    }
    if (eventDialogShown.value) {
        if (userData != null) {
            if (tripId != null) {
                AddEventDialog(
                    userId = userData.uid, tripId = tripId,
                    initialDate = startDateTimestamp!!,
                    finalDate = endDateTimestamp!!,
                    onDismiss = { eventDialogShown.value = false },
                    onSave = { event ->
                        dataViewModel.addEvent(event)
                        eventDialogShown.value = false
                    }
                )
            }
        }
    }
}
