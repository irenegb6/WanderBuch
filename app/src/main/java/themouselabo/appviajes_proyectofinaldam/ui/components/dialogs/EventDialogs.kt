package themouselabo.appviajes_proyectofinaldam.ui.components.dialogs

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import themouselabo.appviajes_proyectofinaldam.R
import themouselabo.appviajes_proyectofinaldam.data.Event
import themouselabo.appviajes_proyectofinaldam.theme.BasicTextStyle
import themouselabo.appviajes_proyectofinaldam.theme.DialogButtonSize
import themouselabo.appviajes_proyectofinaldam.theme.DialogButtonTextStyle
import themouselabo.appviajes_proyectofinaldam.theme.TittleTextStyle
import themouselabo.appviajes_proyectofinaldam.ui.components.buttons.AddDateButton
import themouselabo.appviajes_proyectofinaldam.ui.components.buttons.AddMapButton
import themouselabo.appviajes_proyectofinaldam.ui.components.buttons.AddTimeButton
import themouselabo.appviajes_proyectofinaldam.ui.components.buttons.CustomTextIconButton
import themouselabo.appviajes_proyectofinaldam.utils.DateWithinTripDates
import themouselabo.appviajes_proyectofinaldam.utils.EndTimeAfterStartTime
import java.util.Calendar
import java.util.UUID

@Composable
fun AddEventDialog(
    tripId: String,
    userId: String,
    initialDate: Timestamp,
    finalDate: Timestamp,
    onDismiss: () -> Unit,
    onSave: (Event) -> Unit
) {
    val eventId = UUID.randomUUID().toString()
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf<Timestamp?>(null) }
    var startTime by remember { mutableStateOf<Timestamp?>(null) }
    var endTime by remember { mutableStateOf<Timestamp?>(null) }
    var isMap by remember { mutableStateOf(false) }
    var place by remember { mutableStateOf<GeoPoint?>(null) }
    val context = LocalContext.current

    CustomAlertDialog(
        onDismiss = onDismiss,
        title = {
            Text(
                text = stringResource(id = R.string.dialog_event),
                style = TittleTextStyle
            )
        },
        content = {
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = {
                        Text(
                            text = stringResource(id = R.string.title),
                            style = BasicTextStyle
                        )
                    })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = {
                        Text(
                            text = stringResource(id = R.string.description),
                            style = BasicTextStyle
                        )
                    })
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AddDateButton(
                        onDateSelected = { selectedDate -> date = selectedDate },
                        buttonText = stringResource(id = R.string.date)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AddTimeButton(
                        onTimeSelected = { selectedTime -> startTime = selectedTime },
                        buttonText = stringResource(id = R.string.start_date_time_button)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    AddTimeButton(
                        onTimeSelected = { selectedTime -> endTime = selectedTime },
                        buttonText = stringResource(id = R.string.end_date_time_button)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(id = R.string.start_time),
                        style = BasicTextStyle
                    )
                    Spacer(modifier = Modifier.width(35.dp))
                    Text(
                        text = stringResource(id = R.string.end_time),
                        style = BasicTextStyle
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stringResource(id = R.string.title_map), style = BasicTextStyle)
                    Spacer(modifier = Modifier.width(2.dp))
                    Switch(
                        checked = isMap,
                        onCheckedChange = { isChecked -> isMap = isChecked }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    if (isMap) {
                        AddMapButton(
                            onGeoPointSelected = { selectedGeoPoint ->
                                place = selectedGeoPoint
                            })
                    }
                }
            }
        },
        confirmButton = {
            CustomTextIconButton(
                onClick = {
                    if (title.isNotEmpty() && date != null && startTime != null && endTime != null && (if (isMap) place != null else true)) {
                        if (DateWithinTripDates(date!!, initialDate, finalDate)) {
                            val startCalendar = Calendar.getInstance().apply {
                                time = date!!.toDate()
                                set(Calendar.HOUR_OF_DAY, startTime!!.toDate().hours)
                                set(Calendar.MINUTE, startTime!!.toDate().minutes)
                            }
                            val adjustedStartTime = Timestamp(startCalendar.time)
                            val endCalendar = Calendar.getInstance().apply {
                                time = date!!.toDate()
                                set(Calendar.HOUR_OF_DAY, endTime!!.toDate().hours)
                                set(Calendar.MINUTE, endTime!!.toDate().minutes)
                            }
                            val adjustedEndTime = Timestamp(endCalendar.time)
                            if (EndTimeAfterStartTime(adjustedStartTime, adjustedEndTime)) {
                                val event = Event(
                                    eventId,
                                    title,
                                    description,
                                    date,
                                    adjustedStartTime,
                                    adjustedEndTime,
                                    place,
                                    isMap,
                                    tripId,
                                    userId
                                )
                                onSave(event)
                                onDismiss()
                            } else {
                                Toast.makeText(
                                    context,
                                    R.string.error_end_time_before_start_time,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                context,
                                R.string.error_date_between_trip_dates,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            context,
                            R.string.error_all_fields_required,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                icon = Icons.Default.Save,
                text = stringResource(id = R.string.save),
                styleText = DialogButtonTextStyle,
                iconSize = DialogButtonSize
            )
        },
        dismissButton = {
            CustomTextIconButton(
                onClick = {
                    onDismiss()
                },
                icon = Icons.Default.Cancel,
                text = stringResource(id = R.string.cancel),
                styleText = DialogButtonTextStyle,
                iconSize = DialogButtonSize
            )
        }
    )
}

@Composable
fun EditEventDialog(
    event: Event,
    onDismiss: () -> Unit,
    onSave: (Event) -> Unit
) {
    var title by remember { mutableStateOf(event.title) }
    var description by remember { mutableStateOf(event.description) }
    var date by remember { mutableStateOf(event.date) }
    var startTime by remember { mutableStateOf(event.startTime) }
    var endTime by remember { mutableStateOf(event.endTime) }
    var isMap by remember { mutableStateOf(event.isMap) }
    var place by remember { mutableStateOf(event.place) }
    val context = LocalContext.current

    CustomAlertDialog(
        onDismiss = onDismiss,
        title = {
            Text(
                text = stringResource(id = R.string.dialog_event),
                style = TittleTextStyle
            )
        },
        content = {
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = {
                        Text(
                            text = stringResource(id = R.string.title),
                            style = BasicTextStyle
                        )
                    })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = {
                        Text(
                            text = stringResource(id = R.string.description),
                            style = BasicTextStyle
                        )
                    })
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AddDateButton(
                        onDateSelected = { selectedDate -> date = selectedDate },
                        buttonText = stringResource(id = R.string.date)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AddTimeButton(
                        onTimeSelected = { selectedTime -> startTime = selectedTime },
                        buttonText = stringResource(id = R.string.start_time)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    AddTimeButton(
                        onTimeSelected = { selectedTime -> endTime = selectedTime },
                        buttonText = stringResource(id = R.string.end_time)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stringResource(id = R.string.title_map), style = BasicTextStyle)
                    Spacer(modifier = Modifier.width(2.dp))
                    Switch(
                        checked = isMap,
                        onCheckedChange = { isChecked -> isMap = isChecked }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    if (isMap) {
                        AddMapButton(
                            onGeoPointSelected = { selectedGeoPoint -> place = selectedGeoPoint }
                        )
                    }
                }
            }
        },
        confirmButton = {
            CustomTextIconButton(
                onClick = {
                    if (title.isNotEmpty() && date != null && startTime != null && endTime != null && (if (isMap) place != null else true)) {
                        val adjustedStartTime = Timestamp(startTime!!.toDate())
                        val adjustedEndTime = Timestamp(endTime!!.toDate())
                        if (EndTimeAfterStartTime(adjustedStartTime, adjustedEndTime)) {
                            val updatedEvent = event.copy(
                                title = title,
                                description = description,
                                date = date,
                                startTime = adjustedStartTime,
                                endTime = adjustedEndTime,
                                place = place,
                                isMap = isMap
                            )
                            onSave(updatedEvent)
                            onDismiss()
                        } else {
                            Toast.makeText(
                                context,
                                R.string.error_end_time_before_start_time,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            context,
                            R.string.error_all_fields_required,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                icon = Icons.Default.Save,
                text = stringResource(id = R.string.save),
                styleText = DialogButtonTextStyle,
                iconSize = DialogButtonSize
            )
        },
        dismissButton = {
            CustomTextIconButton(
                onClick = {
                    onDismiss()
                },
                icon = Icons.Default.Cancel,
                text = stringResource(id = R.string.cancel),
                styleText = DialogButtonTextStyle,
                iconSize = DialogButtonSize
            )
        }
    )
}
