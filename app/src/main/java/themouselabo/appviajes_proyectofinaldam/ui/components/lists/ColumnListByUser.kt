package themouselabo.appviajes_proyectofinaldam.ui.components.lists

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.data.Event
import themouselabo.appviajes_proyectofinaldam.theme.EmptyTextStyle
import themouselabo.appviajes_proyectofinaldam.utils.formatEventDate
import java.util.Calendar

@Composable
fun ColumnEventListByUser(
    userId: String,
    fetchList: () -> Unit,
    onTripTabsClick: (String) -> Unit,
    renderItem: @Composable (Event, (String) -> Unit) -> Unit,
    emptyMessage: String,
    events: List<Event>?
) {
    val currentHour = Timestamp.now()
    val hoursLater = Calendar.getInstance().apply { add(Calendar.HOUR, 48) }.time

    LaunchedEffect(userId) {
        fetchList()
    }

    if (events == null) {
        Text(stringResource(id = R.string.loading))
    } else {
        if (events.isEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(text = emptyMessage, style = EmptyTextStyle)
            }
        } else {
            val filteredEvents = events.filter {
                val startTime = it.startTime?.toDate()
                val endTime = it.endTime?.toDate()
                startTime != null && endTime != null &&
                        endTime.after(currentHour.toDate()) && startTime.before(hoursLater)
            }
            val groupedEvents = filteredEvents.groupBy {
                it.date?.toDate()?.toInstant()?.atZone(java.time.ZoneId.systemDefault())
                    ?.toLocalDate()
            }

            Column(modifier = Modifier.fillMaxWidth(0.95f)) {
                groupedEvents.forEach { (date, events) ->
                    Row(modifier = Modifier.padding(vertical = 8.dp)) {
                        Column(modifier = Modifier.weight(1f)) {
                            events.forEach { event ->
                                renderItem(event, onTripTabsClick)
                            }
                        }
                        Column(
                            modifier = Modifier
                                .weight(0.2f)
                                .align(Alignment.Top),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = date?.let { formattedDate ->
                                    formatEventDate(formattedDate)
                                } ?: "",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(top = 10.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .height(2.dp)
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.primary)
                            )
                        }
                    }
                }
            }
        }
    }
}
