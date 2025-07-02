package themouselabo.appviajes_proyectofinaldam.ui.screens.tabs

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.data.getEndDateFromFirebase
import themouselabo.appviajes_proyectofinaldam.data.getStartDateFromFirebase
import themouselabo.appviajes_proyectofinaldam.theme.DayTextStyle
import themouselabo.appviajes_proyectofinaldam.theme.EmptyTextStyle
import themouselabo.appviajes_proyectofinaldam.theme.MonthYearTextStyle
import themouselabo.appviajes_proyectofinaldam.ui.components.buttons.CustomDateButton
import themouselabo.appviajes_proyectofinaldam.ui.components.items.EventsItem
import themouselabo.appviajes_proyectofinaldam.ui.components.viewModel.DataViewModel
import themouselabo.appviajes_proyectofinaldam.utils.convertLocalDateToTimestamp
import themouselabo.appviajes_proyectofinaldam.utils.convertTimestampToLocalDate
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EventsTab(userData: FirebaseUser?, tripId: String) {
    var startDateTimestamp: Timestamp? by remember { mutableStateOf(null) }
    var endDateTimestamp: Timestamp? by remember { mutableStateOf(null) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    val dataviewModel: DataViewModel = viewModel()

    LaunchedEffect(tripId) {
        tripId.let {
            startDateTimestamp = getStartDateFromFirebase(it)
            endDateTimestamp = getEndDateFromFirebase(it)
        }
    }

    val startDateTime: LocalDate? = startDateTimestamp?.let {
        convertTimestampToLocalDate(it)
    }
    val endDateTime: LocalDate? = endDateTimestamp?.let {
        convertTimestampToLocalDate(it)
    }

    val dateList = remember(startDateTime, endDateTime) {
        if (startDateTime != null && endDateTime != null) {
            (0..ChronoUnit.DAYS.between(startDateTime, endDateTime)).map {
                startDateTime.plusDays(it)
            }
        } else {
            emptyList()
        }
    }

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(align = Alignment.Center)
            ) {
                item { Spacer(modifier = Modifier.height(5.dp)) }
                items(dateList) { date ->
                    CustomDateButton(onClick = { selectedDate = date }, text = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = date.dayOfMonth.toString(), style = DayTextStyle)
                            Text(text = date.month.toString(), style = MonthYearTextStyle)
                            Text(text = date.year.toString(), style = MonthYearTextStyle)
                        }
                    })
                    Spacer(modifier = Modifier.width(2.dp))
                }
            }
            selectedDate?.let { date ->
                val dateTimestamp = convertLocalDateToTimestamp(date)
                ShowListByDate(tripId, dateTimestamp, userData?.uid ?: "", dataviewModel)
            }
        }
    }
}

@Composable
fun ShowListByDate(
    tripId: String,
    date: Timestamp,
    userId: String,
    dataviewModel: DataViewModel
) {
    val events = dataviewModel.eventsState.value
    LaunchedEffect(tripId, date, userId) {
        dataviewModel.loadEventsByDateTrip(userId, tripId, date)
    }
    if (events.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(events) { event ->
                EventsItem(event)
            }
        }
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = stringResource(id = R.string.no_events),
                style = EmptyTextStyle
            )
        }
    }
}



