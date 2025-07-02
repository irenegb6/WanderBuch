package themouselabo.appviajes_proyectofinaldam.ui.components.lists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import themouselabo.appviajes_proyectofinaldam.data.Trip
import themouselabo.appviajes_proyectofinaldam.theme.EmptyTextStyle
import themouselabo.appviajes_proyectofinaldam.ui.components.items.TripsItem

@Composable
fun RowTripListByUser(
    userId: String,
    fetchList: () -> Unit,
    onTripTabsClick: (String) -> Unit,
    onLuggageClick: (String) -> Unit,
    emptyMessage: String,
    trips: List<Trip>,
    type: String
) {
    LaunchedEffect(userId) {
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
        LazyRow {
            items(trips) { trip ->
                TripsItem(trip, onTripTabsClick, onLuggageClick, type)
            }
        }
    }
}