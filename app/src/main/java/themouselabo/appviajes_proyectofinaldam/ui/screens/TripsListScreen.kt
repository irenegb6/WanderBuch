package themouselabo.appviajes_proyectofinaldam.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseUser
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.theme.PageButtonSize
import themouselabo.appviajes_proyectofinaldam.theme.PageTittleTextStyle
import themouselabo.appviajes_proyectofinaldam.ui.components.lists.ColumnTripListByUser
import themouselabo.appviajes_proyectofinaldam.ui.components.viewModel.DataViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripsListScreen(
    userData: FirebaseUser?,
    onTripTabsClick: (String) -> Unit,
    onLuggageClick: (String) -> Unit,
    navController: NavController
) {
    val tabOptions =
        listOf(stringResource(R.string.future_trips), stringResource(R.string.past_trips))
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val dataViewModel: DataViewModel = viewModel()
    val trips = dataViewModel.tripsState.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.title_trip_list),
                        style = PageTittleTextStyle
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBackIos,
                            contentDescription = null,
                            modifier = Modifier.size(PageButtonSize)
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabOptions.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index })
                }
            }
            when (selectedTabIndex) {
                0 -> {
                    if (userData != null) {
                        ColumnTripListByUser(
                            userId = userData.uid,
                            fetchList = { dataViewModel.loadFutureTrips(userData.uid) },
                            onTripTabsClick = onTripTabsClick,
                            onLuggageClick = onLuggageClick,
                            emptyMessage = stringResource(id = R.string.no_trips),
                            trips = trips,
                            key = "future_trips",
                            type = "column"
                        )
                    }
                }

                1 -> {
                    if (userData != null) {
                        ColumnTripListByUser(
                            userId = userData.uid,
                            fetchList = { dataViewModel.loadPastTrips(userData.uid) },
                            onTripTabsClick = onTripTabsClick,
                            onLuggageClick = onLuggageClick,
                            emptyMessage = stringResource(id = R.string.no_trips),
                            trips = trips,
                            key = "past_trips",
                            type = "column"
                        )
                    }
                }
            }
        }
    }
}
