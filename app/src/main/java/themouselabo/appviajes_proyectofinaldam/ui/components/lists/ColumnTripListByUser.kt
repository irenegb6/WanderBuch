package themouselabo.appviajes_proyectofinaldam.ui.components.lists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import themouselabo.appviajes_proyectofinaldam.data.Trip
import themouselabo.appviajes_proyectofinaldam.theme.EmptyTextStyle
import themouselabo.appviajes_proyectofinaldam.ui.components.items.TripsItem

@Composable
fun ColumnTripListByUser(
    userId: String,
    fetchList: () -> Unit,
    onTripTabsClick: (String) -> Unit,
    onLuggageClick: (String) -> Unit,
    emptyMessage: String,
    trips: List<Trip>,
    key: String,
    type: String
) {
    LaunchedEffect(userId, key) {
        fetchList()
    }

    if (trips.isEmpty()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(text = emptyMessage, style = EmptyTextStyle)
        }
    } else {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            items(trips, key = { trip -> trip.id }) { trip -> // Key each item by its id
                TripsItem(trip, onTripTabsClick, onLuggageClick, type)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

