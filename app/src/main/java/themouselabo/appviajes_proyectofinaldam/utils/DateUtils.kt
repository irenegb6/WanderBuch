package themouselabo.appviajes_proyectofinaldam.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.Timestamp
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.theme.PageSettingsOptionsTextStyle
import themouselabo.appviajes_proyectofinaldam.ui.components.cards.SettingsCard
import themouselabo.appviajes_proyectofinaldam.ui.components.viewModel.SettingsViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

enum class DateFormat {
    FORMAT_DDMMYYYY,
    FORMAT_MMDDYYYY,
    FORMAT_YYYYMMDD
}

fun formatEventDate(date: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMM", Locale.getDefault())
    return date.format(formatter)
}

fun endDateAfterStartDate(startDate: Timestamp, endDate: Timestamp): Boolean {
    return endDate.toDate().after(startDate.toDate())
}

fun DateWithinTripDates(date: Timestamp, initialDate: Timestamp, finalDate: Timestamp): Boolean {
    return !date.toDate().before(initialDate.toDate()) && !date.toDate().after(finalDate.toDate())
}

@Composable
fun DateFormatSetting(onDateFormatSelected: (DateFormat) -> Unit) {
    val settingsViewModel: SettingsViewModel = viewModel()
    val selectedDateFormat by settingsViewModel.dateFormat

    val options = listOf(
        DateFormat.FORMAT_DDMMYYYY to stringResource(R.string.dd_MM_yyyy),
        DateFormat.FORMAT_MMDDYYYY to stringResource(R.string.MM_dd_yyyy),
        DateFormat.FORMAT_YYYYMMDD to stringResource(R.string.yyyy_MM_dd)
    )

    SettingsCard(
        title = stringResource(R.string.settings_date_format),
        icon = Icons.Default.DateRange,
        content = {
            Column {
                options.forEach { (format, label) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        RadioButton(
                            selected = selectedDateFormat == format,
                            onClick = {
                                settingsViewModel.setDateFormat(format)
                                onDateFormatSelected(format)
                            }
                        )
                        Text(
                            text = label,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .align(Alignment.CenterVertically),
                            style = PageSettingsOptionsTextStyle
                        )
                    }
                }
            }
        }
    )
}


fun convertTimestampToLocalDate(timestamp: Timestamp): LocalDate? {
    val instant = Instant.ofEpochSecond(timestamp.seconds)
    val zoneId = ZoneId.systemDefault()
    return instant.atZone(zoneId).toLocalDate()
}

fun convertLocalDateToTimestamp(date: LocalDate): Timestamp {
    val zoneId =
        ZoneId.systemDefault()
    val instant = date.atStartOfDay(zoneId).toInstant()

    return Timestamp(
        instant.toEpochMilli() / 1000,
        ((instant.toEpochMilli() % 1000) * 1000000).toInt()
    )
}
